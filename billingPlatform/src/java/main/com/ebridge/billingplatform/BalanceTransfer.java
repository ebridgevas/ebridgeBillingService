package com.ebridge.billingplatform;

import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.util.BalanceTransferReversalFailedException;
import com.ebridge.commons.util.TransactionException;

import java.math.BigDecimal;
import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public interface BalanceTransfer {

    public BalanceDTO[] transfer(
            String uuid,
            String mobileNumber,
            String beneficiaryId,
            BigDecimal amount,
            String paymentMethod,
            String oneTimePassword)
            throws RemoteException, TransactionException, BalanceTransferReversalFailedException;
}
