package com.ibm.rcs.ap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReqIFParser {
	
	private static ReqIFParser reqIFParser;
	private static Object classLock = ReqIFParser.class;
	//private String filePath;
	
	private ReqIFParser(){
		//this.filePath = filePath;
	}
	
	public static ReqIFParser getInstance() {
		
		if (reqIFParser == null) {
			synchronized(ReqIFParser.class){
				if (reqIFParser == null) {
					System.out.println("Reading *.reqif XML file...");
					reqIFParser = new ReqIFParser();
					
				}
				
			}
		}
		
		return reqIFParser;
	}

	public Document readXML(String filePath){
		Document doc = null;
		try{
			File xmlFile = new File(filePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			doc = builder.parse(xmlFile);
			
			// optional but recommended, http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			
			doc.getDocumentElement().normalize();
		
			
			//this.getReqRef(doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
}
