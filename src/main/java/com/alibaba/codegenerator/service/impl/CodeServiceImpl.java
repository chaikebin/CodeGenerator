package com.alibaba.codegenerator.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.codegenerator.engine.TemplateEngine;
import com.alibaba.codegenerator.model.Code;
import com.alibaba.codegenerator.model.Table;
import com.alibaba.codegenerator.service.CodeService;

@Service
public class CodeServiceImpl implements CodeService {

	private String targetCodeDir = "targetCode";
	private String templatePath = "templates/default";

	private static final Logger logger = LoggerFactory
			.getLogger(CodeServiceImpl.class);

	@Autowired
	private TemplateEngine templateEngine;

	@Override
	public void makeCode(Code code) {
		try {
			File targetDir = initTargetDir();
			File[] files = new File(templatePath).listFiles();
			if (files == null || files.length == 0) {
				return;
			}
			for (File file : files) {
				generalCode(file, targetDir, code);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void generalCode(File file, File targetDir, Code code)
			throws IOException {
		if (file.isDirectory()) {
			makeDir(file, targetDir, code);
		} else {
			makeFile(file, targetDir, code);
		}
	}

	private void makeFile(File file, File targetDir, Code code)
			throws IOException {
		if (file.getName().startsWith("_")) {
			if (file.getName().contains("$ClassName$")) {
				doClassFile(file, targetDir, code);
			} else {
				doTemplateFile(file, targetDir, code);
			}
		} else {
			FileUtils.copyFile(file, new File(targetDir, file.getName()));
		}
	}

	private void doTemplateFile(File file, File targetDir, Code code)
			throws IOException {
		Map<String, Object> params = fillCodeParams(code);
		templateEngine.processToFile(file, new File(targetDir, file.getName()
				.substring(1)), params);
	}

	private Map<String, Object> fillCodeParams(Code code) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("packageName", code.getPackageName());
		params.put("projectName", code.getProjectName());
		params.put("version", code.getVersion());
		return params;
	}

	private void doClassFile(File file, File targetDir, Code code)
			throws IOException {
		if (code.getTables() == null) {
			return;
		}
		for(Table table:code.getTables()) {
			Map<String, Object> params = fillCodeParams(code);
			params.put("table", table);
			params.put("columns", table.getColumns());
			templateEngine.processToFile(file, new File(targetDir, file.getName()
					.substring(1).replaceAll("$ClassName$", table.getClassName())), params);
		}
	}

	private void makeDir(File file, File targetDir, Code code)
			throws IOException {
		if (file.getName().contains("$PackageName$")) {
			String[] packageNames = code.getPackageName().split("\\.");
			for (String packageName : packageNames) {
				targetDir = new File(targetDir, packageName);
				targetDir.mkdir();
			}
		} else {
			targetDir = new File(targetDir, file.getName());
			targetDir.mkdir();
		}
		File[] subFiles = file.listFiles();
		if (subFiles != null && subFiles.length > 0) {
			for (File subFile : subFiles) {
				generalCode(subFile, targetDir, code);
			}
		}

	}

	private File initTargetDir() throws IOException {
		File targetDir = new File(System.getProperty("user.dir"), targetCodeDir);
		FileUtils.deleteDirectory(targetDir);
		targetDir.mkdir();
		return targetDir;
	}
}