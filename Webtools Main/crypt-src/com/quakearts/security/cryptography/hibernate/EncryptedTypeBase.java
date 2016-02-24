package com.quakearts.security.cryptography.hibernate;

import java.util.logging.Logger;

import javax.naming.InitialContext;

import org.hibernate.usertype.UserType;
import com.quakearts.security.cryptography.CryptoProxy;
import com.quakearts.security.cryptography.CryptoResource;
import com.quakearts.security.cryptography.CryptoUtils;
import com.quakearts.webapp.hibernate.HibernateHelper;

public abstract class EncryptedTypeBase implements UserType {

	private transient CryptoResource resource;
	private static final Logger log = Logger.getLogger(EncryptedStringType.class.getName());

	
	protected CryptoResource getCryptoResource(){
		if(resource==null){
			try {
				String serviceJNDIname = HibernateHelper.getCurrentConfiguration().getProperty("com.quakearts.cryptoservice");
				InitialContext ctx = CryptoUtils.getInitialContext();
				CryptoProxy proxy =(CryptoProxy) ctx.lookup(serviceJNDIname);
				resource = proxy.getResource();
			} catch (Exception e) {
				log.severe("Cannot perform cryptography: "+e.getMessage()+". "+e.getClass().getName());
				throw new RuntimeException();
			}
		}
		return resource;
	}
}
