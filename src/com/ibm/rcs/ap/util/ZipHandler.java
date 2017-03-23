package com.ibm.rcs.ap.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZipHandler {
	
	private Zip zip;
	private Unzip unzip;
	
	private String reqifzFilePath;
	private String workingFolder;
	private String outputFolder;
	private String reqifFilePath;
	private String reqifzFileName;
	private String outputFileName;
	
	public ZipHandler(String reqifzFilePath) {
		this.reqifzFilePath = reqifzFilePath;
		File file = new File(reqifzFilePath);
		reqifzFileName = file.getName();
		System.out.println("Zip File Name: " +reqifzFileName);
		String separator = file.separator;
		int lastIndex = reqifzFilePath.lastIndexOf(separator);
		workingFolder = reqifzFilePath.substring(0, lastIndex);
		
	}
	
	public void unzipit(){
		unzip = new Unzip(reqifzFilePath, workingFolder);
		reqifFilePath = unzip.unZipIt();
		outputFolder = unzip.getOutputFolder();
	}
	
	public void zipit(){
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		outputFileName = workingFolder + File.separator + timeStamp + "_" + reqifzFileName;
		
		zip = new Zip(outputFileName,outputFolder);
		zip.generateFileList(new File(outputFolder));
		zip.zipIt();
	}

	public String getReqifzFilePath() {
		return reqifzFilePath;
	}

	public void setReqifzFilePath(String reqifzFilePath) {
		this.reqifzFilePath = reqifzFilePath;
	}

	public String getWorkingFolder() {
		return workingFolder;
	}

	public void setWorkingFolder(String workingFolder) {
		this.workingFolder = workingFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getReqifFilePath() {
		return reqifFilePath;
	}

	public void setReqifFilePath(String reqifFilePath) {
		this.reqifFilePath = reqifFilePath;
	}

	public String getReqifzFileName() {
		return reqifzFileName;
	}

	public void setReqifzFileName(String reqifzFileName) {
		this.reqifzFileName = reqifzFileName;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	
	
	
}
