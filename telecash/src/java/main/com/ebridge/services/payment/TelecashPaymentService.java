package com.ebridge.services.payment;

import com.ebridge.commons.dto.PayDto;
import com.ebridge.commons.dto.Payload;
import com.ebridge.services.payment.telecash.dao.PayDAO;
import com.ebridge.services.payment.telecash.util.ObopayWebServiceDataFactory;
import com.ebridge.services.payment.telecash.util.SecurityHeaderInjector;
import com.ebridge.services.payment.telecash.ws.ObopayExternalWebServiceServiceStub;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.obopay.www.xml.oews.v1.*;
import com.obopay.www.xml.oews.v1.Process;
import data.ws.obopay.com.ObopayWebServiceData;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;

import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.ebridge.commons.util.JsonUtils.config;

/**
 * @author david@tekeshe.com
 */
public class TelecashPaymentService {


//    private final static String configFilename = System.getProperty("ebridge.conf.path") + "/" + "service.config";
    private final static String configFilename = "/prod/staging/conf/service.config";

    private Map<String, String> config;

    private ObopayExternalWebServiceServiceStub paymentService;
    private ServiceClient serviceClient;
    private ObopayWebServiceData cachedRequest;

    private Gson gson;

    public TelecashPaymentService() {
        try {
            config = config(configFilename).get("payment-system-interfaces").get("telecash");

            try {
                init();
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init() throws AxisFault,
            InvalidKeyException,
            NoSuchAlgorithmException,
            InvalidParameterSpecException,
            NoSuchPaddingException,
            UnsupportedEncodingException {

        paymentService = new ObopayExternalWebServiceServiceStub( config.get("target-endpoint") );

        serviceClient = paymentService._getServiceClient() ;

        SecurityHeaderInjector.injectSecurityHeaders(serviceClient,
                config.get("user-name"),
                config.get("password"),
                config.get("partner-id"),
                config.get("encryption-key"));

        cachedRequest = ObopayWebServiceDataFactory.serviceRequest(config);

        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public String process( String payloadJson ) throws RemoteException {



        Payload payload = new Gson().fromJson(payloadJson, Payload.class );


        ObopayWebServiceData request = cachedRequest; // TODO clone object

        request.setCustomerPhoneNumber(payload.getMobileNumber());
        request.setProcessorCode( payload.getProcessingCode() );
        request.setTransactionType( payload.getTransactionType() );
        request.setRequestId(payload.getUuid());

        if ("0026".equals(payload.getProcessingCode())) {

            request.setPaymentDetails1(payload.getCustomerOneTimePassword());
            request.setTxnAmount(payload.getDebitAmount());
            request.setFeeAmount(payload.getFeeAmount());
            request.setTaxAmount(payload.getTaxAmount());

        } else if ("0028".equals(payload.getProcessingCode())) {

            request.setPaymentDetails1( payload.getOriginalTransactionId() );
            request.setTxnAmount(payload.getCreditAmount());
            request.setFeeAmount(payload.getFeeAmount());
            request.setTaxAmount(payload.getTaxAmount());

        } else if ("0029".equals(payload.getProcessingCode())) {
            // 04032014MC111111_12
            request.setPaymentDetails1(new SimpleDateFormat("ddMMyyyy").format(new Date())
                    + "MC" + config.get("user-name") + "_" + payload.getBatchId() );
            // 15566|100
            request.setPaymentDetails2( payload.getOriginalTransactionId() + "|" +
                    payload.getCreditAmount().setScale(0, RoundingMode.HALF_UP));
        }

        Process process = new Process();
        process.setRequest(request);

        System.out.println("===> \n" + gson.toJson(request, ObopayWebServiceData.class));
        ObopayWebServiceData response = paymentService.process( process ).get_return();
        System.out.println("<=== \n" + gson.toJson(response, ObopayWebServiceData.class) );

        payload.setResponseCode(response.getErrorCode());
        payload.setNarrative(response.getRemark());

        return new Gson().toJson( payload );
    }

    public String purchase( String uuid, String mobileNumber, String oneTimePassword, BigDecimal txnAmount ) {

        ObopayWebServiceData request = cachedRequest; // TODO clone object

        request.setCustomerPhoneNumber( mobileNumber );
        request.setProcessorCode("0026");
        request.setTransactionType("500");
        request.setRequestId( uuid );

        request.setPaymentDetails1(oneTimePassword);
        request.setTxnAmount(txnAmount );
        request.setFeeAmount(new BigDecimal("1.00"));
        request.setTaxAmount(new BigDecimal("1.00"));

        Process process = new Process();
        process.setRequest(request);

        // Persist
        PayDAO.persist(PayDAO.from(request));

        System.out.println("===> \n" + gson.toJson(request, ObopayWebServiceData.class));
        ObopayWebServiceData response = null;
        try {
            response = paymentService.process( process ).get_return();

            // Update DB
            PayDAO.update( response );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("<=== \n" + gson.toJson(response, ObopayWebServiceData.class) );

        return response.getRemark();
    }

    public Boolean voidTransaction( String uuid, String originalPaymentUuid ) {

        // Original payment
        PayDto payDto = PayDAO.payDto( originalPaymentUuid );

        ObopayWebServiceData request = cachedRequest; // TODO clone object

        // Settlement
        request.setProcessorCode("0027");
        request.setTransactionType("501");
        request.setRequestId(uuid);
        request.setCustomerPhoneNumber(payDto.getLocalCustomerPhoneNumber());

        request.setTxnAmount( BigDecimal.ZERO );
        request.setFeeAmount(new BigDecimal("1.00"));
        request.setTaxAmount(new BigDecimal("1.00"));

        request.setPaymentDetails1( payDto.getLocalOpsTransactionId() );

        Process process = new Process();
        process.setRequest(request);

        System.out.println("===> \n" + gson.toJson(request, ObopayWebServiceData.class));
        ObopayWebServiceData response = null;
        try {
            PayDAO.persist( PayDAO.from( request ) );
            response = paymentService.process( process ).get_return();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("<=== \n" + gson.toJson(response, ObopayWebServiceData.class) );

        System.out.println("Settlement transaction failed : " +
                response.getErrorCode() + " / " + response.getRemark());
        PayDAO.update( response );
        return "0".equals( response.getTxnStatus() );
    }

//    public String refund( String uuid, String originalPaymentUuid ) {
//
//        // Original payment
//        PayDto payDto = PayDAO.payDto( originalPaymentUuid );
//
//        ObopayWebServiceData request = cachedRequest; // TODO clone object
//
//        // Settlement
//        request.setCustomerPhoneNumber( payDto.getLocalCustomerPhoneNumber() );
//        request.setProcessorCode("0029");
//        request.setTransactionType("503");
//        request.setRequestId( uuid );
//
//        request.setTxnAmount( BigDecimal.ZERO );
//        request.setFeeAmount(new BigDecimal("1.00"));
//        request.setTaxAmount(new BigDecimal("1.00"));
//
//        Date refundDate = new Date();
//
//        Integer batchId = TelecashBatchIdDAO.batchId( refundDate );
//        if ( batchId == null ) {
//            PayDto refundDto = PayDAO.from( request );
//            refundDto.setLocalErrorCode("201");
//            refundDto.setLocalRemark("Refund for #" + originalPaymentUuid + " failed. Batch Id generator failed.");
//            PayDAO.persist( refundDto );
//
//            return refundDto.getLocalRemark();
//        }
//
//        // 04032014MC111111_12
//        request.setPaymentDetails1(new SimpleDateFormat("ddMMyyyy").format( refundDate )
//                + "MC" + config.get("user-name") + "_" + batchId );
//
//        // 15566|100
//        request.setPaymentDetails2(payDto.getLocalOpsTransactionId() + "|" +
//                payDto.getLocalTxnAmount().setScale(2, RoundingMode.HALF_UP));
//
//        com.obopay.www.xml.oews.v1.Process process = new Process();
//        process.setRequest(request);
//
//        System.out.println("===> \n" + gson.toJson(request, ObopayWebServiceData.class));
//        ObopayWebServiceData response = null;
//        try {
//            response = paymentService.process( process ).get_return();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        System.out.println("<=== \n" + gson.toJson(response, ObopayWebServiceData.class) );
//
//        if ( "0".equals(response.getErrorCode())) {
//            System.out.println("Refunding ...");
//            refund( uuid, payDto );
//        } else {
//            System.out.println("Settlement transaction failed : " +
//                                response.getErrorCode() + " / " + response.getRemark());
//            PayDAO.update( response );
//            return response.getRemark();
//
//        }
//        return response.getRemark();
//    }

    private String refund(String uuid, PayDto payDto) {


        ObopayWebServiceData request = cachedRequest; // TODO clone object

        request.setCustomerPhoneNumber(payDto.getLocalCustomerPhoneNumber() );
        request.setProcessorCode( "0028" );
        request.setTransactionType( "502" );
        request.setRequestId( uuid );

        request.setPaymentDetails1( payDto.getLocalOpsTransactionId() );
        request.setTxnAmount(payDto.getLocalTxnAmount() );
        request.setFeeAmount(payDto.getLocalFeeAmount());
        request.setTaxAmount(payDto.getLocalTaxAmount());

        Process process = new Process();
        process.setRequest(request);

        System.out.println("===> \n" + gson.toJson(request, ObopayWebServiceData.class));
        ObopayWebServiceData response = null;
        try {

            response = paymentService.process( process ).get_return();
            System.out.println("<=== \n" + gson.toJson(response, ObopayWebServiceData.class) );

            return response.getRemark();
        } catch (RemoteException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
