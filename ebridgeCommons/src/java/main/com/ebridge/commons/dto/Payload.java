package com.ebridge.commons.dto;

import java.math.BigDecimal;

/**
 * @author david@tekeshe.com
 */
public class Payload {

    private final String uuid;
    private final Integer sessionId;
    private String mobileNumber;
    private final String processingCode;
    private final String transactionType;

    private String beneficiaryId;
    private String destinationId;

    private String customerOneTimePassword;
    private String originalTransactionId;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal feeAmount;
    private BigDecimal taxAmount;
    private String batchId;

    private String responseCode;
    private String narrative;

    public Payload(
            String uuid,
            Integer sessionId,
            String processingCode,
            String transactionType) {

        this.uuid = uuid;
        this.sessionId = sessionId;
        this.processingCode = processingCode;
        this.transactionType = transactionType;
    }

    public String getUuid() {
        return uuid;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getCustomerOneTimePassword() {
        return customerOneTimePassword;
    }

    public void setCustomerOneTimePassword(String customerOneTimePassword) {
        this.customerOneTimePassword = customerOneTimePassword;
    }

    public String getOriginalTransactionId() {
        return originalTransactionId;
    }

    public void setOriginalTransactionId(String originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }
}
