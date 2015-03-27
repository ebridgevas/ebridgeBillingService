package com.ebridge.billingplatform;

import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.util.TransactionException;

import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public interface VoucherRecharge {

    public BalanceDTO recharge(String uuid, String mobileNumber, String rechargeVoucher)
            throws RemoteException, TransactionException;

}
