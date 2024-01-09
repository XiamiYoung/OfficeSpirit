package com.xiami.Component;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import com.xiami.core.SystemConfig;
import com.xiami.frame.WordPanel;
import com.xiami.logic.FileLoader;

public class DroppableList extends JList implements DropTargetListener,
		DragSourceListener, DragGestureListener {
	
	private static final long serialVersionUID = 1L;
	
	DropTarget dropTarget = new DropTarget(this, this);
	DragSource dragSource = DragSource.getDefaultDragSource();
	WordPanel wordPanel;

	public DroppableList(DefaultListModel model) {
		dragSource.createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_COPY_OR_MOVE, this);
		setModel(model);
		this.setCellRenderer(new CustomCellRenderer());
	}

	public void dragDropEnd(DragSourceDropEvent DragSourceDropEvent) {
	}

	public void dragEnter(DragSourceDragEvent DragSourceDragEvent) {
	}

	public void dragExit(DragSourceEvent DragSourceEvent) {
	}

	public void dragOver(DragSourceDragEvent DragSourceDragEvent) {
	}

	public void dropActionChanged(DragSourceDragEvent DragSourceDragEvent) {
	}

	public void dragEnter(DropTargetDragEvent dropTargetDragEvent) {
//		Transferable tr = dropTargetDragEvent.getTransferable();
//		try {
//			java.util.List fileList = (java.util.List) tr
//			.getTransferData(DataFlavor.javaFileListFlavor);
//			File file = (File) fileList.get(0);
//			if (file.getAbsoluteFile().toString().toLowerCase().endsWith(
//					"xls")) {
//				dropTargetDragEvent.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
//			}
//		} catch (UnsupportedFlavorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
	}

	public void dragExit(DropTargetEvent dropTargetEvent) {
	}

	public void dragOver(DropTargetDragEvent dropTargetDragEvent) {
	}

	public void dropActionChanged(DropTargetDragEvent dropTargetDragEvent) {
	}

	@SuppressWarnings("unchecked")
	public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
		try {
			Transferable tr = dropTargetDropEvent.getTransferable();
			if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dropTargetDropEvent
						.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				java.util.List<Object> fileList = (List<Object>) tr
						.getTransferData(DataFlavor.javaFileListFlavor);
//				Iterator iterator = fileList.iterator();
				//multi drop
				//  while (iterator.hasNext())
				//{
				//  File file = (File)iterator.next();
				// Hashtable hashtable = new Hashtable();
				//  hashtable.put("name",file.getName());
				//  hashtable.put("url",file.toURL().toString());
				//  hashtable.put("path",file.getAbsolutePath());
				//   ((DefaultListModel)getModel()).addElement(hashtable);
				File file = (File) fileList.get(0);
				if (file.getAbsoluteFile().toString().toLowerCase().endsWith(
						"xls")) {
					FileLoader.setFile(file);
					FileLoader.updateHistory(file);
					wordPanel.loadContent();
					wordPanel.updateItemHistory();
					//                }
					dropTargetDropEvent.getDropTargetContext().dropComplete(
							true);
				} else {
					JOptionPane.showMessageDialog(this, SystemConfig
							.getLanguageProp("Message.XlsOnly"));
				}
			} else {
				System.err.println("Rejected");
				dropTargetDropEvent.rejectDrop();
			}
		} catch (IOException io) {
			io.printStackTrace();
			dropTargetDropEvent.rejectDrop();
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
			dropTargetDropEvent.rejectDrop();
		}
	}

	public void dragGestureRecognized(DragGestureEvent dragGestureEvent) {
//		if (getSelectedIndex() == -1)
//			return;
//		Object obj = getSelectedValue();
//		if (obj == null) {
//			// Nothing selected, nothing to drag
//			System.out.println("Nothing selected - beep");
//			getToolkit().beep();
//		} else {
//			Hashtable table = (Hashtable) obj;
//			FileSelection transferable = new FileSelection(new File(
//					(String) table.get("path")));
//			dragGestureEvent.startDrag(DragSource.DefaultCopyDrop,
//					transferable, this);
//		}
	}

	class FileSelection extends Vector<Object> implements Transferable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		final static int FILE = 0;
		final static int STRING = 1;
		final static int PLAIN = 2;
		@SuppressWarnings("deprecation")
		DataFlavor flavors[] = { DataFlavor.javaFileListFlavor,
				DataFlavor.stringFlavor, DataFlavor.plainTextFlavor };

		@SuppressWarnings("unchecked")
		public FileSelection(File file) {
			addElement(file);
		}

		/* Returns the array of flavors in which it can provide the data. */
		public synchronized DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		/* Returns whether the requested flavor is supported by this object. */
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			boolean b = false;
			b |= flavor.equals(flavors[FILE]);
			b |= flavor.equals(flavors[STRING]);
			b |= flavor.equals(flavors[PLAIN]);
			return (b);
		}

		/**
		 * If the data was requested in the "java.lang.String" flavor,
		 * return the String representing the selection.
		 */
		public synchronized Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (flavor.equals(flavors[FILE])) {
				return this;
			} else if (flavor.equals(flavors[PLAIN])) {
				return new StringReader(((File) elementAt(0)).getAbsolutePath());
			} else if (flavor.equals(flavors[STRING])) {
				return ((File) elementAt(0)).getAbsolutePath();
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}
	}

	class CustomCellRenderer implements ListCellRenderer {
		DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer();

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean selected, boolean hasFocus) {
			listCellRenderer.getListCellRendererComponent(list, value, index,
					selected, hasFocus);
			listCellRenderer.setText(getValueString(value));
			return listCellRenderer;
		}

		private String getValueString(Object value) {
			String returnString = "null";
			if (value != null) {
				//              if (value instanceof Hashtable) {
				//                Hashtable h = (Hashtable)value;
				//                String name = (String)h.get("name");
				//                String url = (String)h.get("url");
				//                returnString = name + " ==> " + url;
				//              } else {
				returnString = value.toString();
				//              }
			}
			return returnString;
		}
	}

	/**
	 * @return the wordPanel
	 */
	public WordPanel getWordPanel() {
		return wordPanel;
	}

	/**
	 * @param wordPanel the wordPanel to set
	 */
	public void setWordPanel(WordPanel wordPanel) {
		this.wordPanel = wordPanel;
	}
}
