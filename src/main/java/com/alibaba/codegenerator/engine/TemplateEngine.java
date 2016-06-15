package com.alibaba.codegenerator.engine;

import java.io.File;
import java.util.Map;

public interface TemplateEngine {

	public void processToFile(File templateFile, File targetFile,
			Map<String, Object> params);

}
