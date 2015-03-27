package com.ebridge.billingplatform.impl.prepaid.processors;

import com.comverse_in.prepaid.ccws.BalanceCreditAccount;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.billingplatform.AccountBalanceFactory;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.util.TransactionException;
import com.ebridge.services.payment.TelecashPaymentService;
import com.google.inject.Provider;
import org.apache.log4j.Category;

import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static com.ebridge.billingplatform.util.Util.dataBalance;

/**
 * @author david@tekeshe.com
 */
public class CreditRequestProcessor {

    private static Category CAT = Category.getInstance(CreditRequestProcessor.class);

    public static Boolean process(
                String paymentMethod,
                AccountBalanceFactory prepaidAccountBalanceFactory,
                Provider<ServiceSoap> prepaidServiceSoapProvider,
                TelecashPaymentService telecashPaymentService,
                String uuid, String sourceId, DataBundleDTO dataBundle, String beneficiaryId,
                Calendar expirationDate,
                BalanceCreditAccount[] debitPayload, BalanceDTO[] result )
            throws RemoteException, TransactionException {

        BalanceDTO beneficiaryDataBalance = null;
        BalanceCreditAccount[] creditPayload = null;
        try {

            Set<BalanceDTO> destinationBalances = prepaidAccountBalanceFactory.balances( beneficiaryId );
            beneficiaryDataBalance = dataBalance( destinationBalances );
            creditPayload = new BalanceCreditAccount[1];
            creditPayload[0] = new BalanceCreditAccount();
            creditPayload[0].setBalanceName( beneficiaryDataBalance.getBalanceName().getSystemValue() );
            creditPayload[0].setCreditValue(dataBundle.getCredit().doubleValue());
            creditPayload[0].setExpirationDate(
                    expirationDate.after( beneficiaryDataBalance.getExpiryDate() )
                            ? expirationDate : beneficiaryDataBalance.getExpiryDate());

            CAT.debug("Crediting ...");
            System.out.println("Crediting ...");
            prepaidServiceSoapProvider.get().creditAccount(
                    beneficiaryId.substring(3), null, creditPayload, "", uuid); //"VAS Gw: Data Bundle Purchase Ref: " + uuid);
        } catch (Exception e) {
            e.printStackTrace();

            if ( "TELECASH".equalsIgnoreCase( paymentMethod ) ) {
                Boolean voided = telecashPaymentService.voidTransaction(
                        randomUUID().toString().replaceAll("-", "").toUpperCase(), uuid);
                throw new TransactionException("Transaction not completed. " +
                            ( voided ? "Your money was refunded." : "Will notify you after refunding your money."));

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
                throw new TransactionException("Transaction not completed.");
            }
        }

        result[1] = new BalanceDTO();
        result[1].setMobileNumber( beneficiaryId );
        result[1].setBalance(( beneficiaryDataBalance.getBalance())
                .add((dataBundle.getCredit().divide(
                        dataBundle.getConsumptionTariff(), 2, RoundingMode.HALF_UP))
                        .setScale(2, RoundingMode.HALF_UP)));
        result[1].setExpiryDate( creditPayload[0].getExpirationDate());
        result[1].setSubscriberPackage("PREPAID");

        System.out.println("Done ...");

        return Boolean.TRUE;
    }
}
