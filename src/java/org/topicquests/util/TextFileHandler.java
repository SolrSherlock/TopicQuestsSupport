/*
 * Copyright 2013, TopicQuests
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.topicquests.util;

import  java.io.File;
import  java.io.FileInputStream;
import  java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import  java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.Reader;

import  java.io.BufferedReader;
import  java.io.IOException;
import java.io.FileNotFoundException;
import  javax.swing.JFileChooser;
import java.util.zip.*;

/**
 * TextFileHandler.java
 *  General purpose Text File handler
 *  @author Jack Park
 */
/**
 * FIXME: Errors should throw new RuntimeException
 */
public class TextFileHandler {
  private String fName = null;
  private String body = null;
  private BufferedReader inStream = null;
  private JFileChooser chooser = null;

	public TextFileHandler() {
	}
  //////////////////////////////////////
  // Directory services
  // To use:
  //      First save:
  //      // caller gets a file e.g. to set a document name
  //      File newFile = handler._saveAs();
  //      // callser uses that file
  //      if (newFile != null)
  //        handler.writeFile(newFile, bodyString);
  //////////////////////////////////////
  public File _saveAs() {
    File result = null;
    if (chooser==null)chooser = new JFileChooser(new File("."));
    int retVal = chooser.showSaveDialog(null);
    if(retVal == JFileChooser.APPROVE_OPTION) {
      result = chooser.getSelectedFile();
    }
    return result;
  }

  public void saveAs(String body) {
    File myFile = _saveAs();
    if (myFile != null) {
        writeFile(myFile, body);
    }
  }

  public File openFile() {
    return openFile(null);
  }

  public File openFile(String title) {
    File result = null;
    JFileChooser chooser = new JFileChooser(new File("."));
    if (title != null)
      chooser.setDialogTitle(title);
    int retVal = chooser.showOpenDialog(null);
    if(retVal == JFileChooser.APPROVE_OPTION) {
      result = chooser.getSelectedFile();
    }
    return result;
  }

  public File [] openFiles(String title) {
	  File [] result = null;
	    JFileChooser chooser = new JFileChooser(new File("."));
	    if (title != null)
	      chooser.setDialogTitle(title);
	    chooser.setMultiSelectionEnabled(true);
	    int retVal = chooser.showOpenDialog(null);
	    if(retVal == JFileChooser.APPROVE_OPTION) {
	      result = chooser.getSelectedFiles();
	    }
	    return result;
  }
  
  public File openDirectory() {
    return openDirectory(null);
  }

  public File openDirectory(String title) {
    File result = null;
    JFileChooser chooser = new JFileChooser(new File("."));
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (title != null)
      chooser.setDialogTitle(title);
    int retVal = chooser.showOpenDialog(null);
    if(retVal == JFileChooser.APPROVE_OPTION) {
      result = chooser.getSelectedFile();
    }
    return result;
  }
  //////////////////////////////////////
  //  Simple File handlers
  /////////////////////////////////////
  public String readFile(String fileName) {  // fully qualified name
     File f = new File(fileName);
     fName = fileName;
     return readFile(f);
  }
  public String readFile(File f) {
     int size = (int) f.length();
     int bytesRead = 0 ;
     body = null;
     try {
       FileInputStream in = new FileInputStream(f) ;

       byte[] data = new byte[size] ;
       in.read(data, 0, size);
       body = new String(data) ;
       in.close() ;
     } catch (IOException e) {
         System.out.println("Error: TextFileHandler couldn't read from " + f + "\n") ;
     }
     return body;
  }
  public String readFile16(File f) throws IOException {
	  StringBuilder sb = new StringBuilder();
	  String line;
	  Reader in = null;
	  try {
	     in = new InputStreamReader(new FileInputStream(f), "UTF-16");
	     BufferedReader reader = new BufferedReader(in);
	     while ((line = reader.readLine()) != null) {
	           sb.append(line).append("\n");
	     }
	  } finally {
	     in.close();
	  }
	  return sb.toString();
  }
  
  public void writeFile(String fileName, String inBody) {
     File f = new File(fileName) ;
     fName = fileName;
     writeFile(f, inBody);
  }

  public void writeFile(File f, String inBody) {
//  System.out.println("WRITING "+f);
     int size = (int) inBody.length();
     int bytesOut = 0 ;
     byte data[] = inBody.getBytes(); //new byte[size] ;
  //   data = body.getBytes();
     try {
       FileOutputStream out = new FileOutputStream(f) ;
       out.write(data, 0, size);
       out.flush() ;
       out.close() ;
     }
     catch (IOException e) {
        System.out.println("Error: TextFileHandler couldn't write to " + fName + "\n");
     }
    }

    //////////////////////////////////////
    //  Line-oriented File readers
    /////////////////////////////////////
    public String readFirstLine(String fileName) {
      File f = new File(fileName);
      return readFirstLine(f);
    }
    public String readFirstLine(File f) {
    fName = f.getName();
      try {
       FileInputStream in = new FileInputStream(f);
       inStream = new BufferedReader(new InputStreamReader(in));
     } catch (IOException e) {
         System.out.println("Error: TextFileHandler couldn't open a DataInputStream on " + fName + "\n");
     }
     return readNextLine();
    }
    /**
     *  Read a line from an open file
     *  Return null when done
     */
    public String readNextLine() {
      String str = null;
      try {
         str = inStream.readLine();
      } catch (IOException e) {
         System.out.println("Error: TextFileHandler couldn't read from " + fName + "\n");
      }
      return str;
    }

    ////////////////////////////////////////////
    // Serialized Java Class utilities
    ////////////////////////////////////////////

    public void persist(String fileName, Object obj) {
      try {
          new ObjectOutputStream(
                 new FileOutputStream(new File(fileName))).writeObject(obj);
      } catch (Exception e) {
//          e.printStackTrace();
          throw new RuntimeException(e);
      }
    }

    public Object restore(String fileName) {
      Object result = null;
      try {
        result = new ObjectInputStream(
            new FileInputStream(new File(fileName))).readObject();
      }
      catch (Exception e) {
//        e.printStackTrace();
        System.out.println("Restoring "+fileName);
 //       e.printStackTrace();
 //       throw new RuntimeException("Failed");
      }
      return result;
    }
    ////////////////////////////////////////////
    // GZip utilities
    ////////////////////////////////////////////

    /**
     * Save content to a .gz file
     * @param fileName e.g. foo.txt.gz
     * @param content
     */
    public void saveGZipFile(String fileName, String content) {
      try {
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(fileName));
        PrintWriter pw = new PrintWriter(out);
        pw.write(content);
        pw.flush();
        pw.close();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    public PrintWriter getGZipWriter(String fileName) throws Exception {
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(fileName));
        return new PrintWriter(out);
    }

    public void saveGZipFile(File outFile, String content) throws Exception{
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outFile));
        PrintWriter pw = new PrintWriter(out);
        pw.write(content);
        pw.flush();
        pw.close();
    }
    
    /**
     * Retrieve a String from a .gz file
     * @param fileName e.g. bar.xml.gz
     * @return
     */
    public String openGZipFile(String fileName) {
      try {
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(
            fileName));
        StringBuffer buf = new StringBuffer();
        byte [] b = new byte[1024];
        int length;
        while ((length = in.read(b)) > 0) {
          String s = new String(b);
          buf.append(s);
        }
        return buf.toString().trim();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      return null;
    }

    public GZIPInputStream getGZipInputStream(FileInputStream fis) throws IOException {
    	return new GZIPInputStream(fis);
    }
}
/**
	ChangeLog
	20020512	JP: minor fix in readFile
**/