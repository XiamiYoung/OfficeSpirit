package com.xiami.bean;

public class TextFileBean {
	private String UUID;
	private String name;
	private String path;
	private int lastOpened;

	public int getLastOpened() {
		return lastOpened;
	}

	public void setLastOpened(int lastOpened) {
		this.lastOpened = lastOpened;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uuid) {
		UUID = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
