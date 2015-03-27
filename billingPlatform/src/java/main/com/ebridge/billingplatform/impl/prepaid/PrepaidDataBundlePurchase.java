package com.ebridge.billingplatform.impl.prepaid;

import com.comverse_in.prepaid.ccws.BalanceCreditAccount;
import com.comverse_in.prepaid.ccws.ServiceSoap;

import com.ebridge.billingplatform.AccountBalanceFactory;
import com.ebridge.billingplatform.DataBundlePurchase;
import com.ebridge.billingplatform.impl.prepaid.processors.CreditRequestProcessor;
import com.ebridge.billingplatform.impl.prepaid.processors.DebitCreditRequestProcessor;
import com.ebridge.billingplatform.impl.prepaid.processors.HuaweiCreditRequestProcessor;
import com.ebridge.billingplatform.impl.prepaid.processors.PrepaidDebitRequestProcessor;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.util.TransactionException;
import com.ebridge.services.payment.TelecashPaymentService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.telecel.wfb.services.TestFacebookSession;
import com.telecel.wfb.services.TestFacebookSessionServiceLocator;
import data.ws.obopay.com.ObopayWebServiceData;
import org.apache.axis2.client.ServiceClient;
import org.apache.log4j.Category;
import org.joda.time.DateTime;

import javax.inject.Named;
import javax.xml.rpc.ServiceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import static com.ebridge.billingplatform.util.Util.balance;
import static com.ebridge.billingplatform.util.Util.dataBalance;

/**
 * @author david@tekeshe.com
 */
public class PrepaidDataBundlePurchase implements DataBundlePurchase {

    private final static String configFilename = System.getProperty("ebridge.conf.path") + "/" + "service.config";

    private Map<String, String> config;


    private static Category CAT = Category.getInstance(PrepaidDataBundlePurchase.class);

    @Inject
    @Named("prepaidServiceSoapProvider")
    private Provider<ServiceSoap> prepaidServiceSoapProvider;

//    @Inject
//    @Named("telecashSoapProvider")
//    private Provider<ServiceSoap> telecashSoapProvider;

    private TelecashPaymentService telecashPaymentService;

    private TestFacebookSession dataBundleManagementService;

    private HuaweiCreditRequestProcessor huaweiCreditRequestProcessor;

    @Inject
    @Named("prepaidAccountBalanceFactory")
    private AccountBalanceFactory prepaidAccountBalanceFactory;


