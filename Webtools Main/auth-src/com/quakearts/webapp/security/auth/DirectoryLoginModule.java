package com.quakearts.webapp.security.auth;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.spi.LoginModule;
import com.novell.ldap.*;
import com.quakearts.webapp.security.auth.util.AttemptChecker;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectoryLoginModule implements LoginModule{
	public static final String DEFAULT_AUTH_GRP = "com.quakearts.webapp.security.auth";
    private Subject subject;
    private Group rolesgrp;
    private CallbackHandler callbackHandler;
    @SuppressWarnings("rawtypes")
	private Map sharedState;
    @SuppressWarnings({ "unused", "rawtypes" })
	private Map options;
    private int ldapPort;
    private String ldapHost, keyStorePath,searchuser,searchpass,searchbasedn,filterParam,subject_username,password_param;
    private LDAPConnection conn;
    private boolean loginOk=false, use_first_pass,usecompare,usessl,allowEmptyPassword;
    private LDAPSocketFactory ssf;
    /* attribute variable must be a list of exactly 9 directory attributes corresponding to the following (in strict order)
     * (firstname), (surname), (email address), (unit), (department), (branch), (position), (grade), (staff number)
     * ex "givenname","sn","mail","costcenter","ou","sitelocation","title","employeetype","employeenumber"
    */
    private String[] attributes;
    private LDAPEntry userprof;
    private String rolesgrpname;
    private String[] defaultroles;
    private static final Logger log = Logger.getLogger(DirectoryLoginModule.class.getName());
    private AttemptChecker checker;
    
    public DirectoryLoginModule() {
    }

    @SuppressWarnings("rawtypes")
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) 
    {
        log.fine("Initializing....");
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        String ldapPort_str = null;
        ldapHost = (String) options.get("ldap.server");        	        	
        ldapPort_str = (String) options.get("ldap.port");
        keyStorePath = (String) options.get("ldap.keystore");
        usessl = Boolean.parseBoolean((String)options.get("ldap.ssl.use"));
        
        usecompare = Boolean.parseBoolean((String) options.get("ldap.compare.use"));
        searchbasedn=(String) options.get("ldap.search.baseDN");
        rolesgrpname = (String) options.get("directory.rolename");
        filterParam = (String) options.get("ldap.filter");
        allowEmptyPassword = Boolean.parseBoolean((String) options.get("ldap.allow.anonymousbind"));
        password_param = (String) options.get("ldap.password.param");
        
        String defaultroles_str = (String) options.get("directory.defaultroles");
        String attributes_str = (String) options.get("directory.attributes");
        
        String maxAttempts_str = (String) options.get("max_try_attempts");
        String lockoutTime_str = (String) options.get("lockout_time");
        
        if(maxAttempts_str == null && lockoutTime_str == null){
        	checker = AttemptChecker.getChecker(ldapHost);
        }else{
	        int maxAttempts, lockoutTime;
	        try {
				maxAttempts = Integer.parseInt(maxAttempts_str);
			} catch (Exception e) {
				maxAttempts = 4;
			}
			
			try {
				lockoutTime = Integer.parseInt(lockoutTime_str);
			} catch (Exception e) {
				lockoutTime = 3600000;
			}
        	AttemptChecker.createChecker(ldapHost, maxAttempts, lockoutTime);
        	checker = AttemptChecker.getChecker(ldapHost);
        }
                
        if(attributes_str!=null)
        	attributes = attributes_str.split(";");
        else
        	attributes = new String[0];
        
        if (rolesgrpname == null)
        	rolesgrpname = new String("Roles");
        
        if(defaultroles_str != null){
        	defaultroles = defaultroles_str.split(";");
        	for(int i=0;i<defaultroles.length;i++)
        		defaultroles[i] = defaultroles[i].trim();
        }
        
    	searchuser = (String) options.get("ldap.search.dn");
    	searchpass = (String) options.get("ldap.search.acc");
		if(searchuser==null || searchpass==null){
		    searchuser="";
			searchpass="";
		}
        
        if(usessl){
            if(System.getProperty("javax.net.ssl.trustStore")==null){
                System.setProperty("javax.net.ssl.trustStore", keyStorePath);
            }
 
            ssf = new LDAPJSSESecureSocketFactory();
            LDAPConnection.setSocketFactory(ssf);
        }

        ldapPort = ldapPort_str == null? (usessl?LDAPConnection.DEFAULT_SSL_PORT:LDAPConnection.DEFAULT_PORT):Integer.parseInt(ldapPort_str);
        
        use_first_pass = Boolean.parseBoolean((String)options.get("use_first_pass"));
        
        log.fine("Initialization complete.\n"+
                  "\t\tZeDirectoryLoginModule options:\n"+
                  "\t\tldap.server: "+ldapHost+"\n"+
                  "\t\tldap.port: "+ldapPort_str+"\n"+
                  "\t\tldap.keystore: "+keyStorePath+"\n"+
                  "\t\tldap.compare.use: "+usecompare+"\n"+
                  "\t\tldap.ssl.use: "+usessl+"\n"+
                  "\t\tldap.search.baseDN: "+searchbasedn+"\n"+
                  "\t\tedirectory.rolename: "+rolesgrpname+"\n"+
                  "\t\tedirectory.defaultroles: "+defaultroles_str+"\n"+
                  "\t\tldap.search.dn: "+searchuser+"\n"
        );
    }

    @SuppressWarnings("unchecked")
	public boolean login() throws LoginException {
        log.fine("Starting authentication......");
        
        if(ldapHost==null)
        	throw new LoginException("No ldap host");
        if(searchbasedn==null)
        	throw new LoginException("No ldap base dn");
        if(filterParam==null)
        	throw new LoginException("No ldap filter param");
        if(usecompare && password_param == null)
        	throw new LoginException("No ldap password param");
        
        String loginDN = null,username=null;
        byte[] password = null;
        conn = new LDAPConnection();

        Callback[] callbacks = new Callback[2];

        if(use_first_pass){
            if(sharedState != null){
                log.fine("Using first pass....");
                Object loginDN_val = sharedState.get("javax.security.auth.login.name");
                Object password_val = sharedState.get("javax.security.auth.login.password");
                username = (loginDN_val!=null && loginDN_val instanceof Principal)?((Principal) loginDN_val).getName():null;
                password = (password_val!=null && password_val instanceof char[])?new String((char[]) password_val).getBytes():null;
            }
        }
        
        if(!use_first_pass || username==null || password==null){
            NameCallback name = new NameCallback("Enter your username.");
            PasswordCallback pass = new PasswordCallback("Enter your password:",false);           
            callbacks[0] = name;
            callbacks[1] = pass;

            try {
                log.fine("Handling callback....");
                callbackHandler.handle(callbacks);
            } catch (UnsupportedCallbackException e) {
                throw new LoginException("Callback is not supported");
            } catch (IOException e) {
                throw new LoginException("IOException during call back");
            }
            
            username = name.getName()==null? name.getDefaultName():name.getName();
            password = (new String(pass.getPassword())).getBytes();
            
            if (sharedState != null){
                log.fine("Storing state....");
                UserPrincipal shareduser = new UserPrincipal(username);
                sharedState.put("javax.security.auth.login.name", shareduser);
                char[] sharedpass = new String(password).toCharArray();
                sharedState.put("javax.security.auth.login.password", sharedpass);
            }
        }
        
        if(username == null || password == null)
            throw new LoginException("Login/Password is null.");
        
        if(!allowEmptyPassword && password.length < 1)
            throw new LoginException("Password is empty.");
        	
        
        subject_username = username;
        
        if(checker.isLocked(username))
        	throw new LoginException("Account has been locked out.");

        try {
            conn.connect(ldapHost,ldapPort);
            log.fine("Connected to ldap://"+ldapHost+":"+ldapPort);
            //Login to ldap server:
            conn.bind(LDAPConnection.LDAP_V3,searchuser, searchpass!=null?searchpass.getBytes():null);
            log.fine("Bound "+searchuser+" to host ldap://"+ldapHost+":"+ldapPort);
            //Search for the user DN
            LDAPSearchResults result = conn.search(searchbasedn,LDAPConnection.SCOPE_SUB,filterParam+"="+username,attributes,false);
            userprof = result.next();
            loginDN = userprof.getDN();
            log.fine("Found DN "+loginDN);

            if(usecompare){
                log.fine("Comparing credentials.");
                LDAPAttribute pword = new LDAPAttribute("userPassword");
                pword.addValue(password);
                
                if(!conn.compare(loginDN,pword))
                    throw new LoginException("Invalid password");
            } else
            {
                try {
                	conn.disconnect();
                    conn.bind( LDAPConnection.LDAP_V3, loginDN, password);
                    log.fine("Bound "+loginDN+" to ldap://"+ldapHost+":"+ldapPort);
                } catch (LDAPException e) {
                    throw new LoginException(e.getMessage());
                }
            }
        } catch (LDAPException e) {
            log.log(Level.SEVERE, "Error while logging in: "+e.getMessage(),e);
            if ( e.getResultCode() == LDAPException.NO_SUCH_OBJECT ) {
                log.log(Level.SEVERE, "Error: No such entry.",e);
            } else if ( e.getResultCode() == LDAPException.NO_SUCH_ATTRIBUTE ) {
                log.log(Level.SEVERE, "Error: No such attribute",e);
            } else {
                log.log(Level.SEVERE, "Error while authenticating",e);
            }
            throw new LoginException("Error while searching for user profile.");
        } finally {
            try {
                conn.disconnect();
            } catch (LDAPException e) {
                log.log(Level.SEVERE, "Error disconnecting from LDAP server. ",e);
                e.printStackTrace();
            }
            loginDN = null;
            password = null;
        }
        checker.reset(username);
        loginOk = true;
        log.fine("Login is successful.");
        return loginOk;
    }

    @SuppressWarnings("rawtypes")
	public boolean commit() {
        if(loginOk){

            UserPrincipal user = new UserPrincipal(subject_username);

            LDAPAttribute attribute;
            StringBuffer buf = new StringBuffer();
            attribute = userprof.getAttribute(attributes[0]);
            if(attribute!=null){
                buf.append(attribute.getStringValue());
                buf.append(" ");
            }
            attribute = userprof.getAttribute(attributes[1]);
            if(attribute!=null)
                buf.append(attribute.getStringValue());
            NamePrincipal name = buf.length()==0?new NamePrincipal("NO NAME"): new NamePrincipal(buf.toString());
            
            attribute = userprof.getAttribute(attributes[2]);
            EmailPrincipal email = (attribute == null)? new EmailPrincipal("NONE"): new EmailPrincipal(attribute.getStringValue());

            attribute = userprof.getAttribute(attributes[3]);
            UnitPrincipal unit = (attribute == null)? new UnitPrincipal("NONE"):new UnitPrincipal(attribute.getStringValue());

            attribute = userprof.getAttribute(attributes[4]);
            DeptPrincipal dept = (attribute == null)?new DeptPrincipal("NONE") :new DeptPrincipal(attribute.getStringValue());

            attribute = userprof.getAttribute(attributes[5]);
            BranchPrincipal branch =  (attribute == null)? new BranchPrincipal("NONE") :new BranchPrincipal(attribute.getStringValue());

            attribute = userprof.getAttribute(attributes[6]);
            PositionPrincipal position = (attribute == null)? new PositionPrincipal("NONE"): new PositionPrincipal(attribute.getStringValue());

            attribute = userprof.getAttribute(attributes[7]);
            GradePrincipal grade = (attribute == null)? new GradePrincipal("NONE"):new GradePrincipal(attribute.getStringValue());

            attribute = userprof.getAttribute(attributes[8]);
            StaffNumberPrincipal number = (attribute == null)? new StaffNumberPrincipal("NONE"): new StaffNumberPrincipal(attribute.getStringValue());

            Set<Principal> principalset = subject.getPrincipals();            
            if(use_first_pass){
        		log.fine("Fetching already existing roles group...");
				for (Iterator i = principalset.iterator(); i.hasNext();) {
					Object obj = i.next();
					if (obj instanceof Group && ((Group) obj).getName().equals(rolesgrpname)) {
						rolesgrp = (Group) obj;
                        log.fine("Found existing roles group: "+rolesgrp.getName());
						break;
					}
				}				
        	}
            
        	if(rolesgrp==null){
        		rolesgrp = new DirectoryRoles(rolesgrpname);
                principalset.add(rolesgrp);
        	}
        	
        	rolesgrp.addMember(user);            
            rolesgrp.addMember(name);
            rolesgrp.addMember(unit);
            rolesgrp.addMember(dept);
            rolesgrp.addMember(branch);
            rolesgrp.addMember(grade);
            rolesgrp.addMember(position);
            rolesgrp.addMember(email);
            rolesgrp.addMember(number);
            
            log.fine("Commiting "+user.getName()+" with profile:\n" +
                      "Name: " +name.getName()+"\n"+
                      "Unit: " +unit.getName()+"\n"+
                      "Department: " +dept.getName()+"\n"+
                      "Branch: " +branch.getName()+"\n"+
                      "Grade: " +grade.getName()+"\n"+
                      "Position: " +position.getName()+"\n"+
                      "E-mail: " +email.getName()+"\n"+
                      "Staff Number: " +number.getName()+"\n");
            
            for(int i=0;i<defaultroles.length;i++){
                rolesgrp.addMember(new OtherPrincipal(defaultroles[i]));
            }
            
            userprof = null;
            return true;           
        }
        return false;
    }
    
    @SuppressWarnings("rawtypes")
	public boolean abort() {
        if(loginOk){
            userprof = null;
            Set princ = subject.getPrincipals();
            princ.remove(rolesgrp);
            return true;
        }else
            return false;
    }

    @SuppressWarnings("rawtypes")
	public boolean logout() {
        if(loginOk){
            userprof = null;
            Set princ = subject.getPrincipals();
            princ.remove(rolesgrp);
            return true;
        }else
            return false;
    }

}