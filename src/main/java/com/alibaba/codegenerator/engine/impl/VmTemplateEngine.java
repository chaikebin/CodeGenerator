package com.alibaba.codegenerator.engine.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.codegenerator.engine.TemplateEngine;

@Service
public class VmTemplateEngine implements TemplateEngine {

	private VelocityEngine ve;

	private static final Logger logger = LoggerFactory
			.getLogger(VmTemplateEngine.class);

	@PostConstruct
	public void init() {
		ve = new VelocityEngine();
		ve.init();
	}

	@Override
	public void processToFile(File templateFile, File targetFile,
			Map<String, Object> params) {
		FileWriterWithEncoding writer = null;
		try {
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			Template template = ve.getTemplate(
					templateFile.getAbsolutePath().substring(
							templateFile.getAbsolutePath().indexOf(
									"templates")), "UTF-8");
			VelocityContext context = new VelocityContext();
			Iterator<String> keys = params.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				context.put(key, params.get(key));
			}
			writer = new FileWriterWithEncoding(targetFile, "UTF-8");
			template.merge(context, writer);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
