package com.ebridge.billingplatform.impl.prepaid;

import com.comverse_in.prepaid.ccws.BalanceCreditAccount;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.billingplatform.AccountBalanceFactory;
import com.ebridge.billingplatform.BalanceTransfer;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.util.BalanceTransferReversalFailedException;
import com.ebridge.commons.util.ConfigurationService;
import com.ebridge.commons.util.TransactionException;
import com.ebridge.services.payment.TelecashPaymentService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Category;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.Map;

import static java.util.UUID.randomUUID;
import static com.ebridge.billingplatform.util.Util.balance;

/**
 * @author david@tekeshe.com
 */
public class PrepaidBalanceTransfer implements BalanceTransfer {

    @Inject
    @Named("prepaidServiceSoapProvider")
    private Provider<ServiceSoap> prepaidServiceSoapProvider;

    @Inject
    @Named("prepaidAccountBalanceFactory")
    private AccountBalanceFactory prepaidAccountBalanceFactory;

    private TelecashPaymentService telecashPaymentService;

    @Inject
    private ConfigurationService configurationService;

    private static final String CORE_BALANCE = "Core";

    private static final Category CAT = Category.getInstance(PrepaidBalanceTransfer.class);

    public PrepaidBalanceTransfer() {

        telecashPaymentService = new TelecashPaymentService();
    }

    @Override
    public BalanceDTO[] transfer(   String uuid,
                                    String mobileNumber,
                                    String beneficiaryId,
                                    BigDecimal amount,
                                    String paymentMethod,
                                    String oneTimePassword)
            throws RemoteException, TransactionException, BalanceTransferReversalFailedException {

        BalanceDTO source = balance(
                                    CORE_BALANCE, prepaidAccountBalanceFactory.balances( mobileNumber ) );
        BalanceDTO beneficiary = balance(
                CORE_BALANCE, prepaidAccountBalanceFactory.balances(beneficiaryId));

        Map<String, BigDecimal> params = configurationService.config().getBalanceTransferParameters();
        BigDecimal charge = params.get("balanceTransferCharge");
        CAT.info("Before Txn : " + source );
        CAT.info("Before Txn : " + beneficiary );
        CAT.info("Transfer charge : " + charge );

        // debit source
        BalanceCreditAccount[] sourcePayload = new BalanceCreditAccount[1];
        sourcePayload[0] = new BalanceCreditAccount();
        sourcePayload[0].setBalanceName( source.getBalanceName().getSystemValue() );
        sourcePayload[0].setCreditValue( 0.00 - amount.doubleValue() - charge.doubleValue() );
        sourcePayload[0].setExpirationDate( source.getExpiryDate() );

        // credit beneficiary
        BalanceCreditAccount[] beneficiaryPayload = new BalanceCreditAccount[1];
        beneficiaryPayload[0] = new BalanceCreditAccount();
        beneficiaryPayload[0].setBalanceName( beneficiary.getBalanceName().getSystemValue() );
        beneficiaryPayload[0].setCreditValue( amount.doubleValue() );
        beneficiaryPayload[0].setExpirationDate(beneficiary.getExpiryDate());

        // debit
        if (paymentMethod.equals( "AIRTIME" ) ) {

            // Debit Prepaid Account.

            prepaidServiceSoapProvider.get().creditAccount(
                    mobileNumber.substring(3),
                    null,
                    sourcePayload,
                    "", uuid);
        } else {

            // Debit Telecash

            String errorCode = null;
            String narrative =
                    telecashPaymentService.purchase(uuid,
                                                    mobileNumber,
                                                    oneTimePassword,
                                                    new BigDecimal( amount.doubleValue() +
                                                                    charge.doubleValue())
                                                                .setScale(2, RoundingMode.HALF_UP));

            System.out.println("Error: mobile-number : " + mobileNumber + ", error: " + errorCode +
                    ", narrative : " + narrative);

            if ( ! narrative.equalsIgnoreCase("Success")) {
                throw new TransactionException( narrative );
            }
        }

        try {
            // credit
            prepaidServiceSoapProvider.get()
                    .creditAccount(
                            beneficiary.getMobileNumber().substring(3),
                            null,
                            beneficiaryPayload,
                            "",uuid);
                            // "Transfer from : " +  source.getMobileNumber().substring(3) + ". Ref: " + uuid );

        } catch ( RemoteException remoteException ) {

            if ( "TELECASH".equalsIgnoreCase( paymentMethod ) ) {
                Boolean voided = telecashPaymentService.voidTransaction(
                        randomUUID().toString().replaceAll("-", "").toUpperCase(), uuid);
                throw new TransactionException("Transaction not completed. " +
                        ( voided ? "Your money was refunded." : "Will notify you after refunding your money."));

            } else {
                // Reverse the debit
                try {
                    sourcePayload[0].setCreditValue(0.00 - (amount.doubleValue() + charge.doubleValue()));
                    prepaidServiceSoapProvider.get()
                            .creditAccount(
                                    source.getMobileNumber().substring(3),
                                    null,
                                    sourcePayload,
                                    "", uuid);
                    // "Transfer to : " + beneficiary.getMobileNumber().substring(3) + ". Ref: " + uuid );
                } catch (RemoteException e) {
                    // FATAL. Failed to reverse the debit.
                    throw new BalanceTransferReversalFailedException(uuid, mobileNumber, beneficiaryId, amount, charge);
                }

                throw remoteException;
            }
        }

        source.setBalance(source.getBalance().subtract(amount).subtract(charge).setScale(2, RoundingMode.HALF_UP));
        beneficiary.setBalance(beneficiary.getBalance().add(amount).setScale(2, RoundingMode.HALF_UP));

        BalanceDTO[] result = new BalanceDTO[]{source, beneficiary};
        CAT.info("{result: " + result + "}");

        return result;
    }
}