    public PrepaidDataBundlePurchase() {

        telecashPaymentService = new TelecashPaymentService();
        try {
            dataBundleManagementService = new TestFacebookSessionServiceLocator().getTestFacebookSessionPort();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        huaweiCreditRequestProcessor = new HuaweiCreditRequestProcessor();
    }

    @Override
    public BalanceDTO[] dataBundlePurchase(
                String uuid, String sourceId, DataBundleDTO dataBundle, String beneficiaryId,
                String paymentMethod, String oneTimePassword, String dataBundleServiceCommand )
            throws RemoteException, TransactionException {

        BalanceDTO[] result = new BalanceDTO[2];

        if ( "UN-SUBSCRIBE".equalsIgnoreCase( dataBundleServiceCommand ) ) {


            System.out.println("-----> {service-command: opt-out, mobile-number : " + sourceId +
                    ", data-bundle-type : " + dataBundle.getProductType() +
                    ", amount : " + dataBundle.getDebit().doubleValue() );

            result[0] = new BalanceDTO();
            result[0].setMobileNumber( beneficiaryId );
            result[0].setNarrative(
                    dataBundleManagementService.optOut(beneficiaryId,
                            dataBundle.getProductType(),
                            dataBundle.getDebit().doubleValue()));

            System.out.println("<----- {service-command: opt-out, mobile-number : " +
                    sourceId + ", data-bundle-type : " +
                    dataBundle.getProductType() +
                    ", amount : " + dataBundle.getDebit().doubleValue() + ", response : " + result[0].getNarrative() );

            return result;
        }

        Calendar expirationDate = Calendar.getInstance();
        expirationDate.setTime( new DateTime(expirationDate.getTime()).plusDays( dataBundle.getWindowSize()).toDate() );

        CAT.info("{dataBundlePurchase : { mobileNumber : " + sourceId +
                  ", debitValue : " + dataBundle.getDebit() +
                    ", creditValue : " + dataBundle.getCredit() + ", expirationDate : " +
                    String.format("%1$td/%1$tm/%1$tY", expirationDate ) + "}}");

        if (paymentMethod.equals( "AIRTIME" ) ) {

            Boolean ownPhone = sourceId.equals(beneficiaryId);

            if ("STANDARD".equalsIgnoreCase( dataBundle.getProductType())) {

                if (ownPhone) {

                    DebitCreditRequestProcessor.process(prepaidAccountBalanceFactory, prepaidServiceSoapProvider,
                            uuid, sourceId, dataBundle, beneficiaryId, expirationDate,
                            result);
                } else {

                    BalanceCreditAccount[] debitPayload
                            = PrepaidDebitRequestProcessor.process(
                            prepaidAccountBalanceFactory, prepaidServiceSoapProvider,
                            uuid, sourceId, dataBundle, beneficiaryId, expirationDate,
                            result);

                    CreditRequestProcessor.process(
                            paymentMethod,
                            prepaidAccountBalanceFactory, prepaidServiceSoapProvider,
                            telecashPaymentService,
                            uuid, sourceId, dataBundle, beneficiaryId, expirationDate,
                            debitPayload, result);
                }
            } else {

                BalanceCreditAccount[] debitPayload
                        = PrepaidDebitRequestProcessor.process(
                        prepaidAccountBalanceFactory, prepaidServiceSoapProvider,
                        uuid, sourceId, dataBundle, beneficiaryId, expirationDate,
                        result);

                 huaweiCreditRequestProcessor.process(
                        paymentMethod,
                        dataBundleManagementService,
                        prepaidServiceSoapProvider,
                        telecashPaymentService,
                        uuid, sourceId, dataBundle, beneficiaryId, expirationDate,
                        debitPayload, result);
            }

        } else {

            String errorCode = null;
            String narrative =
                    telecashPaymentService.purchase(
                            uuid, sourceId, oneTimePassword, dataBundle.getDebit());

            System.out.println("Error: mobile-number : " + sourceId + ", error: " + errorCode +
                    ", narrative : " + narrative);

            if ( ! narrative.equalsIgnoreCase("Success")) {
                throw new TransactionException( narrative );
            }

            if ("STANDARD".equalsIgnoreCase( dataBundle.getProductType())) {
                result[0] = new BalanceDTO();
                result[0].setMobileNumber(sourceId);
                result[0].setBalance(null);
                result[0].setExpiryDate(null);
                result[0].setSubscriberPackage("PREPAID");

                Boolean credited =
                        CreditRequestProcessor.process(
                                paymentMethod,
                                prepaidAccountBalanceFactory, prepaidServiceSoapProvider,
                                telecashPaymentService,
                                uuid, sourceId, dataBundle, beneficiaryId, expirationDate,
                                null, result);
            } else {

                result[0] = new BalanceDTO();
                result[0].setMobileNumber(sourceId);
                result[0].setBalance(null);
                result[0].setExpiryDate(null);
                result[0].setSubscriberPackage("PREPAID");

                huaweiCreditRequestProcessor.process(
                        paymentMethod,
                        dataBundleManagementService,
                        prepaidServiceSoapProvider,
                        telecashPaymentService,
                        uuid, sourceId, dataBundle, beneficiaryId, expirationDate,
                        null, result);
            }

        }

        CAT.info("{result: " + result + "}");

        return result;
    }
}
