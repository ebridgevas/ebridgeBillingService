package com.ebridge.commons.util;

import java.math.BigDecimal;

/**
 * @author david@tekeshe.com
 */
public class BalanceTransferReversalFailedException extends Exception {

    private final String uuid;
    private final String mobileNumber;
    private final String beneficiaryId;
    private final BigDecimal amount;
    private final BigDecimal txnCharge;

    public BalanceTransferReversalFailedException (
            String uuid,
            String mobileNumber,
            String beneficiaryId,
            BigDecimal amount,
            BigDecimal txnCharge) {

        this.uuid = uuid;
        this.mobileNumber = mobileNumber;
        this.beneficiaryId = beneficiaryId;
        this.amount = amount;
        this.txnCharge = txnCharge;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getTxnCharge() {
        return txnCharge;
    }
}
