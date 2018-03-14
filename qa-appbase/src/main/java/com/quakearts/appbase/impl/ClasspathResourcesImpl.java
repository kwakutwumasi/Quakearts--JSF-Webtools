package com.quakearts.appbase.impl;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.jar.Manifest;

import com.quakearts.appbase.ClasspathResources;
import com.quakearts.appbase.Main;
import com.quakearts.appbase.exception.ConfigurationException;

public class ClasspathResourcesImpl implements ClasspathResources {

	private HashMap<String, URL> libraryMap = new HashMap<>();
	
	@Override
	public URL getLibraryPath(String libraryName) {
		if(libraryMap.containsKey(libraryName))
			return libraryMap.get(libraryName);
		
		throw new ConfigurationException("Unable to find library URL for library: "+libraryName);
	}

	public void getClasspathFromManifest(URL url) {
		try {
			URL jarUrl = new URL("jar:"+url.toString()+"!/");
			JarURLConnection con = (JarURLConnection) jarUrl.openConnection();
			Manifest manifest = con.getManifest();
			if(manifest != null && manifest.getMainAttributes().getValue("Class-Path")!=null) {
				String[] classpathEntries = manifest.getMainAttributes().getValue("Class-Path").split(" ");
				for(String classpathEntry:classpathEntries) {
					try {
						URL fileurl; 
						File file; 
						
						if(classpathEntry.startsWith("file:/")) {
							fileurl = new URL(classpathEntry);
							file = new File(fileurl.toURI());
						} else {
							file = new File(classpathEntry);
							if(!file.exists())
								continue;
							
							fileurl = file.toURI().toURL();
						}
						
						if(file.isDirectory())
							continue;
						
						String name = file.getName();		
							libraryMap.put(name,fileurl);
					} catch (URISyntaxException e) {
						throw new ConfigurationException("Unable to create url for entry "+classpathEntry);
					}
				}
			}
		} catch (IOException e) {
			throw new ConfigurationException("Unable to access jar url", e);
		}
	}
	
	@Override
	public void addLibraryPath(URL libraryPathUrl) {
		if(libraryPathUrl == null)
			throw new ConfigurationException("libraryPathUrl is required");

		if(!libraryPathUrl.getProtocol().equals("file"))
			throw new ConfigurationException("libraryPathUrl must be a file url");
		
		try {
			File testFile = new File(libraryPathUrl.toURI());
			if(testFile.isDirectory())
				return;
		
			getClasspathFromManifest(libraryPathUrl);
			
			String name = testFile.getName();		
			libraryMap.put(name,libraryPathUrl);
		} catch (URISyntaxException e) {
			throw new ConfigurationException("Unable to access url "+libraryPathUrl);
		}
	}

	@Override
	public void loadClassPath() {
		ClassLoader loader = Main.class.getClassLoader(); //Use the class loader that loaded Main
		if(loader instanceof URLClassLoader) {
			loadFromClassLoader((URLClassLoader)loader);
		} else {
			loadFromSystemProperty();
		}
	}

	public void loadFromClassLoader(URLClassLoader loader) {
		for(URL url: loader.getURLs()) {
			addLibraryPath(url);
		}
	}

	public void loadFromSystemProperty() {
		String classpathString = System.getProperty("java.class.path");
		String[] classpathParts = classpathString.split(":");
		for(String classpathPart:classpathParts) {
			File file = new File(classpathPart);
			if(file.exists() && !file.isDirectory()) {
				try {
					addLibraryPath(file.toURI().toURL());
				} catch (MalformedURLException e) {
					throw new ConfigurationException("Unable to add library path: "+file+" caused a malformed URL exception");
				}
			}
		}
	}

	@Override
	public String toString() {
		return "ClasspathResourcesSpiImpl [libraryMap=" + libraryMap + "]";
	}
	
}
