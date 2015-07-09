package com.quakearts.tools.classloaders.hibernate;

// Generated 17-Mar-2014 00:13:24 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * JarFile generated by hbm2java
 */
public class JarFile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 520678837264493311L;
	private long id;
	private byte[] jarData;
	private String jarName;
	private Set<JarFileEntry> jarFileEntries = new HashSet<JarFileEntry>(0);

	public JarFile() {
	}

	public JarFile(byte[] jarData) {
		this.jarData = jarData;
	}

	public JarFile(byte[] jarData, String jarName, Set<JarFileEntry> jarFileEntries) {
		this.jarData = jarData;
		this.jarName = jarName;
		this.jarFileEntries = jarFileEntries;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getJarData() {
		return this.jarData;
	}

	public void setJarData(byte[] jarData) {
		this.jarData = jarData;
	}

	public Set<JarFileEntry> getJarFileEntries() {
		return this.jarFileEntries;
	}

	public void setJarFileEntries(Set<JarFileEntry> jarFileEntries) {
		this.jarFileEntries = jarFileEntries;
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}
}
