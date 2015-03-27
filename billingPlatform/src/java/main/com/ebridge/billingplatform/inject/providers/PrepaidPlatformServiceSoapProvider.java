package com.ebridge.billingplatform.inject.providers;

import com.comverse_in.prepaid.ccws.ServiceLocator;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.message.token.UsernameToken;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author david@tekeshe.com
 *
 * TODO inject test ip address / port : 172.17.1.28:8080
 * TODO inject live ip address / port : 172.17.1.19:8080
 * TODO inject file provider
 * TODO Persistent connection
 *
 */
@Singleton
public class PrepaidPlatformServiceSoapProvider  implements Provider<ServiceSoap> {

    @Override
    public ServiceSoap get() {
        EngineConfiguration config = new FileProvider("/prod/ebridge/wsdd/client_deploy_v2.wsdd");
        ServiceLocator locator = new ServiceLocator(config);
        ServiceSoap prepaidService = null;
        try {
            prepaidService = locator.getServiceSoap(
                    new URL("http://172.17.1.19:8080/ocswebservices/services/zimbabweocsWebServices?wsdl"));
            Stub axisPort = (Stub) prepaidService;
            axisPort._setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT);
            axisPort._setProperty(UsernameToken.PASSWORD_TYPE, WSConstants.PASSWORD_TEXT);
            axisPort._setProperty(WSHandlerConstants.USER, "zsmart2");
            axisPort._setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, "PasswordCallback");

            return prepaidService;

        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
