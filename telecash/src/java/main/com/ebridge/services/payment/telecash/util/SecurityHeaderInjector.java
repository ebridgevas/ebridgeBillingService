package com.ebridge.services.payment.telecash.util;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.client.ServiceClient;

/**
 * @author david@tekeshe.com
 */
public class SecurityHeaderInjector {

    public static void injectSecurityHeaders( ServiceClient serviceClient,
                                              String userName,
                                              String password,
                                              String partnerId,
                                              String currentIV )  {

        String nameSpace = "http://www.obopay.com/xml/oews/v1";
        String nameSpaceRef = "ns3";
        String soapHeaderBlockName = "process";

        // Add Headers
        SOAPFactory factory = OMAbstractFactory.getSOAP12Factory();
        OMNamespace omNamespace = factory.createOMNamespace(nameSpace, nameSpaceRef);

        //Add Initialization Vector - IV
        SOAPHeaderBlock ivHdr = factory.createSOAPHeaderBlock(soapHeaderBlockName, omNamespace );
        ivHdr.addAttribute("name", "iv", null);
        ivHdr.setText( currentIV );
        serviceClient.addHeader( ivHdr );

        // Add User Name
        SOAPHeaderBlock userNameHeader = factory.createSOAPHeaderBlock(soapHeaderBlockName, omNamespace );
        userNameHeader.addAttribute("name" ,"UserName" ,null);
        userNameHeader.setText( userName );
        serviceClient.addHeader(userNameHeader);

        // Add Password
        SOAPHeaderBlock passwordHeader = factory.createSOAPHeaderBlock(soapHeaderBlockName, omNamespace );
        passwordHeader.addAttribute("name" ,"Password" ,null);
        passwordHeader.setText(password);
        serviceClient.addHeader( passwordHeader );

        // Add Partner Code
        SOAPHeaderBlock partnerCodeHeader = factory.createSOAPHeaderBlock(soapHeaderBlockName, omNamespace );
        partnerCodeHeader.addAttribute("name" ,"PartnerId" ,null);
        partnerCodeHeader.setText( partnerId );
        serviceClient.addHeader(partnerCodeHeader);
    }
}
