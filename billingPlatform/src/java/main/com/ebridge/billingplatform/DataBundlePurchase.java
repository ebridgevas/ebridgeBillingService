package com.ebridge.billingplatform;

import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.util.TransactionException;

import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public interface DataBundlePurchase {

    public BalanceDTO[] dataBundlePurchase(
            String uuid, String mobileNumber, DataBundleDTO dataBundle, String beneficiaryId,
            String paymentMethod, String oneTimePassword, String dataBundleServiceCommand)
            throws RemoteException, TransactionException;

}
