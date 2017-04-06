package com.ibm.rcs.ap.factory;

import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.rcs.ap.beans.ReqModule;
import com.ibm.rcs.ap.beans.ReqRelation;
import com.ibm.rcs.ap.util.AddXMLNode;

/*
 * Not used but this class is to convert a ReqIF to be more DOORS compatible by adding DOORS ReqIF definition 
 * and DOORS ReqIF token
 * @author Charlie Seo
 * @version 2.0
 * @since 23/03/2017
 */
public class ReqModuleFactory {

	private String moduleTypeRef;
	private static final String MODULE_TYPE_NS = "http://jazz.net/ns/rm#Module";
	private Document doc;
	private ArrayList<ReqModule> modules = new ArrayList<ReqModule>();
	private String reqIfToken;
	private AddXMLNode addXmlNode;
	
	public ReqModuleFactory(Document doc, AddXMLNode addXmlNode) {
		this.doc = doc;
		this.addXmlNode = addXmlNode;
		doc.getDocumentElement().normalize();
	}
	
	public void updateDOORSReqIFDefinition() throws IOException, Exception{
		collectReqIfToken(); // req-if-common is added for rount trip
		getModuleTypeRef();  // get module type ref ID to retrieve module Refs
		collectModuleRefs();
		createDOORSReqIFDefinition(); // Append DOORS REQ-IF DEFINITION to enable round trip including import & merge
	}
	
	private void collectReqIfToken(){
		NodeList nlist = doc.getElementsByTagName("reqif-common:EXCHANGE-CONVERSATION");
		for (int i = 0; i < nlist.getLength(); i++) {
			Node nNode = nlist.item(i);
			//System.out.println("\nCurrent element: " + nNode.getNodeName());
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Element e = (Element) nNode;
				reqIfToken = e.getElementsByTagName("reqif-common:IDENTIFIER").item(0).getTextContent();
			}
		}
		
	}
	private void createDOORSReqIFDefinition() throws IOException, Exception{
		StringBuffer sbOutput = new StringBuffer();
		sbOutput.append("<doors:DOORS-RIF-DEFINITION><doors:IDENTIFIER>");
		sbOutput.append(reqIfToken);
		sbOutput.append("</doors:IDENTIFIER></doors:DOORS-RIF-DEFINITION>");
		String xmlOutput = sbOutput.toString();
		addXmlNode.addReqIfDef(xmlOutput);
		for (int i = 0; i < modules.size(); i++ ){
			StringBuffer sbOutput2 = new StringBuffer();
			
			if (modules.get(i).getModRef() != null) {
				
				sbOutput2.append("<doors:MODULE-DEFINITION><doors:DDC-MODE>DDC_FULL_MODULE</doors:DDC-MODE><SPECIFICATION-REF>");
				sbOutput2.append(modules.get(i).getModRef());
				sbOutput2.append("</SPECIFICATION-REF></doors:MODULE-DEFINITION>");
			}
			
			
			String moduleDefinition = sbOutput2.toString();
			//String moduleDefUTF8 = new String(moduleDefinition, UTF_8);
			addXmlNode.addModDef(moduleDefinition);
		}
		
		
	}
	
	
	
	private void collectModuleRefs() {
		NodeList nlist = doc.getElementsByTagName("SPECIFICATION");
		for (int i = 0; i < nlist.getLength(); i++) {
			Node nNode = nlist.item(i);
			//System.out.println("\nCurrent element: " + nNode.getNodeName());
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Element e = (Element) nNode;
				Element valueElement = (Element) e.getElementsByTagName("VALUES").item(0);
				
				NodeList attrList = valueElement.getElementsByTagName("ATTRIBUTE-VALUE-ENUMERATION");
				
				for (int j=0; j < attrList.getLength(); j++){
					Node attrNode = attrList.item(j);
					Element attrElement = (Element) attrNode;
					Element enumValueElement = (Element) attrElement.getElementsByTagName("VALUES").item(0);
					Node enumValueRefNode = enumValueElement.getElementsByTagName("ENUM-VALUE-REF").item(0);
					
					String moduleRefValue = enumValueRefNode.getTextContent();
					
					if (moduleRefValue.equals(moduleTypeRef)){
						ReqModule mod = new ReqModule();
						String moduleRefID = e.getAttribute("IDENTIFIER");
						mod.setModRef(moduleRefID);
						modules.add(mod);
					}
				}
			}
		}
		
		for (int j = 0; j < modules.size(); j++) {
			System.out.println("Module_" + j + ": " + modules.get(j).getModRef());
		}
	}
	
	public void getModuleTypeRef(){

		NodeList nlist = doc.getElementsByTagName("rm-reqif:DATATYPE-DEFINITION-EXTENSION");
		
		for (int i = 0; i < nlist.getLength(); i++) {
			
			Node nNode = nlist.item(i);
			//System.out.println("\nCurrent element: " + nNode.getNodeName());
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				Element e = (Element) nNode;
				
				String moduleTypeNS = e.getElementsByTagName("rm-reqif:SAME-AS").item(0).getTextContent();
				
				if (moduleTypeNS.equals(MODULE_TYPE_NS)) {
					moduleTypeRef = e.getElementsByTagName("DATATYPE-DEFINITION-ENUMERATION-REF").item(0).getTextContent();
				}
				
				
			}
		}
		
	}

	public ArrayList<ReqModule> getModules() {
		return modules;
	}

	public void setModules(ArrayList<ReqModule> modules) {
		this.modules = modules;
	}

	public void setModuleTypeRef(String moduleTypeRef) {
		this.moduleTypeRef = moduleTypeRef;
	}

	public String getReqIfToken() {
		return reqIfToken;
	}

	public void setReqIfToken(String reqIfToken) {
		this.reqIfToken = reqIfToken;
	}
	
	
}
