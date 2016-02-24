
package com.quakearts.webapp.security.auth.webserviceclient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "authenticator", targetNamespace = "http://security.jboss.quakearts.com/", wsdlLocation = "http://quakearts.com/login-service/login?wsdl")
public class Authenticator
    extends Service
{

    private final static URL AUTHENTICATOR_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.quakearts.webapp.security.auth.webserviceclient.Authenticator.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.quakearts.webapp.security.auth.webserviceclient.Authenticator.class.getResource(".");
            url = new URL(baseUrl, "http://quakearts.com/login-service/login?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://quakearts.com/login-service/login?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        AUTHENTICATOR_WSDL_LOCATION = url;
    }

    public Authenticator(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Authenticator() {
        super(AUTHENTICATOR_WSDL_LOCATION, new QName("http://security.jboss.quakearts.com/", "authenticator"));
    }

    /**
     * 
     * @return
     *     returns LoginBean
     */
    @WebEndpoint(name = "LoginBeanPort")
    public LoginBean getLoginBeanPort() {
        return super.getPort(new QName("http://security.jboss.quakearts.com/", "LoginBeanPort"), LoginBean.class);
    }

}
