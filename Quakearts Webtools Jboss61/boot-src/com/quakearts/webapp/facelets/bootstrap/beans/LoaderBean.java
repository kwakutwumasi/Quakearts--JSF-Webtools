package com.quakearts.webapp.facelets.bootstrap.beans;

public class LoaderBean {
	private static final String MAINAJAXLOADER = "qaboot.mainLoaderHandler";
	private static final String MINIAJAXLOADER = "qaboot.miniLoaderHandler";
	
	public String getMainAjaxLoader() {
		return MAINAJAXLOADER;
	}
	
	public String getMiniAjaxLoader() {
		return MINIAJAXLOADER;
	}
}
