package com.ebridge.commons.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author david@tekeshe.com
 */
public class PayDto {

    private final String localRequestId;
    private String localApiIdType;
    private String localTier1AgentId;
    private String localTier1AgentName;
    private String localTier2AgentId;
    private String localTier3AgentId;
    private String localTier1AgentPassword;
    private String localCustomerPhoneNumber;
    private String localNationalID;
    private String localSenderFirstName;
    private String localSenderLastName;
    private String localTransactionType;
    private String localInstrumentType;
    private String localProcessorCode;
    private BigDecimal localCashInAmount;
    private String localPaymentDetails1;
    private String localPaymentDetails2;
    private String localPaymentDetails3;
    private String localPaymentDetails4;
    private BigDecimal localTxnAmount;
    private String localCurrencyType;
    private BigDecimal localNetTxnAmount;
    private BigDecimal localFeeAmount;
    private BigDecimal localTaxAmount;
    private String localCountry;
    private String localTerminalID;
    private String localTxnStatus;
    private String localErrorCode;
    private String localOpsTransactionId;
    private Date localTransactionDate;
    private String localRemark;
    private String localReference1;
    private String localReference2;
    private String localReference3;
    private String localReference4;
    private String localReference5;

    public PayDto(String localRequestId) {
        this.localRequestId = localRequestId;
    }

    public String getLocalApiIdType() {
        return localApiIdType;
    }

    public void setLocalApiIdType(String localApiIdType) {
        this.localApiIdType = localApiIdType;
    }

    public String getLocalTier1AgentId() {
        return localTier1AgentId;
    }

    public void setLocalTier1AgentId(String localTier1AgentId) {
        this.localTier1AgentId = localTier1AgentId;
    }

    public String getLocalTier1AgentName() {
        return localTier1AgentName;
    }

    public void setLocalTier1AgentName(String localTier1AgentName) {
        this.localTier1AgentName = localTier1AgentName;
    }

    public String getLocalTier2AgentId() {
        return localTier2AgentId;
    }

    public void setLocalTier2AgentId(String localTier2AgentId) {
        this.localTier2AgentId = localTier2AgentId;
    }

    public String getLocalTier3AgentId() {
        return localTier3AgentId;
    }

    public void setLocalTier3AgentId(String localTier3AgentId) {
        this.localTier3AgentId = localTier3AgentId;
    }

    public String getLocalTier1AgentPassword() {
        return localTier1AgentPassword;
    }

    public void setLocalTier1AgentPassword(String localTier1AgentPassword) {
        this.localTier1AgentPassword = localTier1AgentPassword;
    }

    public String getLocalCustomerPhoneNumber() {
        return localCustomerPhoneNumber;
    }

    public void setLocalCustomerPhoneNumber(String localCustomerPhoneNumber) {
        this.localCustomerPhoneNumber = localCustomerPhoneNumber;
    }

    public String getLocalNationalID() {
        return localNationalID;
    }

    public void setLocalNationalID(String localNationalID) {
        this.localNationalID = localNationalID;
    }

    public String getLocalSenderFirstName() {
        return localSenderFirstName;
    }

    public void setLocalSenderFirstName(String localSenderFirstName) {
        this.localSenderFirstName = localSenderFirstName;
    }

    public String getLocalSenderLastName() {
        return localSenderLastName;
    }

    public void setLocalSenderLastName(String localSenderLastName) {
        this.localSenderLastName = localSenderLastName;
    }

    public String getLocalTransactionType() {
        return localTransactionType;
    }

    public void setLocalTransactionType(String localTransactionType) {
        this.localTransactionType = localTransactionType;
    }

    public String getLocalInstrumentType() {
        return localInstrumentType;
    }

    public void setLocalInstrumentType(String localInstrumentType) {
        this.localInstrumentType = localInstrumentType;
    }

    public String getLocalProcessorCode() {
        return localProcessorCode;
    }

    public void setLocalProcessorCode(String localProcessorCode) {
        this.localProcessorCode = localProcessorCode;
    }

    public BigDecimal getLocalCashInAmount() {
        return localCashInAmount;
    }

    public void setLocalCashInAmount(BigDecimal localCashInAmount) {
        this.localCashInAmount = localCashInAmount;
    }

