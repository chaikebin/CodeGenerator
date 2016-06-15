package com.alibaba.codegenerator.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.omg.CORBA.portable.ApplicationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.codegenerator.model.Column;

public class TypeMapping implements Serializable {

    private static final long serialVersionUID = 8573950623347746321L;

    private static final String  MAPING_FILE = "TypeMapping.xml";

    private String               mappginFile;

    private Map<Integer, String> typeMap;
    private Map<Integer, String> fullTypeMap;

    public TypeMapping(String classPath){
        this.mappginFile = classPath + MAPING_FILE;
        typeMap = new HashMap<Integer, String>();
        fullTypeMap = new HashMap<Integer, String>();
    }

    public void loadMappgin() throws ApplicationException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(mappginFile);
            Element rootNode = doc.getDocumentElement();

            NodeList nodeList = rootNode.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node childNode = nodeList.item(i);

                if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                if ("map".equals(childNode.getNodeName())) { //$NON-NLS-1$
                    Properties attrs = parseAttributes(childNode);
                    int sqlType = JdbcTypeResolver.getJdbcType(attrs.getProperty("sqlType"));
                    typeMap.put(sqlType, attrs.getProperty("javaType"));
                    fullTypeMap.put(sqlType, attrs.getProperty("fullJavaType"));
                }
            }
        } catch (Exception e) {
        }
    }

    private Properties parseAttributes(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = attribute.getNodeValue();
            attributes.put(attribute.getNodeName(), value);
        }

        return attributes;
    }

    public String calculateJavaType(Column column) {
        String javaType = typeMap.get(column.getJdbcType());

        if (javaType == null) {
            javaType = JavaTypeResolver.calculateJavaType(column);
        }
        return javaType;
    }

    public String calculateFullJavaType(Column column) {
        String javaType = fullTypeMap.get(column.getJdbcType());

        if (javaType == null) {
            javaType = JavaTypeResolver.calculateFullJavaType(column);
        }
        return javaType;
    }

    public String[] getAllJavaTypes() {
        Set<String> javaTypeSet = new HashSet<String>();
        javaTypeSet.addAll(typeMap.values());
        if(javaTypeSet.isEmpty()){
            return JavaTypeResolver.getAllJavaTypes();
        }

        String[] values = new String[javaTypeSet.size()];
        int index = 0;
        for (String itemValue : javaTypeSet) {
            values[index++] = itemValue;
        }
        return values;
    }
}
