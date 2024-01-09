package com.xiami.dict;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

/** */
/**  
* @author bean  
*   
*/
public class WordGetterScript {

	private Robot robot = null;

	public WordGetterScript() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/** */
	public void keyBoardDemo() {
		robot.keyPress(KeyEvent.VK_ALT);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_TAB);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_TAB);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_TAB);
	}

	public void mouseDemo(String word) {
		
		setClipBoardContents(word);
		
		robot.setAutoWaitForIdle(false);
		
		robot.mouseMove(480, 90);
		
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		
		robot.mouseMove(62, 90);
		
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		
		robot.keyPress(KeyEvent.VK_CONTROL);
		
		robot.keyPress(KeyEvent.VK_V);
		
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		robot.keyRelease(KeyEvent.VK_V);
		
		robot.keyPress(KeyEvent.VK_ENTER);
		
		robot.keyRelease(KeyEvent.VK_ENTER);
		
		robot.mouseMove(225, 280);
		
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		
		robot.keyPress(KeyEvent.VK_CONTROL);
		
		robot.keyPress(KeyEvent.VK_A);
		
		robot.keyRelease(KeyEvent.VK_A);
		
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		robot.keyPress(KeyEvent.VK_CONTROL);
		
		robot.keyPress(KeyEvent.VK_C);
		
		robot.keyRelease(KeyEvent.VK_C);
		
		robot.keyRelease(KeyEvent.VK_CONTROL);
		
		
//		robot.keyPress(KeyEvent.VK_ESCAPE);
//		
//		robot.keyRelease(KeyEvent.VK_ESCAPE);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(getClipBoardContents());
		
	}

	public static void setClipBoardContents(String content) {
        try {
          
            StringSelection stringSelection = new StringSelection(content);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	 public static String getClipBoardContents() {
	        String text = "";
	        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	        Transferable contents = clipboard.getContents(null);
	        boolean hasTransferableText = (contents != null) &&
	            contents.isDataFlavorSupported(DataFlavor.stringFlavor);
	        if (hasTransferableText) {
	            try {
	                text = (String) contents.getTransferData(DataFlavor.stringFlavor);
	                
	                return text;
	            }
	            catch (UnsupportedFlavorException ex) {
	                ex.printStackTrace();
	            }
	            catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return null;
	    }

	
	/** */
	/**  
	   * @param args  
	   */
	public static void main(String[] args) {
		WordGetterScript demo = new WordGetterScript();
//		demo.keyBoardDemo();
		try {
			Thread.sleep(3000);
			demo.mouseDemo("には");
			demo.mouseDemo("は");
			demo.mouseDemo("には");
			demo.mouseDemo("にばん");
			demo.mouseDemo("番");
			demo.mouseDemo("は");
			demo.mouseDemo("には");
			demo.mouseDemo("は");
			demo.mouseDemo("には");
			demo.mouseDemo("は");
			demo.mouseDemo("には");
			demo.mouseDemo("は");
			demo.mouseDemo("には");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		;
	}

}
