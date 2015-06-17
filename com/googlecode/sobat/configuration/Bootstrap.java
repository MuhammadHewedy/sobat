package com.googlecode.sobat.configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.googlecode.sobat.configuration.MappingXmlWrapper.ClassElement;
import com.googlecode.sobat.ql.dialect.Dialect;
import com.googlecode.sobat.ql.dialect.MySQLDialect;

/**
 * 
 * Note: <code>sobat.mapping</code> can be used as a property to set the name and the location of the mapping.xml file 
 * 
 * @author mohammed hewedy
 *
 */
public class Bootstrap {
	
	public Bootstrap() {}
	
	private static Map<String, Document>  objectsDomMap = new HashMap<String, Document>();
	private static Document mappingDocument;
	private static String mappingFileName;
	private static MappingXmlWrapper mappingXmlWrapper;
	private static Logger logger = Logger.getLogger(Bootstrap.class.getName());
	
	public static Map<String, Document> getObjcetsMocumentMap(){
		return objectsDomMap;
	}
	
	public static Document getMappingDocument() {
		return mappingDocument;
	}
	
	public static MappingXmlWrapper getMappingXmlWrapper() {
		return mappingXmlWrapper;
	}
	
	public static Dialect getDialect() {
		Dialect dialect = null;
		DialectEnum dialectValue = DialectEnum.valueOf(MappingXmlWrapper.getBootstrapInstance().getDriverManagerElement().getDialect());
		switch (dialectValue) {
			case MySQLDialect:
				dialect = new MySQLDialect();
				break;
			default:
				throw new RuntimeException(dialectValue + "Invalid Dialect");
		}
		return dialect;
	}
	
	static {
		try {
			logger.info("Initializing sobat..");
			initMappingDocument();
			initMappingXmlWrapper();
			initDocumentList();
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new ExceptionInInitializerError(ex);
		}
	}
	private static void initMappingDocument() {
		try {
			initMappingFile();
			InputStream mappingStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(mappingFileName);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			mappingDocument = builder.parse(mappingStream);
			mappingStream.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private static void initDocumentList() throws Exception {
		final Set<String> pathSet = new HashSet<String>();
		List<ClassElement> celeList =  mappingXmlWrapper.getClassesElement();
		for (ClassElement  cele : celeList) {
			pathSet.add(cele.getPath());
		}
		loadObjectDocuments(pathSet);
	}
	
	private static void loadObjectDocuments(final Set<String> pathSet) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		for (String path : pathSet) {
			InputStream pathStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			if (pathStream == null)
				throw new RuntimeException(path + " Not found");
			Document doc = builder.parse(pathStream);
			objectsDomMap.put(path, doc);
			pathStream.close();
		}
	}
		
	private static void initMappingFile() {
		mappingFileName = System.getProperty("sobat.mapping");
		if (mappingFileName == null)
			mappingFileName = "mapping.xml";
	}
	
	private static void initMappingXmlWrapper() {
		mappingXmlWrapper = new MappingXmlWrapper();
		mappingXmlWrapper.initialize(mappingDocument);
	}
	public enum DialectEnum {
		MySQLDialect("MySQLDialect");
		private String dialect; 
		private DialectEnum(String dialect) {
			this.dialect = dialect;
		}
	}
}
