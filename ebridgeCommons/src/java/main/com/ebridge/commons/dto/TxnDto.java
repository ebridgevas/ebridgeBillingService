package com.ebridge.commons.dto;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author david@tekeshe.com
 *
 */
public class TxnDto implements Comparable<TxnDto> {

    private final BigInteger uuid;
    private Integer sessionId;
    private final String shortCode;
    private final String sourceId;
    private final String deliveryChannel;
    private final Date transactionDate;

    private String transactionType;
    private String productCode;
    private String destinationId;
    private BigDecimal amount;
    private String statusCode;
    private String narrative;
    private String shortMessage;
    private String accountType;
    private BigDecimal sourceBalance;
    private BigDecimal beneficiaryBalance;

    private String paymentMethod;

    public TxnDto( BigInteger uuid,
            String shortCode,
            String sourceId,
            String deliveryChannel,
            Date transactionDate) {

        this.uuid = uuid;
        this.shortCode = shortCode;
        this.sourceId = sourceId;
        this.deliveryChannel = deliveryChannel;
        this.transactionDate = transactionDate;
    }

    public BigInteger getUuid() {
        return uuid;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDeliveryChannel() {
        return deliveryChannel;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getSourceBalance() {
        return sourceBalance;
    }

    public void setSourceBalance(BigDecimal sourceBalance) {
        this.sourceBalance = sourceBalance;
    }

    public BigDecimal getBeneficiaryBalance() {
        return beneficiaryBalance;
    }

    public void setBeneficiaryBalance(BigDecimal beneficiaryBalance) {
        this.beneficiaryBalance = beneficiaryBalance;
    }

    @Override
    public int compareTo(TxnDto o) {
        return transactionDate.after(o.getTransactionDate()) ? -1 : 1;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
