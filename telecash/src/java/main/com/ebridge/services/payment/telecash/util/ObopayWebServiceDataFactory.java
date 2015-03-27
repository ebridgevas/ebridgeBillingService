package com.ebridge.services.payment.telecash.util;

import data.ws.obopay.com.ObopayWebServiceData;

import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class ObopayWebServiceDataFactory {

    public static ObopayWebServiceData serviceRequest(Map<String, String> config) {

        ObopayWebServiceData request = new ObopayWebServiceData();

        request.setApiIdType(       config.get("api-id-type"));
        request.setCurrencyType(    config.get("currency-type"));
//        request.setPaymentDetails2( config.get("country-code"));
        request.setInstrumentType( config.get("instrument-type"));
//        request.setTransactionType( config.get("transaction-type"));
//        request.setProcessorCode(   config.get("processor-code"));
        request.setTier3AgentId(config.get("tier-3-agent-id"));
        request.setReference1(      config.get("reference1"));
        request.setReference2(      config.get("reference2"));
        request.setTerminalID(      config.get("terminal-id"));

        return request;
    }
}
