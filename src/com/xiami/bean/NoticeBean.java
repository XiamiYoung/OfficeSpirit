package com.xiami.bean;


@SuppressWarnings("unchecked")
public class NoticeBean {

	private String UUID;
	private String name;
	private String user;
	private String fromtime;
	private String noticetime;
	private String content;
	private String isNoticed = "false";
	private String isCommon;



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the fromtime
	 */
	public String getFromtime() {
		return fromtime;
	}

	/**
	 * @param fromtime the fromtime to set
	 */
	public void setFromtime(String fromtime) {
		this.fromtime = fromtime;
	}

	public String getNoticetime() {
		return noticetime;
	}

	public void setNoticetime(String noticetime) {
		this.noticetime = noticetime;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public String getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(String isCommon) {
		this.isCommon = isCommon;
	}

	public String getIsNoticed() {
		return isNoticed;
	}

	public void setIsNoticed(String isNoticed) {
		this.isNoticed = isNoticed;
	}

	/**
	 * @return the uUID
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * @param uuid the uUID to set
	 */
	public void setUUID(String uuid) {
		UUID = uuid;
	}

}
