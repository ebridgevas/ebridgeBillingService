package com.ebridge.commons.dto;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BalanceDTO implements Comparable<BalanceDTO> {

    private String mobileNumber;
    private LanguageDTO balanceName;
    private BigDecimal balance;
    private Calendar expiryDate;
    private BigDecimal otherAmount;
    private DateTime currentExpiryDate;
    private String subscriberPackage;
    private String narrative;

    public BalanceDTO() {
    }

    public BalanceDTO( String mobileNumber,
                       LanguageDTO balanceName,
                       BigDecimal balance,
                       Calendar expiryDate,
                        String subscriberPackage) {
        this.mobileNumber = mobileNumber;
        this.balanceName = balanceName;
        this.balance = balance;
        this.expiryDate = expiryDate;
        this.subscriberPackage = subscriberPackage;
    }

    public BalanceDTO(
                        String mobileNumber,
                        LanguageDTO balanceName,
                        BigDecimal balance,
                        Calendar expiryDate,
                        BigDecimal otherAmount,
                        DateTime currentExpiryDate ) {

        this.mobileNumber = mobileNumber;
        this.balanceName = balanceName;
        this.balance = balance;
        this.expiryDate = expiryDate;
        this.otherAmount = otherAmount;
        this.currentExpiryDate = currentExpiryDate;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LanguageDTO getBalanceName() {
        return balanceName;
    }

    public void setBalanceName(LanguageDTO balanceName) {
        this.balanceName = balanceName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Calendar getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Calendar expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }

    public DateTime getCurrentExpiryDate() {
        return currentExpiryDate;
    }

    public void setCurrentExpiryDate(DateTime currentExpiryDate) {
        this.currentExpiryDate = currentExpiryDate;
    }

    public String getSubscriberPackage() {
        return subscriberPackage;
    }

    public void setSubscriberPackage(String subscriberPackage) {
        this.subscriberPackage = subscriberPackage;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String toString() {
        return "{ balanceName : " + balanceName + ", balance : " + balance + ", expiryDate : " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( expiryDate.getTime() ) + "}";
    }

    @Override
    public int compareTo(BalanceDTO o) {
        System.out.println(" balanceName : " + balanceName);
        System.out.println(" balanceName.getIndex() : " + balanceName.getIndex());
        System.out.println(" o balanceName : " + o);
        System.out.println(" o.getBalanceName() : " + o.getBalanceName());
        return balanceName.getIndex().compareTo(o.getBalanceName().getIndex());
    }
}
