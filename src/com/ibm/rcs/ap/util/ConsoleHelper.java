package com.ibm.rcs.ap.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ConsoleHelper {
	
	private String filePath;
	private String fileName;
	
	public String getReqIfzFile() throws IOException {
		boolean isZipFile = false;
		
		while (!isZipFile) {
			System.out.print("-----Please specify the ReqIF file (*.reqifz) path: ");
			filePath = System.console().readLine();
			File file = new File(filePath);
	        fileName = file.getName();
	        System.out.println(fileName);
	        try {
				RandomAccessFile raf = new RandomAccessFile(file, "r");
				System.out.println("test");
				long n = raf.readInt();
				if (n == 0x504B0304){
					isZipFile = true;
				} else {
					System.out.println("It seems the file is not an archive formate. Please check and enter a correct file path: ");
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
