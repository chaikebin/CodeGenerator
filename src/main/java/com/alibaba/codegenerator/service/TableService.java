package com.alibaba.codegenerator.service;

import java.util.List;

import com.alibaba.codegenerator.model.Table;

public interface TableService {
	
	public List<Table> generate(List<String> tableNames);

}
