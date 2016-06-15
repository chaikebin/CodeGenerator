package com.alibaba.codegenerator.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.codegenerator.config.DruidConfig;
import com.alibaba.codegenerator.model.Column;
import com.alibaba.codegenerator.model.Table;
import com.alibaba.codegenerator.service.TableService;
import com.alibaba.codegenerator.util.JdbcTypeResolver;
import com.alibaba.codegenerator.util.StringUtil;
import com.alibaba.codegenerator.util.TypeMapping;

@Service
public class TableServiceImpl implements TableService {

	@Autowired
	private DruidConfig druidConfig;
	private Connection connection;
	@Autowired
	private TypeMapping typeMapping;
	private static final String COLUMN_NAME = "COLUMN_NAME";

	private static final Logger logger = LoggerFactory
			.getLogger(TableServiceImpl.class);

	@Override
	public List<Table> generate(List<String> tableNames) {
		List<Table> tables = new ArrayList<Table>();
		try {
			connection = druidConfig.mysqlDataSource().getConnection();
			for (String tableName : tableNames) {
				Table table = generateTable(tableName);
				if (table != null) {
					tables.add(table);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return tables;
	}

	private Table generateTable(String tableName) throws SQLException {
		Table table = null;
		ResultSet rs = null;
		try {
			rs = connection.getMetaData().getTables(null, null, tableName,
					new String[] { "TABLE", "VIEW" });
			if (rs.next()) {
				table = new Table();
				table.setTableName(tableName);
				makePrimaryKeys(table);
				makeColumns(table);
			}
			return table;
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs);
		}
	}

	private void makeColumns(Table table) throws SQLException {
		ResultSet rs = null;
		try {
			rs = connection.getMetaData().getColumns(null, null,
					table.getTableName(), "%");
			while (rs.next()) {
				String columnName = rs.getString(COLUMN_NAME);
				if (StringUtil.isEmpty(columnName)) {
					continue;
				}

				Column column = table.getColumn(columnName);
				if (column == null) {
					column = new Column(columnName);
					table.addBaseColumn(column);
				}
				addColumnInfo(column, rs, columnName);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs);
		}
	}

	private void addColumnInfo(Column column, ResultSet rs, String columnName)
			throws SQLException {
		column.setJdbcType(rs.getInt("DATA_TYPE"));
		column.setSize(rs.getInt("COLUMN_SIZE"));
		column.setNullable(rs.getInt("NULLABLE") == 1);
		column.setDefaultValue(rs.getString("COLUMN_DEF"));
		column.setRemarks(rs.getString("REMARKS"));
		column.setAutoincrement(rs.getBoolean("IS_AUTOINCREMENT"));
		column.setJdbcTypeName(JdbcTypeResolver.getJdbcTypeName(column
				.getJdbcType()));
		column.setJavaType(typeMapping.calculateJavaType(column));
		column.setFullJavaType(typeMapping.calculateFullJavaType(column));
		column.setJavaProperty(StringUtil.getCamelCaseString(columnName, false));
	}

	private void makePrimaryKeys(Table table) throws SQLException {
		ResultSet rs = null;
		try {
			rs = connection.getMetaData().getPrimaryKeys(null, null,
					table.getTableName());
			while (rs.next()) {
				String columnName = rs.getString(COLUMN_NAME);
				Column column = table.getColumn(columnName);
				if (column == null) {
					column = new Column(columnName);
					table.addPrimaryKey(column);
				}
				column.setPrimaryKey(true);
				addColumnInfo(column, rs, columnName);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			close(rs);
		}
	}

	private void close(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