    public String getLocalPaymentDetails1() {
        return localPaymentDetails1;
    }

    public void setLocalPaymentDetails1(String localPaymentDetails1) {
        this.localPaymentDetails1 = localPaymentDetails1;
    }

    public String getLocalPaymentDetails2() {
        return localPaymentDetails2;
    }

    public void setLocalPaymentDetails2(String localPaymentDetails2) {
        this.localPaymentDetails2 = localPaymentDetails2;
    }

    public String getLocalPaymentDetails3() {
        return localPaymentDetails3;
    }

    public void setLocalPaymentDetails3(String localPaymentDetails3) {
        this.localPaymentDetails3 = localPaymentDetails3;
    }

    public String getLocalPaymentDetails4() {
        return localPaymentDetails4;
    }

    public void setLocalPaymentDetails4(String localPaymentDetails4) {
        this.localPaymentDetails4 = localPaymentDetails4;
    }

    public BigDecimal getLocalTxnAmount() {
        return localTxnAmount;
    }

    public void setLocalTxnAmount(BigDecimal localTxnAmount) {
        this.localTxnAmount = localTxnAmount;
    }

    public String getLocalCurrencyType() {
        return localCurrencyType;
    }

    public void setLocalCurrencyType(String localCurrencyType) {
        this.localCurrencyType = localCurrencyType;
    }

    public BigDecimal getLocalNetTxnAmount() {
        return localNetTxnAmount;
    }

    public void setLocalNetTxnAmount(BigDecimal localNetTxnAmount) {
        this.localNetTxnAmount = localNetTxnAmount;
    }

    public BigDecimal getLocalFeeAmount() {
        return localFeeAmount;
    }

    public void setLocalFeeAmount(BigDecimal localFeeAmount) {
        this.localFeeAmount = localFeeAmount;
    }

    public BigDecimal getLocalTaxAmount() {
        return localTaxAmount;
    }

    public void setLocalTaxAmount(BigDecimal localTaxAmount) {
        this.localTaxAmount = localTaxAmount;
    }

    public String getLocalCountry() {
        return localCountry;
    }

    public void setLocalCountry(String localCountry) {
        this.localCountry = localCountry;
    }

    public String getLocalRequestId() {
        return localRequestId;
    }

    public String getLocalTerminalID() {
        return localTerminalID;
    }

    public void setLocalTerminalID(String localTerminalID) {
        this.localTerminalID = localTerminalID;
    }

    public String getLocalTxnStatus() {
        return localTxnStatus;
    }

    public void setLocalTxnStatus(String localTxnStatus) {
        this.localTxnStatus = localTxnStatus;
    }

    public String getLocalErrorCode() {
        return localErrorCode;
    }

    public void setLocalErrorCode(String localErrorCode) {
        this.localErrorCode = localErrorCode;
    }

    public String getLocalOpsTransactionId() {
        return localOpsTransactionId;
    }

    public void setLocalOpsTransactionId(String localOpsTransactionId) {
        this.localOpsTransactionId = localOpsTransactionId;
    }

    public Date getLocalTransactionDate() {
        return localTransactionDate;
    }

    public void setLocalTransactionDate(Date localTransactionDate) {
        this.localTransactionDate = localTransactionDate;
    }

    public String getLocalRemark() {
        return localRemark;
    }

    public void setLocalRemark(String localRemark) {
        this.localRemark = localRemark;
    }

    public String getLocalReference1() {
        return localReference1;
    }

    public void setLocalReference1(String localReference1) {
        this.localReference1 = localReference1;
    }

    public String getLocalReference2() {
        return localReference2;
    }

    public void setLocalReference2(String localReference2) {
        this.localReference2 = localReference2;
    }

    public String getLocalReference3() {
        return localReference3;
    }

    public void setLocalReference3(String localReference3) {
        this.localReference3 = localReference3;
    }

    public String getLocalReference4() {
        return localReference4;
    }

    public void setLocalReference4(String localReference4) {
        this.localReference4 = localReference4;
    }

    public String getLocalReference5() {
        return localReference5;
    }

    public void setLocalReference5(String localReference5) {
        this.localReference5 = localReference5;
    }
}
