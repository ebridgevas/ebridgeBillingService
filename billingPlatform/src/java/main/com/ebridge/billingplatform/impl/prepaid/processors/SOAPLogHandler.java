package com.ebridge.billingplatform.impl.prepaid.processors;

/**
 * @author david@tekeshe.com
 */
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;


public class SOAPLogHandler extends GenericHandler{

    public QName[] getHeaders() {
// TODO Auto-generated method stub
        return null;
    }

    public boolean handleRequest(MessageContext context) {
        try {
            SOAPMessageContext smc = (SOAPMessageContext)context;
            SOAPMessage msg = smc.getMessage();
            System.out.println("request:");
            msg.writeTo(System.out);
            System.out.println("");

        } catch(Exception ex) {
            ex.printStackTrace();
//throw new RuntimeException( ex );
        }
        return true;
    }

    public boolean handleResponse(MessageContext context) {
        try {
            SOAPMessageContext smc = (SOAPMessageContext)context;
            SOAPMessage msg = smc.getMessage();
            System.out.println("response:");
            msg.writeTo(System.out);
            System.out.println("");

        } catch(Exception ex) {
            ex.printStackTrace();
//throw new RuntimeException( ex );
        }
        return true;
    }


}