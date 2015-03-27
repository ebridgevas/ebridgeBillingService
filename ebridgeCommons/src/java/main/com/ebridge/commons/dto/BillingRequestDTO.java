package com.ebridge.commons.dto;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author david@tekeshe.com
 */
public class BillingRequestDTO {

    private final String uuid;
    private final String mobileNumber;
    private final String beneficiaryId;
    private final String productCode;
    private final BigDecimal debitValue;
    private final BigDecimal creditValue;
    private final Calendar expirationDate;
    private final String subscribedPackage;

    public BillingRequestDTO(
                                String uuid,
                                String mobileNumber,
                                String beneficiaryId,
                                String productCode,
                                BigDecimal debitValue,
                                BigDecimal creditValue,
                                Calendar expirationDate,
                                String subscribedPackage) {
        this.uuid = uuid;
        this.mobileNumber = mobileNumber;
        this.beneficiaryId = beneficiaryId;
        this.productCode = productCode;
        this.debitValue = debitValue;
        this.creditValue = creditValue;
        this.expirationDate = expirationDate;
        this.subscribedPackage = subscribedPackage;
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

    public String getProductCode() {
        return productCode;
    }

    public BigDecimal getDebitValue() {
        return debitValue;
    }

    public BigDecimal getCreditValue() {
        return creditValue;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public String getSubscribedPackage() {
        return subscribedPackage;
    }

    public String toString() {

        return "{\"BillingRequest\" : {\"uuid\" : " + uuid + ", \"mobileNumber\" : " + mobileNumber +
                ", \"beneficiaryId\" : " + beneficiaryId + ", \"productCode\" : " + productCode +
                ", \"debitValue\" : " + debitValue + ", \"creditValue\" : " + creditValue +
                ", \"expirationDate\" : " + ( expirationDate != null ? expirationDate.getTime() : "") +
                ", \"subscribedPackage\" : " + subscribedPackage + "}}";
    }
}
