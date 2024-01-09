package com.xiami.core;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.NoSuchValueException;
import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class RegEditTool {

	public static boolean setValue(String folder, String subKeyNode,
			String subKeyName, String subKeyValue) {
		try {
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE
					.openSubKey(folder);
			RegistryKey subKey = software.createSubKey(subKeyNode, "");
			subKey
					.setValue(new RegStringValue(subKey, subKeyName,
							subKeyValue));
			subKey.closeKey();
			return true;
		} catch (NoSuchKeyException e) {
			JOptionPane.showMessageDialog(new JFrame(), "No such Key");
		} catch (NoSuchValueException e) {
			JOptionPane.showMessageDialog(new JFrame(), "No such value");
		} catch (RegistryException e) {
			JOptionPane.showMessageDialog(new JFrame(), "RegistryException");
		}
		return false;
	}

	public static boolean deleteValue(String folder, String subKeyNode,
			String subKeyName) {

		try {
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE
					.openSubKey(folder);
			RegistryKey subKey = software.createSubKey(subKeyNode, "");
			subKey.deleteValue(subKeyName);
			subKey.closeKey();
			return true;
		} catch (NoSuchKeyException e) {
			JOptionPane.showMessageDialog(new JFrame(), "No such Key");
		} catch (NoSuchValueException e) {
			JOptionPane.showMessageDialog(new JFrame(), "No such value");
		} catch (RegistryException e) {
			JOptionPane.showMessageDialog(new JFrame(), "RegistryException");
		}
		return false;
	}

	public static boolean deleteSubKey(String folder, String subKeyNode) {
		try {
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE
					.openSubKey(folder);
			software.deleteSubKey(subKeyNode);
			software.closeKey();
			return true;
		} catch (NoSuchKeyException e) {
			JOptionPane.showMessageDialog(new JFrame(), "No such Key");
		} catch (RegistryException e) {
			JOptionPane.showMessageDialog(new JFrame(), "RegistryException");
		}
		return false;
	}

	public static String getValue(String folder, String subKeyNode,
			String subKeyName) {
		String value = "";
		try {
			RegistryKey software = Registry.HKEY_LOCAL_MACHINE
					.openSubKey(folder);
			RegistryKey subKey = software.openSubKey(subKeyNode);
			value = subKey.getStringValue(subKeyName);
			subKey.closeKey();
		}  catch (NoSuchKeyException e) {
			JOptionPane.showMessageDialog(new JFrame(), "No such Key");
		} catch (NoSuchValueException e) {
			JOptionPane.showMessageDialog(new JFrame(), "No such value");
		} catch (RegistryException e) {
			JOptionPane.showMessageDialog(new JFrame(), "RegistryException");
		}
		return value;
	}

	public static void main(String[] args) {
		//        setValue("SOFTWARE", "Microsoft\\Windows\\CurrentVersion\\Run", "test",   
		//                "C:\\1.exe");   
		//    	deleteValue("SOFTWARE", "Microsoft\\Windows\\CurrentVersion\\Run",   
		//    			"test");
	}
}
