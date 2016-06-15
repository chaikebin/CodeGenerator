package com.alibaba.codegenerator.model;

import com.alibaba.codegenerator.util.JavaTypeResolver;
import com.alibaba.codegenerator.util.StringUtil;

public class Column {

	private String columnName;
	private boolean primaryKey;
	private boolean nullable;
	private boolean unique;
	private boolean autoincrement;
	private String defaultValue;
	protected int jdbcType;
	protected String jdbcTypeName;
	private String javaProperty;
	private String javaType;
	private String fullJavaType;
	private int size;
	private String remarks;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Column(String columnName) {
		this.columnName = columnName;
		this.javaProperty = StringUtil.getCamelCaseString(columnName, false);
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isAutoincrement() {
		return autoincrement;
	}

	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(int jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getJdbcTypeName() {
		return jdbcTypeName;
	}

	public void setJdbcTypeName(String jdbcTypeName) {
		this.jdbcTypeName = jdbcTypeName;
	}

	public String getJavaProperty() {
		return javaProperty;
	}

	public void setJavaProperty(String javaProperty) {
		this.javaProperty = javaProperty;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getFullJavaType() {
		return fullJavaType;
	}

	public void setFullJavaType(String fullJavaType) {
		this.fullJavaType = fullJavaType;
	}
	
    public boolean isDate() {
        return JavaTypeResolver.isDate(javaType);
    }
    
    public String getGetterMethodName() {
        StringBuilder sb = new StringBuilder();

        sb.append(javaProperty);
        if (Character.isLowerCase(sb.charAt(0)) && ((sb.length() == 1) || Character.isLowerCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        if (JavaTypeResolver.isBoolean(javaType)) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }

        return sb.toString();
    }

    public String getSetterMethodName() {
        StringBuilder sb = new StringBuilder();

        sb.append(javaProperty);
        if (Character.isLowerCase(sb.charAt(0)) && ((sb.length() == 1) || Character.isLowerCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        sb.insert(0, "set");

        return sb.toString();
    }

}