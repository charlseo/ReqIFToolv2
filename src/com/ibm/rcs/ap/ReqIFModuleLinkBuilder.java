package com.ibm.rcs.ap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.ibm.rcs.ap.beans.ReqRelation;
import com.ibm.rcs.ap.beans.Requirement;
import com.ibm.rcs.ap.util.AddXMLNode;

public class ReqIFModuleLinkBuilder {
	
	private String reqIFFile;
	private ArrayList<Requirement> reqs = new ArrayList<Requirement>();
	private ArrayList<ReqRelation> relations = new ArrayList<ReqRelation>();
	private AddXMLNode addXmlNode;
	
	public ReqIFModuleLinkBuilder(ArrayList<ReqRelation> relations, String reqIFFile){
		
		this.relations = relations;
		this.reqIFFile = reqIFFile;
	}
	
	private void getReqLinkMappings() {
		
		for (int i=0; i < relations.size(); i++) {
			
			for (int j=0; j < reqs.size(); j++){
				String reqRef = reqs.get(j).getRef();
				String reqCoreRef = reqs.get(j).getCoreRef();
				if (reqCoreRef != null) {
					if (relations.get(i).getTargetCoreReqID().equals(reqCoreRef)){
						relations.get(i).setTargetReqID(reqRef);
					}
				}
				
			}
				
			
		}
	}
	
	public void createXMLMaps() throws IOException, Exception{
		//getReqLinkMappings();
		System.out.println("Creating artifact module link maps now.............");
		addXmlNode = new AddXMLNode(reqIFFile);
		ArrayList<String> linkMaps = new ArrayList<String>();
		String linkMapping = "";
		
		for (int i=0; i < relations.size(); i++) {
			System.out.println("Artifact-Source ID: " +relations.get(i).getSourceReqID());
			System.out.println("Artifact-Target ID: " + relations.get(i).getTargetReqID());
			
			
			StringBuilder sbOutput  = new StringBuilder();
			if (relations.get(i).getSourceReqID() != null && relations.get(i).getTargetReqID() != null) {
				
				sbOutput.append("<SPEC-RELATION LAST-CHANGE=\"" + relations.get(i).getLastChange() + "\" IDENTIFIER=\"" +
						relations.get(i).getIdentifier() + "\">");
				sbOutput.append("<SOURCE>" + "<SPEC-OBJECT-REF>" + relations.get(i).getSourceReqID() + "</SPEC-OBJECT-REF>" +
						"</SOURCE>");
				sbOutput.append("<TARGET>" + "<SPEC-OBJECT-REF>" + relations.get(i).getTargetReqID() + "</SPEC-OBJECT-REF>" +
						"</TARGET>");
				sbOutput.append("<TYPE>" + "<SPEC-RELATION-TYPE-REF>" + relations.get(i).getType() + "</SPEC-RELATION-TYPE-REF>" +
						"</TYPE>");
				sbOutput.append("</SPEC-RELATION>");
				linkMapping = sbOutput.toString();
				linkMaps.add(linkMapping);
				System.out.println(linkMapping);
				//addXmlNode.addLinkMaps(linkMapping);
				
			}
			
			
			
			//Thread.sleep(5);
			
		}
		
		addXmlNode.addLinkMaps(linkMaps);	

		
	}
	
	// depreciated
	private void createMappingFile(String linkMapping) {
		
		FileOutputStream fos = null;
		
		File file;
		
		try {
			file = new File("linkmapping.txt");
			fos = new FileOutputStream(file);
			
			if (!file.exists()) {
				file.createNewFile();
			}
			
			byte[] contentInBytes = linkMapping.getBytes();
			
			fos.write(contentInBytes);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	public AddXMLNode getAddXmlNode() {
		return addXmlNode;
	}

	public void setAddXmlNode(AddXMLNode addXmlNode) {
		this.addXmlNode = addXmlNode;
	}
}
