package com.ebridge.billingplatform;

import com.ebridge.commons.dto.BalanceDTO;

import java.rmi.RemoteException;
import java.util.Set;

/**
 * @author david@tekeshe.com
 */
public interface AccountBalanceFactory {

    public Set<BalanceDTO> balances(String mobileNumber) throws RemoteException;
}
