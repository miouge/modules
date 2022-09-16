package miouge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import miouge.beans.FileItem;

public class Unzipper {

	// function #1 to unzip
	void apacheDecompressZip( int flatUnpack, FileItem fi, String destFolder ) throws FileNotFoundException, IOException {
		
		File fromFile = new File( fi.fullpathname);
		
    	int BUFFER_SIZE = 1024;
    	
    	int fileCount = 0;
    	try( FileInputStream fileInputStream = new FileInputStream(fromFile); ZipArchiveInputStream archiveInputStream = new ZipArchiveInputStream(fileInputStream)) {
    		
    		byte[] buffer = new byte[BUFFER_SIZE];
    		
    		ArchiveEntry archiveEntry;
    		while( null != (archiveEntry = archiveInputStream.getNextEntry())) {
    			
    			String entryName = archiveEntry.getName();
    			   			    			
                if( archiveEntry.isDirectory()) {
                	
                	if( flatUnpack > 0 ) {
                		continue;
                	}
                	else {
	                	// directory
	                	File destFile = new File( destFolder, entryName );
	                	String destFilePath = destFile.getCanonicalPath();
	                	Files.createDirectories(Paths.get( destFilePath ));
                	}
                }
                else {
                	
                	if( flatUnpack > 0 ) {

	        			// remove any subfolders, only take the last part of the path (the filename and extension)
	                	// so flat unzip all the file to the same destination folder 
	        			String splitAround = "";
	        			if( entryName.contains("/") ) {
	        				splitAround = "/";
	        			}
	        			else if ( entryName.contains("\\")) {
	        				splitAround = "\\";
	        			}
	        			else {
	        				
	        			}
	        			if( splitAround != "" ) {
	        				
	        				String parts[] = entryName.split( splitAround );
	        				if( parts.length > 1 ) {
	        					
	        					entryName = parts[ parts.length -1 ]; 
	        				}
	        			}
                	}
        			
        			File destFile = new File( destFolder, entryName );
                
        			try( FileOutputStream fileOutputStream = new FileOutputStream(destFile) ) {
        				
        				int length = -1;
        				while ((length = archiveInputStream.read(buffer)) != -1) {
        					fileOutputStream.write(buffer, 0, length);
        				}
        				fileOutputStream.flush();
        			}
    	            fileCount++;
                }
    		}
    	}
    	System.out.format( " %d files saved\n", fileCount );
    }	
	
	// function #2 to unzip
    void javaUtilUnzipFile( int flatUnpack, FileItem fi, String destFolder ) throws IOException {
	       
    	System.out.format( "unzip content of %s ...", fi.name );
    	
        byte[] buffer = new byte[1024];
        
        ZipInputStream zis = new ZipInputStream(new FileInputStream( fi.fullpathname ));
        ZipEntry zipEntry = zis.getNextEntry();
        
        int fileCount = 0;
        while( zipEntry != null ) {
        	
        	File destFile = new File( destFolder, zipEntry.getName());
            
            if( zipEntry.isDirectory()) {
            	
            	// directory
            	String destFilePath = destFile.getCanonicalPath();
            	Files.createDirectories(Paths.get( destFilePath ));
            }
            else {
            
	            FileOutputStream fos = new FileOutputStream( destFile );
	            
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	                fos.write(buffer, 0, len);
	            }
	            fos.close();
	            fileCount++;
            }
            
            zipEntry = zis.getNextEntry();
        }
        
        zis.closeEntry();
        zis.close();
 
        System.out.format( " %d files saved\n", fileCount );
    }	
    
    void unpackZIP( int flatUnpack, FileItem fi, String destFolder ) throws Exception {

    	boolean success = false;
    	
    	// try function #1
    	try {

    		apacheDecompressZip( flatUnpack, fi, destFolder );
    		success = true;
    	}
    	catch( Exception e ) {
    	}

    	if( success ) {
    		return;
    	}
    	
    	// else if an error occur try function #2
    	try {

    		javaUtilUnzipFile( flatUnpack, fi, destFolder );
    		success = true;
    	}
    	catch( Exception e ) {
    	}
    	
    	if( success != true ) {
    		throw new Exception( "fail to unzip" );
    	}
    }
}
