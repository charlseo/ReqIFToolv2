package com.ibm.rcs.ap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {

	private String inputZipFile;
	private String outputFolder;
	
	private String timeStamp;
	
	public Unzip (String inputZipFile, String outputFolder) {
		this.inputZipFile = inputZipFile;
		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		this.outputFolder = outputFolder + File.separator + "temp_" + timeStamp;
		System.out.println("ReqIFz file has been unzipped in :" +outputFolder);
	}
	
	public String unZipIt(){
		
		String reqIFFile = null;
		
	     byte[] buffer = new byte[1024];

	     try{

	    	//create output directory is not exists
	    	File folder = new File(outputFolder);
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}

	    	//get the zip file content
	    	ZipInputStream zis =
	    		new ZipInputStream(new FileInputStream(inputZipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();

	    	while(ze!=null){

	    	   String fileName = ze.getName();
	           File newFile = new File(outputFolder + File.separator + fileName);
	           if (fileName.contains(".reqif")){
	        	   reqIFFile = newFile.getAbsolutePath();
	           }
	           System.out.println("file unzip : "+ newFile.getAbsoluteFile());

	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParent()).mkdirs();

	            FileOutputStream fos = new FileOutputStream(newFile);

	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	       		fos.write(buffer, 0, len);
	            }

	            fos.close();
	            ze = zis.getNextEntry();
	    	}

	        zis.closeEntry();
	    	zis.close();

	    }catch(IOException ex){
	       ex.printStackTrace();
	    }
	     return reqIFFile;
	}
	
	public String getInputZipFile() {
		return inputZipFile;
	}

	public void setInputZipFile(String inputZipFile) {
		this.inputZipFile = inputZipFile;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

}
