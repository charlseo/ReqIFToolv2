package com.ibm.rcs.ap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.ibm.rcs.ap.beans.ReqExtension;
import com.ibm.rcs.ap.beans.ReqRelation;
import com.ibm.rcs.ap.beans.Requirement;
import com.ibm.rcs.ap.factory.LinkConstructor;
import com.ibm.rcs.ap.factory.ReqFactorySax;
import com.ibm.rcs.ap.util.AddXMLNode;
import com.ibm.rcs.ap.util.ConsoleHelper;
import com.ibm.rcs.ap.util.Test;
import com.ibm.rcs.ap.util.ZipHandler;

public class ReqIFToolSax {

	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub

		// Provide console interface to get ReqIFz as an input.
		ConsoleHelper console = new ConsoleHelper();		
		String filePath = console.getReqIfzFile();	
		File filePathCheck = new File(filePath);
		filePath = filePathCheck.getAbsolutePath();
		
		/*******************************************/
//		Test test = new Test();
//		String filePath = test.getReqIFzFile();	
//		
		/*******************************************/
		
		// Handling uncompression and compression by ZipHandler
		ZipHandler zipHandler = new ZipHandler(filePath);		
		zipHandler.unzipit();

		// Return reqif file
		String reqIFFile = zipHandler.getReqifFilePath();
		//String reqIFFile = test.getReqIFFile();
		ReqFactorySax reqFacSax = new ReqFactorySax(reqIFFile);
		
		System.out.println("\nReading the reqif file now..... \n\n");

		//
		reqFacSax.getArtifacts();

		ArrayList<Requirement> requirements = reqFacSax.getReqCollection();
		ArrayList<ReqExtension> reqExtensions = reqFacSax.getReqExtensions();
		ArrayList<ReqRelation> reqRelations = reqFacSax.getReqRelations();
		
		System.out.println("\n" + requirements.size() + " number of requirements found..... \n\n");
		
//		for (int i = 0; i < requirements.size(); i++) {
//
//			System.out.println("Requirement Mod ref: " + requirements.get(i).getRef());
//			System.out.println("Requirement ID: " + requirements.get(i).getReqID());
//		}

		//System.out.println(reqExtensions.size());
//		for (int j = 0; j < reqExtensions.size(); j++) {
//
//			System.out.println("Map- Core Ref: " + reqExtensions.get(j).getCoreArtifactRef());
//			System.out.println("Map - Mod Ref: " + reqExtensions.get(j).getModArtifactRef());
//		}

		System.out.println(reqRelations.size() + " number of links found..... \n\n\tConstructing module artifact links now, this might take a few minutes....");
//		for (int j = 0; j < reqRelations.size(); j++) {
//
//			System.out.println("M:Source Ref: " + reqRelations.get(j).getSourceCoreReqID());
//			System.out.println("M:Target Ref: " + reqRelations.get(j).getTargetCoreReqID());
//		}

		
		LinkConstructor linkConstructor = new LinkConstructor();
		linkConstructor.addModArtifactRef(reqExtensions, reqRelations);

		reqRelations = linkConstructor.getReqRelations();


		// Building link mapping for Artifact Module linking 
		// Core artifact Ref mapping is used to create artifact module mapping with module artifact refs
		ReqIFModuleLinkBuilder linkBuilder = new ReqIFModuleLinkBuilder(reqRelations, reqIFFile);
		
		System.out.println("\nCreating module artifact links now..... \n\n");
		// Create XML link mapping and append into the existing reqif file. 
		linkBuilder.createXMLMaps();
		AddXMLNode addXmlNode = linkBuilder.getAddXmlNode();

		zipHandler.zipit();
		String finalZipFile = zipHandler.getOutputFileName();
		System.out.println("It successfully converted the RDNG ReqIF file to be compatible with DOORS. The final deliverable (ReqIFz) is " + finalZipFile);

	}

}
