package com.ibm.rcs.ap.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.ibm.rcs.ap.beans.*;

public class ReqFactorySax {

	private String reqIfFilePath;

	private final String TAG_ARTIFACT_EXTENSION = "rm:SPEC-OBJECT-EXTENSION";
	private final String TAG_MODARTIFACT = "SPEC-OBJECT";
	private final String TAG_REQID = "ATTRIBUTE-VALUE-INTEGER";
	private final String TAG_LINKS = "SPEC-RELATION";
	private final String TAG_LINK_SOURCE = "SOURCE";
	private final String TAG_LINK_TARGET = "TARGET";	
	private final String TAG_COREARTIFACTREF = "rm:CORE-SPEC-OBJECT-REF";
	private final String TAG_MODARTIFACTREF = "SPEC-OBJECT-REF";
	private final String TAG_OLEARTIFACTREF = "rm:WRAPPED-RESOURCE-REF";

	private ArrayList<Requirement> reqCollection = new ArrayList<Requirement>();
	private ArrayList<ReqExtension> reqExtensions = new ArrayList<ReqExtension>();
	private ArrayList<ReqRelation> reqRelations = new ArrayList<ReqRelation>();

	private Requirement req = null;
	private ReqExtension reqExtension = null;
	private ReqRelation reqRelation = null;

	public ReqFactorySax(String reqIfFilePath) {
		this.reqIfFilePath = reqIfFilePath;
	}


	public void getArtifacts() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		DefaultHandler handler = new DefaultHandler() {

			private	boolean isArtifactExtension = false;
			private	boolean isModArtifact = false;
			private	boolean isReqId = false;
			private	boolean isLink = false;
			private	boolean isSource = false;
			private	boolean isTarget = false;
			private	boolean isModArtifactRef = false;
			private	boolean isCoreArtifactRef = false;
			private	boolean isOLEArtifactRef = false;
			private String reqId = null;

			public void startElement(String uri, String localName, String qName, Attributes attributes){
				
				//System.out.println("Start Element :" + qName);

				if (qName.equalsIgnoreCase(TAG_MODARTIFACT)) {
					isModArtifact = true;
					req = new Requirement();
					req.setRef(attributes.getValue("IDENTIFIER"));
					System.out.println("Module Artifact ID: " + attributes.getValue("IDENTIFIER"));
				}

				if (qName.equalsIgnoreCase(TAG_REQID)) {
					isReqId = true;
					reqId = attributes.getValue("THE-VALUE");
					req.setReqID(reqId);
					System.out.println("DNG Requirement ID: " + reqId);

				}

				if (qName.equalsIgnoreCase(TAG_ARTIFACT_EXTENSION)) {
					isArtifactExtension = true;
					reqExtension = new ReqExtension();

				}

				if (qName.equalsIgnoreCase(TAG_LINKS)) {
					isLink = true;
					reqRelation = new ReqRelation();
					String lastChange = attributes.getValue("LAST-CHANGE");
					String identifier = attributes.getValue("IDENTIFIER");
					reqRelation.setIdentifier(identifier);
					reqRelation.setLastChange(lastChange);
				}

				if (qName.equalsIgnoreCase(TAG_MODARTIFACTREF)) {
					isModArtifactRef = true;
				}

				if (qName.equalsIgnoreCase(TAG_OLEARTIFACTREF)) {
					isOLEArtifactRef = true;
				}

				if (qName.equalsIgnoreCase(TAG_COREARTIFACTREF)) {
					isCoreArtifactRef = true;
				}

				if (qName.equalsIgnoreCase(TAG_LINK_SOURCE)) {
					isSource = true;
				}


				if (qName.equalsIgnoreCase(TAG_LINK_TARGET)) {
					isTarget = true;
				}



			}

			public void endElement(String uri, String localName,
					String qName) throws SAXException {

				//System.out.println("End Element :" + qName);

				if (qName.equalsIgnoreCase(TAG_MODARTIFACTREF)) {
					isModArtifactRef = false;
				}

				if (qName.equalsIgnoreCase(TAG_ARTIFACT_EXTENSION)) {
					isArtifactExtension = false;
					reqExtensions.add(reqExtension);
				}

				if (qName.equalsIgnoreCase(TAG_REQID)) {
					reqCollection.add(req);
				}

				if (qName.equalsIgnoreCase(TAG_LINKS)) {
					isLink = false;
					reqRelations.add(reqRelation);
				}

				if (qName.equalsIgnoreCase(TAG_LINK_SOURCE)) {
					isSource = false;

				}

				if (qName.equalsIgnoreCase(TAG_LINK_TARGET)) {
					isTarget = false;

				}




			}

			public void characters(char ch[], int start, int length) throws SAXException {



				if (isArtifactExtension && isCoreArtifactRef) {
					String coreArtifactRef = new String(ch, start, length);
					System.out.println("Core Artifact Reference: " +
							coreArtifactRef);
					//				isArtifactExtension = false;
					isCoreArtifactRef = false;
					reqExtension.setCoreArtifactRef(coreArtifactRef);
				} 

				if (isArtifactExtension && isModArtifactRef) {
					String modArtifactRef = new String(ch, start, length);
					System.out.println("Module Artifact Reference: " +
							modArtifactRef);
					reqExtension.setModArtifactRef(modArtifactRef);
					//				isArtifactExtension = false;
					isModArtifactRef = false;

				} 

				if (isArtifactExtension && isOLEArtifactRef) {
					String oleArtifactRef = new String(ch, start, length);
					System.out.println("OLE Artifact Reference: " +
							oleArtifactRef);
					reqExtension.setCoreArtifactRef(oleArtifactRef);
					//				isArtifactExtension = false;
					isOLEArtifactRef = false;
				}

				if (isLink) { if (isSource && isModArtifact) {

					String sourceRef =  new String(ch, start, length);

					if (sourceRef.length() > 20) {
						System.out.println("Link Source Reference(Core): " +
								sourceRef + " ");
						reqRelation.setSourceCoreReqID(sourceRef);
					}

				}}


				if (isLink) { if(isTarget && isModArtifact) {

					String targetRef =  new String(ch, start, length);
					if (targetRef.length() > 20) {
						System.out.println("Link Target Reference(Core): " +
								targetRef);
						reqRelation.setTargetCoreReqID(targetRef);
					}

				}}

			}

		};

		try {
			saxParser.parse(reqIfFilePath, handler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public ArrayList<Requirement> getReqCollection() {
		return reqCollection;
	}


	public void setReqCollection(ArrayList<Requirement> reqCollection) {
		this.reqCollection = reqCollection;
	}


	public ArrayList<ReqExtension> getReqExtensions() {
		return reqExtensions;
	}


	public void setReqExtensions(ArrayList<ReqExtension> reqExtensions) {
		this.reqExtensions = reqExtensions;
	}


	public ArrayList<ReqRelation> getReqRelations() {
		return reqRelations;
	}


	public void setReqRelations(ArrayList<ReqRelation> reqRelations) {
		this.reqRelations = reqRelations;
	}
}
