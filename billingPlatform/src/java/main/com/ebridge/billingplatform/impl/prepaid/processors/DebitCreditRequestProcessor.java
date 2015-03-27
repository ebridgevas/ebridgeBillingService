package com.ebridge.billingplatform.impl.prepaid.processors;

import static com.ebridge.billingplatform.util.Util.balance;
import static com.ebridge.billingplatform.util.Util.dataBalance;

import com.comverse_in.prepaid.ccws.BalanceCreditAccount;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.billingplatform.AccountBalanceFactory;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.util.TransactionException;
import com.google.inject.Provider;
import org.apache.log4j.Category;

import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Set;


/**
 * @author david@tekeshe.com
 */
public class DebitCreditRequestProcessor {

    private static Category CAT = Category.getInstance(DebitCreditRequestProcessor.class);

    public static Boolean process(
                AccountBalanceFactory prepaidAccountBalanceFactory,
                Provider<ServiceSoap> prepaidServiceSoapProvider,
            String uuid, String sourceId, DataBundleDTO dataBundle, String beneficiaryId,
            Calendar expirationDate, BalanceDTO[] result)
            throws RemoteException, TransactionException {

        BalanceCreditAccount[] payload = new BalanceCreditAccount[2];

        Set<BalanceDTO> sourceBalances = prepaidAccountBalanceFactory.balances( sourceId );
        BalanceDTO sourceCoreBalance = balance("Core", sourceBalances);
        payload[0] = new BalanceCreditAccount();
        payload[0].setBalanceName(sourceCoreBalance.getBalanceName().getSystemValue());
        payload[0].setCreditValue(0 - dataBundle.getDebit().doubleValue());
        payload[0].setExpirationDate( sourceCoreBalance.getExpiryDate() );

        CAT.debug("Debit : " + payload[0].getCreditValue() );
        System.out.println("Debit : " + payload[0].getCreditValue() );

        BalanceDTO beneficiaryDataBalance = dataBalance( sourceBalances );
        payload[1] = new BalanceCreditAccount();
        payload[1].setBalanceName(beneficiaryDataBalance.getBalanceName().getSystemValue());
        payload[1].setCreditValue(dataBundle.getCredit().doubleValue());
        payload[1].setExpirationDate(
                expirationDate.after(beneficiaryDataBalance.getExpiryDate())
                        ? expirationDate : beneficiaryDataBalance.getExpiryDate());

        CAT.debug("Credit : " + payload[1].getCreditValue() );
        System.out.println("Credit : " + payload[1].getCreditValue() );

        CAT.debug("Parsing ...");
        prepaidServiceSoapProvider.get().creditAccount(
                sourceId.substring(3), null, payload, "", uuid); //"VAS Gw: Data Bundle Purchase Ref: " + uuid);

        result[0] = new BalanceDTO();
        result[0].setMobileNumber( sourceId );
        result[0].setBalance(sourceCoreBalance.getBalance()
                .subtract(dataBundle.getDebit()).setScale(2, RoundingMode.HALF_UP));
        result[0].setExpiryDate( payload[0].getExpirationDate() );
        result[0].setSubscriberPackage("PREPAID");

        result[1] = new BalanceDTO();
        result[1].setMobileNumber( beneficiaryId );
        result[1].setBalance(( beneficiaryDataBalance.getBalance())
                .add((dataBundle.getCredit().divide(
                        dataBundle.getConsumptionTariff(), 2, RoundingMode.HALF_UP))
                        .setScale(2, RoundingMode.HALF_UP)));
        result[1].setExpiryDate( payload[1].getExpirationDate());
        result[1].setSubscriberPackage("PREPAID");

        return Boolean.TRUE;
    }
}
