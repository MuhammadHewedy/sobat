package com.googlecode.sobat.configuration;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.sobat.util.ReflectionUtil;

public class ObjectXmlWrapper {
	
	private Document documentObjct;
	
	public ObjectXmlWrapper(Document documentObjct) {
		this.documentObjct = documentObjct;
	}

	public ClassElement getClassElement() {
		ClassElement cele = new ClassElement();
		Node root = documentObjct.getDocumentElement();
		final String nameAttr = "name";
		final String tableAttr = "table";
		final String dynamicUpdateAttr = "dynamic-update";
		
		cele.setName(root.getAttributes().getNamedItem(nameAttr).getNodeValue());
		cele.setTable(root.getAttributes().getNamedItem(tableAttr).getNodeValue());
		
		Node dynaUpdateNode = root.getAttributes().getNamedItem(dynamicUpdateAttr);
		cele.setDynamicUpdate(XmlDefaults.ClassElement_DynamicUpdate);
		if (dynaUpdateNode != null)
			cele.setDynamicUpdate(Boolean.parseBoolean(dynaUpdateNode.getNodeValue()));

		return cele;
	}
	
	public IdElement getIdElement(Object obj) {
		IdElement idele = new IdElement();
		Node root = documentObjct.getDocumentElement();
		NodeList propertyList = root.getChildNodes();
		
		final String idElem = "id";
		final String propertyElemName = "name";
		final String propertyElemColumn = "column";
		final String propertyElemType = "type";
		
		for (int i =0; i < propertyList.getLength(); i++) {
			Node idNode = propertyList.item(i); 
			if (idNode.getNodeName().equals(idElem)) {
				String propertyName = idNode.getAttributes().getNamedItem(propertyElemName).getNodeValue();
				idele.setName(propertyName);
				idele.setColumn(idNode.getAttributes().getNamedItem(propertyElemColumn).getNodeValue());
				String type = idNode.getAttributes().getNamedItem(propertyElemType).getNodeValue();
				idele.setType(type);
				try {
					idele.setValue(ReflectionUtil.getGetterValue(propertyName, obj));
				} catch (Exception ex) {
				}
				return idele;
			}
		}
		throw new RuntimeException("No Id tag found");
	}
	
	public List<PropertyElement> getPropertyElements(Object obj){
		
		List<PropertyElement> propeleList = new ArrayList<PropertyElement>();
		Node root = documentObjct.getDocumentElement();
		NodeList propertyList = root.getChildNodes();
		
		final String propertyElem = "property";
		final String propertyElemName = "name";
		final String propertyElemColumn = "column";
		final String propertyElemType = "type";
		
		for (int i =0; i < propertyList.getLength(); i++) {
			Node propNode = propertyList.item(i); 
			if (propNode.getNodeName().equals(propertyElem)) {
				PropertyElement propele = new PropertyElement();
				String propertyName = propNode.getAttributes().getNamedItem(propertyElemName).getNodeValue();
				propele.setName(propertyName);
				propele.setColumn(propNode.getAttributes().getNamedItem(propertyElemColumn).getNodeValue());
				String type = propNode.getAttributes().getNamedItem(propertyElemType).getNodeValue();
				propele.setType(type);
				try {
					propele.setValue(ReflectionUtil.getGetterValue(propertyName, obj));
				} catch (Exception ex) {
					throw new RuntimeException (ex.getMessage(), ex);
				}
				propeleList.add(propele);
			}
		}
		
		return propeleList;
	}
	
	public class IdElement extends PropertyElement{
	}
	
	public class ClassElement{
		private String name;
		private String table;
		private boolean dynamicUpdate;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTable() {
			return table;
		}
		public void setTable(String table) {
			this.table = table;
		}
		public void setDynamicUpdate(boolean dynamicUpdate) {
			this.dynamicUpdate = dynamicUpdate;
		}
		public boolean isDynamicUpdate() {
			return dynamicUpdate;
		}
	}
	
	public class PropertyElement{
		private String name;
		private String type;
		private Object value;
		private String column;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public void setColumn(String column) {
			this.column = column;
		}
		public String getColumn() {
			return column;
		}
	}
}
