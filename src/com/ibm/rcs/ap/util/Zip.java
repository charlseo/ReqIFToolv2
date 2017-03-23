package com.ibm.rcs.ap.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
	private ArrayList<String> fileList = new ArrayList<String>();
	private String outputZipFile;
	private String sourceFolder;
	
	public Zip(String outputZipFile, String sourceFolder){
		this.outputZipFile = outputZipFile;
		this.sourceFolder = sourceFolder;
	}
	
	public void zipIt(){

	     byte[] buffer = new byte[1024];

	     try{

	    	FileOutputStream fos = new FileOutputStream(outputZipFile);
	    	ZipOutputStream zos = new ZipOutputStream(fos);

	    	System.out.println("Output to Zip : " + outputZipFile);

	    	for(String file : this.fileList){

	    		System.out.println("File Added : " + file);
	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze);

	        	FileInputStream in =
	                       new FileInputStream(sourceFolder + File.separator + file);

	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}

	        	in.close();
	    	}

	    	zos.closeEntry();
	    	//remember close it
	    	zos.close();

	    	System.out.println("Files are compressed successfully.");
	    }catch(IOException ex){
	       ex.printStackTrace();
	    }
	   }

	    /**
	     * Traverse a directory and get all files,
	     * and add the file into fileList
	     * @param node file or directory
	     */
	    public void generateFileList(File node){

	    	//add file only
		if(node.isFile()){
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}

		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}

	    }

	    /**
	     * Format the file path for zip
	     * @param file file path
	     * @return Formatted file path
	     */
	    private String generateZipEntry(String file){
	    	return file.substring(sourceFolder.length()+1, file.length());
	    }
}
