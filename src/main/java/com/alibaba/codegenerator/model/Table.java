package com.alibaba.codegenerator.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.codegenerator.util.StringUtil;

public class Table {

	private String tableName;
	private List<Column> baseColumns = new ArrayList<Column>();
	private List<Column> primaryKeys = new ArrayList<Column>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClassName() {
		return StringUtil.getCamelCaseString(tableName, false);
	}

	public List<Column> getBaseColumns() {
		return baseColumns;
	}

	public void setBaseColumns(List<Column> baseColumns) {
		this.baseColumns = baseColumns;
	}

	public List<Column> getPrimaryKeys() {
		return primaryKeys;
	}

	public void setPrimaryKeys(List<Column> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public Column getColumn(String columnName) {
		for (Column column : primaryKeys) {
			if (column.getColumnName().equals(columnName)) {
				return column;
			}
		}
		for (Column column : baseColumns) {
			if (column.getColumnName().equals(columnName)) {
				return column;
			}
		}
		return null;
	}

	public List<Column> getColumns() {
		List<Column> allColumns = new ArrayList<Column>();
		allColumns.addAll(primaryKeys);
		allColumns.addAll(baseColumns);
		return allColumns;
	}

	public void addPrimaryKey(Column primaryKeyColumn) {
		this.primaryKeys.add(primaryKeyColumn);
	}

	public void addBaseColumn(Column baseColumn) {
		this.baseColumns.add(baseColumn);
	}

    public boolean isHasDateColumn() {
        for (Column column : getColumns()) {
            if (column.isDate()) {
                return true;
            }
        }
        return false;
    }
}