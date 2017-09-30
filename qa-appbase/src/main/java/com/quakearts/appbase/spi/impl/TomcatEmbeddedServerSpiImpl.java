/*******************************************************************************
* Copyright (C) 2016 Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Kwaku Twumasi-Afriyie <kwaku.twumasi@quakearts.com> - initial API and implementation
 ******************************************************************************/
package com.quakearts.appbase.spi.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.scan.StandardJarScanner;
import com.quakearts.appbase.Main;
import com.quakearts.appbase.exception.ConfigurationException;
import com.quakearts.appbase.spi.EmbeddedWebServerSpi;
import com.quakearts.appbase.internal.properties.AppBasePropertiesLoader;
import com.quakearts.appbase.internal.properties.ConfigurationPropertyMap;


public class TomcatEmbeddedServerSpiImpl implements EmbeddedWebServerSpi {
	
	@Override
	public void initiateEmbeddedWebServer() {
		
        File webserversDirLocation = new File("webservers");
        
        if(!webserversDirLocation.exists()){
        	webserversDirLocation.mkdir();
        	return;
        } else if(!webserversDirLocation.isDirectory()){
        	throw new ConfigurationException(webserversDirLocation.getAbsolutePath()+ " is not a directory");
        } else {
        	AppBasePropertiesLoader loader = new AppBasePropertiesLoader();
        	for(File webserverLocation:webserversDirLocation.listFiles()){
        		if(webserverLocation.isDirectory()){
        			launchInstance(webserverLocation, loader);
        		}
        	}
        }
	}
	
	private void launchInstance(File webserverLocation, AppBasePropertiesLoader loader) throws ConfigurationException {
        if(!webserverLocation.exists()){
        	throw new ConfigurationException("Missing webapp: /"+webserverLocation);
        }
        
        final Tomcat tomcat = new Tomcat();
        
        tomcat.setBaseDir(webserverLocation.getPath());
        
        File configurationFile = new File(webserverLocation, "conf"+File.separator+"server.config.json");
        if(configurationFile.exists() && configurationFile.isFile()){
	        
	        //The port that we should run on can be set into an environment variable
	        //Look for that variable and default to 8080 if it isn't there.
	        
            final ConfigurationPropertyMap serverConfiguration = loader.loadParametersFromFile(configurationFile);            
         
            if (serverConfiguration.containsKey("port")) {
				tomcat.setPort(serverConfiguration.getInt("port"));
			}
            
			if(serverConfiguration.containsKey("useapr") 
					&& serverConfiguration.getBoolean("useapr")) {
				tomcat.getServer().addLifecycleListener(new AprLifecycleListener());
			}
			
			if(serverConfiguration.containsKey("hostname"))
				tomcat.setHostname(serverConfiguration.getString("hostname"));

			if (serverConfiguration.containsKey("connectorProtocol")){
				Connector connector = new Connector(serverConfiguration.getString("connectorProtocol"));
	            
				if (serverConfiguration.containsKey("port")) {
	            	connector.setPort(serverConfiguration.getInt("port"));
	            }
	            
				tomcat.setConnector(connector);
			}
			
			try {
				setPropertiesIfAvailable(tomcat.getConnector(), "connector", serverConfiguration);				
			} catch (ConfigurationException e) {
				throw new ConfigurationException("Invalid connector property in /"+configurationFile.getPath()+":"+e.getMessage(), e);
			} catch (IntrospectionException e) {
				throw new ConfigurationException("Unable to instrospect "+Connector.class+": "+e.getMessage(), e);
			}
			
			getProperties("connectorProtocol.", serverConfiguration).keySet().stream().forEach((key)->{
				String propertyName = key.substring(key.indexOf(".")+1);
				String value = serverConfiguration.getString(key);
				tomcat.getConnector().setProperty(propertyName, value);
			});			

			if (serverConfiguration.containsKey("usessl") 
					&& serverConfiguration.getBoolean("usessl")) {
				SSLHostConfig sslHostConfig = new SSLHostConfig();
			
				try {
					setPropertiesIfAvailable(sslHostConfig, "sslHostConfig", serverConfiguration);
				} catch (ConfigurationException e) {
					throw new ConfigurationException("Invalid ssl property in /"+configurationFile.getPath()+":"+e.getMessage());
				} catch (IntrospectionException e) {
					throw new ConfigurationException("Unable to instrospect "+SSLHostConfig.class+": "+e.getMessage());
				}
				
				tomcat.getConnector().addSslHostConfig(sslHostConfig);
				tomcat.getConnector().setScheme("https");
				tomcat.getConnector().setProperty("SSLEnabled", "true");
				tomcat.getConnector().setSecure(true);
				
				if(serverConfiguration.containsKey("addunsecured") 
						&& serverConfiguration.getBoolean("addunsecured")) {
					Connector connector;
					if (serverConfiguration.containsKey("unsecuredConnectorProtocol")){
						connector = new Connector(serverConfiguration.getString("unsecuredConnectorProtocol"));
					} else {
						connector = new Connector();
					}
					
					if (serverConfiguration.containsKey("unsecuredPort")) {
						connector.setPort(serverConfiguration.getInt("unsecuredPort"));
					} else {
						throw new ConfigurationException("Missing property in /"+configurationFile.getPath()+": unsecuredPort is required");
					}
					
					try {
						setPropertiesIfAvailable(connector, "unsecuredConnector", serverConfiguration);				
					} catch (ConfigurationException e) {
						throw new ConfigurationException("Invalid unsecured connector property in /"+webserverLocation.getName()+":"+e.getMessage(), e);
					} catch (IntrospectionException e) {
						throw new ConfigurationException("Unable to instrospect "+Connector.class+": "+e.getMessage(), e);
					}
					
					getProperties("unsecuredConnectorProtocol.", serverConfiguration).keySet().stream().forEach((key)->{
						String propertyName = key.substring(key.indexOf(".")+1);
						String value = serverConfiguration.getString(key);
						connector.setProperty(propertyName, value);
					});
					
					connector.setRedirectPort(tomcat.getConnector().getPort());
					tomcat.getService().addConnector(connector);
				}
			}			
	    } else if(configurationFile.exists() && !configurationFile.isFile()) {
        	throw new ConfigurationException("Invalid file in /"+webserverLocation+". conf/server.config.json is not a file.");
        }
        
        File webappDirLocation = new File(webserverLocation,"webapps");
        
        if(webappDirLocation.exists() && webappDirLocation.isDirectory())
	        for(File webappFolder:webappDirLocation.listFiles()){
	        	if(!webappFolder.isDirectory())
	        		continue;
	        	
		        StandardContext ctx;
				try {
					String contextName = webappFolder.getName().equals("webapp")?"":webappFolder.getName();
					if(contextName.endsWith(".war"))
						contextName = contextName.substring(0, contextName.indexOf(".war"));
					
					ctx = (StandardContext) tomcat.addWebapp("/"+contextName, webappFolder.getAbsolutePath());
				} catch (ServletException e) {
					throw new ConfigurationException("ServletException adding webapp "+webappFolder.getAbsolutePath(), e);
				}
				
				if(ctx.getJarScanner() instanceof StandardJarScanner)
					((StandardJarScanner)ctx.getJarScanner()).setScanClassPath(false);
		        
				Main.log.debug("Configured web application with base directory: " +webappFolder.getAbsolutePath());
		
		        // Declare an alternative location for your "WEB-INF/classes" dir
		        // Servlet 3.0 annotation will work
		        File additionWebInfClasses = new File(webappFolder,"WEB-INF/classes");
		        
		        if(!additionWebInfClasses.exists()){
		        	additionWebInfClasses.mkdirs();
		        } else if(!additionWebInfClasses.isDirectory()){
		        	throw new ConfigurationException(additionWebInfClasses.getAbsolutePath()+ " is not a directory");
		        }
		        
		        WebResourceRoot resources = new StandardRoot(ctx);
		        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
		                additionWebInfClasses.getAbsolutePath(), "/"));
		        
