package com.alibaba.codegenerator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.alibaba.codegenerator.model.Code;
import com.alibaba.codegenerator.service.CodeService;
import com.alibaba.codegenerator.service.TableService;

@SpringBootApplication
@Component
@ComponentScan
public class Generator implements CommandLineRunner {

	@Autowired
	private TableService tableService;
	@Autowired
	private CodeService codeService;
	
	private static Code code = new Code();

	public static void main(String[] args) {
		validation(args);
		SpringApplication.run(Generator.class, args);
	}

	private static void validation(String[] args) {
		if (args == null || args.length < 2) {
			echo();
			System.exit(0);
		}
		parseArgs(code, args);
	}

	@Override
	public void run(String... args) throws Exception {
		code.setTables(tableService.generate(code.getTableNameList()));
		codeService.makeCode(code);
	}

	private static void parseArgs(Code code, String... args) {
		for (String arg : args) {
			if (arg.startsWith("packageName=")) {
				code.setPackageName(arg.substring("packageName=".length()));
			} else if (arg.startsWith("tableName=")) {
				code.setTableName(arg.substring("tableName=".length()));
			}
		}
		if (StringUtils.isEmpty(code.getPackageName())
				|| StringUtils.isEmpty(code.getTableName())) {
			echo();
			System.exit(0);
		}
	}

	private static void echo() {
		System.out.print("dddddddddddd");
	}
}