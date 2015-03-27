package com.ebridge.billingplatform.impl.prepaid.processors;

import com.comverse_in.prepaid.ccws.BalanceCreditAccount;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.billingplatform.AccountBalanceFactory;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.util.TransactionException;
import com.google.inject.Provider;

import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * @author david@tekeshe.com
 */
public class TelecashDebitRequestProcessor {

    public static BalanceCreditAccount[] process(
            AccountBalanceFactory prepaidAccountBalanceFactory,
            Provider<ServiceSoap> prepaidServiceSoapProvider,
            String uuid, String sourceId, DataBundleDTO dataBundle, String beneficiaryId,
            Calendar expirationDate)
                throws RemoteException, TransactionException {

        return null;
    }
}
