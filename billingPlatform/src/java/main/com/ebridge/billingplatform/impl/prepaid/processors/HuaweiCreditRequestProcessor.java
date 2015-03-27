package com.ebridge.billingplatform.impl.prepaid.processors;

import com.comverse_in.prepaid.ccws.BalanceCreditAccount;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.util.TransactionException;
import com.ebridge.services.payment.TelecashPaymentService;
import com.google.inject.Provider;
import com.telecel.wfb.services.TestFacebookSession;
import com.telecel.wfb.services.TestFacebookSessionServiceLocator;
import org.apache.log4j.Category;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;

import static java.util.UUID.randomUUID;

/**
 * @author david@tekeshe.com
 */
public class HuaweiCreditRequestProcessor {

    private static Category CAT = Category.getInstance(HuaweiCreditRequestProcessor.class);

    public static Boolean process (
                String paymentMethod,
                TestFacebookSession dataBundleManagementService,
                Provider<ServiceSoap> prepaidServiceSoapProvider,
                TelecashPaymentService telecashPaymentService,
                String uuid, String sourceId, DataBundleDTO dataBundle, String beneficiaryId,
                Calendar expirationDate,
                BalanceCreditAccount[] debitPayload, BalanceDTO[] result )
            throws RemoteException, TransactionException {

        BalanceDTO beneficiaryDataBalance = null;
        BalanceCreditAccount[] creditPayload = null;
        try {

//            Set<BalanceDTO> destinationBalances = prepaidAccountBalanceFactory.balances( beneficiaryId );
//            beneficiaryDataBalance = dataBalance( destinationBalances );
//            creditPayload = new BalanceCreditAccount[1];
//            creditPayload[0] = new BalanceCreditAccount();
//            creditPayload[0].setBalanceName( beneficiaryDataBalance.getBalanceName().getSystemValue() );
//            creditPayload[0].setCreditValue(dataBundle.getCredit().doubleValue());
//            creditPayload[0].setExpirationDate(
//                    expirationDate.after( beneficiaryDataBalance.getExpiryDate() )
//                            ? expirationDate : beneficiaryDataBalance.getExpiryDate());
//
//            CAT.debug("Crediting ...");
//            System.out.println("Crediting ...");
//            prepaidServiceSoapProvider.get().creditAccount(
//                    beneficiaryId.substring(3), null, creditPayload, "", uuid); //"VAS Gw: Data Bundle Purchase Ref: " + uuid);

            System.out.println("-----> {mobile-number : " + beneficiaryId + ", data-bundle-type : " + dataBundle.getProductType() +
                ", amount : " + dataBundle.getCredit().doubleValue() );

            String response =
                    dataBundleManagementService.purchaseBundleTelecash(beneficiaryId,
                        dataBundle.getProductType(),
                        dataBundle.getCredit().doubleValue());

            System.out.println("<----- {mobile-number : " + beneficiaryId + ", data-bundle-type : " + dataBundle.getProductType() +
                    ", amount : " + dataBundle.getCredit().doubleValue() + ", response : " + response );

            result[1] = new BalanceDTO();
            result[1].setMobileNumber( beneficiaryId );
            result[1].setBalance(null);
            result[1].setExpiryDate( null );
            result[1].setSubscriberPackage("PREPAID");
            result[1].setNarrative( response );

            if ( ! response.startsWith("You have bought the") ) {
                throw new TransactionException( response );
            }
        } catch (Exception e) {
            e.printStackTrace();

            if ( "TELECASH".equalsIgnoreCase( paymentMethod ) ) {
                Boolean voided = telecashPaymentService.voidTransaction(
                        randomUUID().toString().replaceAll("-", "").toUpperCase(), uuid);
//                throw new TransactionException("Transaction not completed. " +
//                            ( voided ? "Your money was refunded." : "Will notify you after refunding your money."));
                  throw e;
            } else {
                debitPayload[0].setCreditValue(dataBundle.getDebit().doubleValue());
                CAT.debug("Reversing ...");
                try {
                    prepaidServiceSoapProvider.get().creditAccount(
                            sourceId.substring(3), null, debitPayload, "", "R" + uuid); //"VAS Gw: Data Bundle Purchase Ref: " + uuid);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new TransactionException("A system error occurred");
                    // TODO Fatal log reversal failure
                }
//                throw new TransactionException("Transaction not completed.");
                throw e;
            }
        }

        System.out.println("Done ...");

        return Boolean.TRUE;
    }

    public static void main(String[] args) {

        System.out.println("running ... " );
        TestFacebookSession dataBundleManagementService = null;
        try {

            TestFacebookSessionServiceLocator locator = new TestFacebookSessionServiceLocator();
            HandlerRegistry register = locator.getHandlerRegistry();
            QName portName = new QName("http://192.1.1.55:8080/TestFacebookSessionService/TestFacebookSession?wsdl", "TestFacebookSessionPort");
            List handlerChain = register.getHandlerChain(portName);
            HandlerInfo hi = new HandlerInfo();
            hi.setHandlerClass(SOAPLogHandler.class);
            handlerChain.add(hi);

//            dataBundleManagementService = locator.getTestFacebookSessionPort();
            dataBundleManagementService = (TestFacebookSession)locator.getPort(portName, TestFacebookSession.class);
            String beneficiaryId = "263739852990";
            String bundleType = "WHATSAPP";
            double amount = 0.29;
            try {
                String response =
                        dataBundleManagementService.purchaseBundle(beneficiaryId,bundleType,amount);

                System.out.println("response : " + response );
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try { Thread.sleep(30000); } catch (Exception e){}

            try {
                String response =
                        dataBundleManagementService.optOut(beneficiaryId,bundleType,amount);

                System.out.println("response : " + response );
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
