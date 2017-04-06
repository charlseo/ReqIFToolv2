package com.ibm.rcs.ap.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
 * AddXMLNode inserts module artifact link mapping as XML element into the existing reqif file
 * @author Charlie Seo
 * @version 2.0
 * 
 */
public class AddXMLNode {
	
	private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder documentBuilder;
	private String reqIfFilePath;
	private String linkMaps;
	private Document doc;
	private Node specRelationsNode;
	
	public AddXMLNode(String reqIfFilePath) throws Exception{
		this.reqIfFilePath = reqIfFilePath;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		doc = documentBuilder.parse(reqIfFilePath);
		doc.getDocumentElement().normalize();
		specRelationsNode = doc.getElementsByTagName("SPEC-RELATIONS").item(0);
		
	}
	
	public void addNameSpace() throws Exception, IOException {
		
		File xmlFile = new File(reqIfFilePath);
		BufferedReader br = new BufferedReader(new FileReader(xmlFile));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine())!= null)
		{
			if(line.indexOf("xmlns:doors") != -1){
				
			} else {
				if(line.indexOf("<REQ-IF ") != -1)
			    {
			        line = line.replaceAll("<REQ-IF ","<REQ-IF xmlns:doors=\"http://www.ibm.com/rdm/doors/REQIF/xmlns/1.0\" ");
			    }  
			}
		    sb.append(line); 
		}
		br.close();

		//BufferedWriter bw = new BufferedWriter(new FileWriter(xmlFile));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xmlFile),"UTF-8"));
		bw.write(sb.toString());
		bw.close();
  	    
  	    System.out.println("--- NameSpace added");
	}
	
	/*
	 * @param linkMaps getting link mapping as String
	 * @deprecated
	 */
	public void addLinkMaps(String linkMaps) throws Exception, IOException{
		
		this.linkMaps = linkMaps;
		// doc = documentBuilder.parse(reqIfFilePath);
        
//        doc.getDocumentElement().normalize();
//		Node specRelationsNode = doc.getElementsByTagName("SPEC-RELATIONS").item(0);
		
		appendXmlFragment(documentBuilder, specRelationsNode, linkMaps);
	}
	/*
	 * Insert link mapping after converting String to XML node element. 
	 * @param linkMaps receive link mapping as arrayList 
	 * @throws TransformerException throws an exception in case malformed node added
	 */
	public void addLinkMaps(ArrayList<String> linkMaps) throws IOException, SAXException, TransformerException{
		for (int i = 0; i < linkMaps.size(); i++) {
			
	  	    String fragment = linkMaps.get(i);
	  	    System.out.println("Module link adding - " + fragment);
			Document doc = specRelationsNode.getOwnerDocument();
			InputStream is = new ByteArrayInputStream(fragment.getBytes("UTF-8"));

	  	    Node fragmentNode = documentBuilder.parse(
	  	        new InputSource(is))
	  	        .getDocumentElement();
	  	    
	  	    fragmentNode = doc.importNode(fragmentNode, true);
	  	    specRelationsNode.appendChild(fragmentNode);
	  	    
		}
		DOMSource source = new DOMSource(doc);
  	    
  	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
  	    Transformer transformer = transformerFactory.newTransformer();
  	    StreamResult result = new StreamResult(reqIfFilePath);
  	    transformer.transform(source, result);
	}
	
	public void addReqIfDef(String reqIfDefinition) throws Exception, IOException{
		
		Document doc = documentBuilder.parse(reqIfFilePath);
        
        doc.getDocumentElement().normalize();
		Node specRelationsNode = doc.getElementsByTagName("REQ-IF-TOOL-EXTENSION").item(0);
		
		appendXmlFragment(documentBuilder, specRelationsNode, reqIfDefinition);
	}
	
	public void addModDef(String modDefinition) throws Exception, IOException{
		
		Document doc = documentBuilder.parse(reqIfFilePath);
        
        doc.getDocumentElement().normalize();
		Node specRelationsNode = doc.getElementsByTagName("doors:DOORS-RIF-DEFINITION").item(0);
		
		appendXmlFragment(documentBuilder, specRelationsNode, modDefinition);
	}

	/*
	 * @deprecated
	 */
	private void appendXmlFragment(
  	      DocumentBuilder docBuilder, Node parent,
  	      String fragment) throws IOException, SAXException, Exception {
  	    
		Document doc = parent.getOwnerDocument();
		InputStream is = new ByteArrayInputStream(fragment.getBytes("UTF-8"));
//		InputStream is = new ByteArrayInputStream(fragment.getBytes());
//		Reader reader = new InputStreamReader(is,"UTF-8");
  	    Node fragmentNode = docBuilder.parse(
  	        new InputSource(is))
  	        .getDocumentElement();
  	    
  	    fragmentNode = doc.importNode(fragmentNode, true);
  	    parent.appendChild(fragmentNode);
  	    DOMSource source = new DOMSource(doc);
  	    
  	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
  	    Transformer transformer = transformerFactory.newTransformer();
  	    StreamResult result = new StreamResult(reqIfFilePath);
  	    transformer.transform(source, result);
  	    //System.out.println("XML updated in " + reqIfFilePath);
  }
	/*
	 * @deprecated
	 */
	
	private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
}
