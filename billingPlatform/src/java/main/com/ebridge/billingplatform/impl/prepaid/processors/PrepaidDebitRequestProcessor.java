package com.ebridge.billingplatform.impl.prepaid.processors;

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

import static com.ebridge.billingplatform.util.Util.balance;

/**
 * @author david@tekeshe.com
 */
public class PrepaidDebitRequestProcessor {

    private static Category CAT = Category.getInstance(PrepaidDebitRequestProcessor.class);

    public static BalanceCreditAccount[] process(
            AccountBalanceFactory prepaidAccountBalanceFactory,
            Provider<ServiceSoap> prepaidServiceSoapProvider,
            String uuid, String sourceId, DataBundleDTO dataBundle, String beneficiaryId,
            Calendar expirationDate, BalanceDTO[] result)
            throws RemoteException, TransactionException {

        BalanceCreditAccount[] payload = new BalanceCreditAccount[1];
                /* --- Initial source and beneficiary balances --- */
        Set<BalanceDTO> sourceBalances = prepaidAccountBalanceFactory.balances( sourceId );
        BalanceDTO sourceCoreBalance = balance("Core", sourceBalances);
//        beneficiaryDataBalance = dataBalance( sourceBalances );
        payload[0] = new BalanceCreditAccount();
        payload[0].setBalanceName(sourceCoreBalance.getBalanceName().getSystemValue());
        payload[0].setCreditValue(0 - dataBundle.getDebit().doubleValue());
        payload[0].setExpirationDate( sourceCoreBalance.getExpiryDate() );


        CAT.debug("Parsing ...");
        prepaidServiceSoapProvider.get().creditAccount(
                sourceId.substring(3), null, payload, "", uuid); //"VAS Gw: Data Bundle Purchase Ref: " + uuid);

        result[0] = new BalanceDTO();
        result[0].setMobileNumber( sourceId );
        result[0].setBalance(sourceCoreBalance.getBalance()
                .subtract(dataBundle.getDebit()).setScale(2, RoundingMode.HALF_UP));
        result[0].setExpiryDate( payload[0].getExpirationDate() );
        result[0].setSubscriberPackage("PREPAID");


        return payload;
    }
}
