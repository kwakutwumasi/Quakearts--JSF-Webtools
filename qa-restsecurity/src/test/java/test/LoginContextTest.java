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
package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.security.URIParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;

import org.junit.Test;

import com.quakearts.webapp.security.rest.context.LoginContext;

public class LoginContextTest {

	@Test
	public void testLoginContext() {
		try {			
			final CallbackHandler handler = (c) -> {
			};
			URL resource = Thread.currentThread().getContextClassLoader().getResource("login.config");
			URI uri = resource.toURI();
			final Configuration jaasConfig = Configuration.getInstance("JavaLoginConfig", new URIParameter(uri));

			LoginContext context = new LoginContext("TestConfigDirect", new Subject(), handler, jaasConfig);
			context.login();
			context.logout();
			
			context = new LoginContext("TestConfigWrapped", new Subject(), handler, jaasConfig);
			context.login();
			context.logout();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unable to instantiate LoginContext");
		}
	}
	
	@Test
	public void testLoginConfig() throws Exception {
		try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("authenticationmatrix.csv")){
			URL resource = Thread.currentThread().getContextClassLoader().getResource("login.config");
			URI uri = resource.toURI();
			final Configuration jaasConfig = Configuration.getInstance("JavaLoginConfig", new URIParameter(uri));

			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = reader.readLine();
			do {
				String[] parts = line.split(",");
				String config = parts[0];
				
				List<List<Boolean>> authenticationMatrix = new ArrayList<>();
				for(int i=0;i<16;i++) {
					authenticationMatrix.add(new ArrayList<>());
				}
				
				for(int i=0;i<4;i++) {
					line = reader.readLine();
					parts = line.split(",");
					for(int j = 1; j<17; j++) {
						authenticationMatrix
							.get(j-1)
							.add(Boolean.parseBoolean(parts[j]));
					}
				}
				
				line = reader.readLine();
				parts = line.split(",");
				List<Boolean> results = new ArrayList<>();
				for(int j = 1; j<17; j++) {
					results.add(Boolean.parseBoolean(parts[j]));
				}
				
				int i=0;
				for(List<Boolean> authenticationMatrixEntry:authenticationMatrix) {
					StringBuilder builder = new StringBuilder();
					int j=1;
					for(Boolean bool:authenticationMatrixEntry) {
						if(!bool)
							builder.append("user")
								.append(j)
								.append(" ");
						
						j++;
					}
					CallbackHandler handler = (callbacks)->{
						((NameCallback)callbacks[0]).setName(builder.toString());
					};
					
					try {
						LoginContext context = new LoginContext(config, null, handler, jaasConfig);
						context.login();
						if(!results.get(i))
							fail("Failed for permutation "+config
									+"; Authentication failed when it should have succeed. Element "
									+(i+1)+". Matrix: "
									+authenticationMatrixEntry);
					} catch (LoginException e) {
						if(results.get(i))
							fail("Failed for permutation "+config
									+"; Authentication failed when it should have succeed. Element "
									+(i+1)+". Matrix: "
									+authenticationMatrixEntry);
					}
					
					i++;
				}
				line = reader.readLine();
			} while (line!=null);
		}
	}
	
	public static void main(String[] args) {
		final CallbackHandler handler = (c) -> {
		};
		try {
			URL resource = Thread.currentThread().getContextClassLoader().getResource("login.config");
			URI uri = resource.toURI();
			final Configuration jaasConfig = Configuration.getInstance("JavaLoginConfig", new URIParameter(uri));

			// Warm up
			for (int count = 0; count < 1000000; count++) {
				useloginContext(handler, jaasConfig);
				usedirect(handler);
				useInternalContext(handler, jaasConfig);
			}

			long start = System.nanoTime();
			useloginContext(handler, jaasConfig);
			long stop = System.nanoTime();
			System.out.println("useloginContext: " + (stop - start) + " ns");
			start = System.nanoTime();
			usedirect(handler);
			stop = System.nanoTime();
			System.out.println("usedirect: " + (stop - start) + " ns");
			start = System.nanoTime();
			useInternalContext(handler, jaasConfig);
			stop = System.nanoTime();
			System.out.println("useInternalContext: " + (stop - start) + " ns");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void useloginContext(CallbackHandler handler, Configuration jaasConfig) throws Exception {
		javax.security.auth.login.LoginContext context = new javax.security.auth.login.LoginContext("TestConfigDirect", null,
				handler, jaasConfig);
		context.login();
	}

	static void useInternalContext(CallbackHandler handler, Configuration jaasConfig) throws Exception {
		com.quakearts.webapp.security.rest.context.LoginContext context = new com.quakearts.webapp.security.rest.context.LoginContext(
				"TestConfigDirect", null, handler, jaasConfig);
		context.login();
	}

	static void usedirect(CallbackHandler handler) throws Exception {
		TestLoginModuleDirect module = new TestLoginModuleDirect();
		module.initialize(new Subject(), handler, new HashMap<>(), new HashMap<>());
		module.login();
	}

}
