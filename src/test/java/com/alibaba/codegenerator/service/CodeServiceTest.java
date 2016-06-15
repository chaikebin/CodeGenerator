package com.alibaba.codegenerator.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.codegenerator.GeneratorTest;
import com.alibaba.codegenerator.model.Code;
import com.alibaba.codegenerator.model.Column;
import com.alibaba.codegenerator.model.Table;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(GeneratorTest.class)
public class CodeServiceTest {
	
	@Autowired
	private CodeService codeService;
	
	@Test
	public void makeCode() {
		Code code = new Code();
		code.setPackageName("com.alibaba.staragent.user");
		code.setProjectName("staragent-user");
		code.setVersion("2.0.0-SNAPSHOT");
		addTable(code);
		codeService.makeCode(code);
	}

	private void addTable(Code code) {
		code.addTable(fillUserTable());
	}

	private Table fillUserTable() {
		Table table = new Table();
		table.setTableName("t_staragent_user");
		Column column = new Column("oid");
		column.setPrimaryKey(true);
		table.addPrimaryKey(column);
		return table;
	}

}
