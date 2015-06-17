package com.googlecode.sobat.configuration;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.sobat.util.StringUtil;

public class MappingXmlWrapper {
	
	MappingXmlWrapper() {};
	
	public static MappingXmlWrapper getBootstrapInstance() {
		return Bootstrap.getMappingXmlWrapper();
	}
	
	private Document mappingDocument;
	
	public DatasourceElement getDatasourceElement() {
		Node mapping = mappingDocument.getDocumentElement();
		final String datasource = "datasource";
		final String jndi = "jndi";
		final String initialContextFactory = "initial-context-factory";
		final String providerUrl = "provider-url";		
		boolean isJndiMissing = true;
		
		NodeList mappings = mapping.getChildNodes();
		for (int i=0; i <mappings.getLength(); i++) {
			Node dsNode = mappings.item(i);
			if (datasource.equals(dsNode.getNodeName())) {
				DatasourceElement dsele = new DatasourceElement();
				String jndiName = mappings.item(i).getAttributes().getNamedItem(jndi).getNodeValue();
				dsele.setJndi(jndiName);
				if (StringUtil.isNullSpacesOrEmpty(jndiName))
					isJndiMissing = false;
				NodeList children = dsNode.getChildNodes();
				
				for (int j=0; j <children.getLength(); j++) {
					Node subele = children.item(j);
					if (initialContextFactory.equals(subele.getNodeName())) {
						dsele.setInitialContextFactory(subele.getTextContent());
					}
					if (providerUrl.equals(subele.getNodeName())) {
						dsele.setProviderUrl(subele.getTextContent());
					}
				}
				if (isJndiMissing)
					throw new RuntimeException("missing mapping tags : datasource tag");
				return dsele;
			}
		}
		return null;
	}
	
	public DriverManagerElement getDriverManagerElement() {
		Node mapping = mappingDocument.getDocumentElement();
		final String driverManager = "driver-manager";
		final String driverClass = "driver-class";
		final String connectionUrl = "connection-url";
		final String username = "user-name";
		final String password = "password";
		final String dialect = "dialect";
		
		NodeList mappings = mapping.getChildNodes();
		for (int i=0; i <mappings.getLength(); i++) {
			Node dsNode = mappings.item(i);
			if (driverManager.equals(dsNode.getNodeName())) {
				DriverManagerElement dmele = new DriverManagerElement();
				NodeList children = dsNode.getChildNodes();
				
				for (int j=0; j <children.getLength(); j++) {
					Node subele = children.item(j);
					if (driverClass.equals(subele.getNodeName())) {
						dmele.setDriverClass(subele.getTextContent());
					}
					if (connectionUrl.equals(subele.getNodeName())) {
						dmele.setConnectionUrl(subele.getTextContent());
					}
					if (username.equals(subele.getNodeName())) {
						dmele.setUserName(subele.getTextContent());
					}
					if (password.equals(subele.getNodeName())) {
						dmele.setPassword(subele.getTextContent());
					}
					if (dialect.equals(subele.getNodeName())) {
						dmele.setDialect(subele.getTextContent());
					}
				}
				validateDriverManagerElemement(dmele);
				return dmele;
			}			
		}
		return null;
	}
	
	private void validateDriverManagerElemement(DriverManagerElement ele) {
		if (ele.getConnectionUrl() == null)
			throw new RuntimeException("connection-url element should be specified as sub-element of \"driver-manager\"");
		if (ele.getUserName() == null)
			throw new RuntimeException("user-name element should be specified as sub-element of \"driver-manager\"");
		if (ele.getPassword() == null)
			throw new RuntimeException("password element should be specified as sub-element of \"driver-manager\"");
		if (ele.getDriverClass() == null)
			throw new RuntimeException("driver-class element should be specified as sub-element of \"driver-manager\"");
		if (ele.getDialect() == null)
			throw new RuntimeException("dialect element should be specified as sub-element of \"driver-manager\"");
	}
	
	public  List<ClassElement> getClassesElement(){
		List<ClassElement> celeList = new ArrayList<ClassElement>();
		final String classNodeName = "class"; 
		final String classNodeAttName = "path";
		
		Node mapping = mappingDocument.getDocumentElement();
		NodeList mappings = mapping.getChildNodes();
		for (int i=0; i <mappings.getLength(); i++) {
			Node classNode = mappings.item(i);
			if (classNodeName.equals(classNode.getNodeName())) {
				ClassElement cele = new ClassElement();
				cele.setPath(mappings.item(i).getAttributes().getNamedItem(classNodeAttName).getNodeValue());
				celeList.add(cele);
			}
		}		
		return celeList;
	}
	
	public void initialize(Document mappingDoc) {
		mappingDocument = mappingDoc;
	}
	
	public class DatasourceElement{
		private String jndi;
		private String initialContextFactory;
		private String providerUrl;
		
		public String getInitialContextFactory() {
			return initialContextFactory;
		}
		public void setInitialContextFactory(String initialContextFactory) {
			this.initialContextFactory = initialContextFactory;
		}
		public String getProviderUrl() {
			return providerUrl;
		}
		public void setProviderUrl(String providerUrl) {
			this.providerUrl = providerUrl;
		}
		public void setJndi(String jndi) {
			this.jndi = jndi;
		}
		public String getJndi() {
			return jndi;
		}
	}
	public class ClassElement{
		private String path;
		
		public void setPath(String path) {
			this.path = path;
		}
		public String getPath() {return path;}
	}
	public class DriverManagerElement{
		private String driverClass;
		private String connectionUrl;
		private String userName;
		private String password;
		private String dialect;
		
		public String getDriverClass() {
			return driverClass;
		}
		public void setDriverClass(String driverClass) {
			this.driverClass = driverClass;
		}
		public String getConnectionUrl() {
			return connectionUrl;
		}
		public void setConnectionUrl(String connectionUrl) {
			this.connectionUrl = connectionUrl;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public void setDialect(String dialect) {
			this.dialect = dialect;
		}
		public String getDialect() {
			return dialect;
		}
	}
}
