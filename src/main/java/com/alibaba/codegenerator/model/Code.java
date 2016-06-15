package com.alibaba.codegenerator.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code {

	private String packageName = "";
	private String projectName = "";
	private String version = "";
	private String tableName = "";
	private String codeTemplatePath = "";
	private List<String> tableNameList;
	private List<Table> tables = new ArrayList<Table>();
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<Table> getTables() {
		return tables;
	}
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	public void addTable(Table table) {
		tables.add(table);
	}
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
		tableNameList = Arrays.asList(tableName.split(","));
	}
	public String getCodeTemplatePath() {
		return codeTemplatePath;
	}
	public void setCodeTemplatePath(String codeTemplatePath) {
		this.codeTemplatePath = codeTemplatePath;
	}
	public List<String> getTableNameList() {
		return tableNameList;
	}
}