		        ctx.setResources(resources);
		        Main.log.debug("Configured additional WEB-INF/classes with directory: " +additionWebInfClasses.getAbsolutePath());
		        
		        File contextFile = new File(webappFolder,"META-INF/context.xml");
		        if(contextFile.exists()){
		        	try {
						ctx.setConfigFile(contextFile.toURI().toURL());
					} catch (MalformedURLException e) {
			        	throw new ConfigurationException(contextFile.getAbsolutePath()+ ": "+e.getMessage(), e);
					}
		        }
	        }
        else if(webappDirLocation.exists() && webappDirLocation.isFile())
        	throw new ConfigurationException("Invalid file in /"+webserverLocation+". webapps is not a directory.");
        else
        	webappDirLocation.mkdir();
        	
        try {
			tomcat.start();
		} catch (LifecycleException e) {
			throw new ConfigurationException("Unable to start embedded server", e);
		}
        
        new Thread(()->{
            tomcat.getServer().await();
        }).start();
	}

	private void setPropertiesIfAvailable(Object target, String prefix, ConfigurationPropertyMap properties) throws IntrospectionException{
		BeanInfo info = Introspector.getBeanInfo(target.getClass());
		
		for(PropertyDescriptor propertyDescriptor: info.getPropertyDescriptors()){
			String propertyName = (prefix!=null?prefix+".":"")+propertyDescriptor.getName();
			try {
				if(properties.containsKey(propertyName) &&
						propertyDescriptor.getWriteMethod()!=null){
					Method setMethod = propertyDescriptor.getWriteMethod();
					setMethod.invoke(target, properties.get(propertyName,propertyDescriptor.getPropertyType()));
				}
			} catch (ClassCastException e) {
				throw new ConfigurationException(propertyName+" cannot be cast to "+propertyDescriptor.getPropertyType());
			} catch (IllegalAccessException | IllegalArgumentException |InvocationTargetException e) {
				throw new ConfigurationException(propertyName+" cannot be written to "+target.getClass().getName()+": "+e.getMessage());
			}
		}
	}
	
	private Map<String, Serializable> getProperties(String prefix, Map<String, Serializable> properties){
		Map<String, Serializable> filteredMap = new ConcurrentHashMap<>();
		
		properties.keySet().stream().parallel().filter((key)->{ return key.startsWith(prefix);}).forEach((key)->{
			filteredMap.put(key, properties.get(key));
		});
		
		return filteredMap;
	}
	
	@Override
	public void shutdownEmbeddedWebServer() {
	}
	
}
