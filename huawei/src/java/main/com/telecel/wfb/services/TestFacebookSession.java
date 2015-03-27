/**
 * TestFacebookSession.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.telecel.wfb.services;

public interface TestFacebookSession extends java.rmi.Remote {
    public String purchaseBundleTelecash(String mobileNumber, String bundleType, Double bundleAmount) throws java.rmi.RemoteException;
    public String purchaseBundle(String mobileNumber, String bundleType, Double bundleAmount) throws java.rmi.RemoteException;
    public String optOut(String mobileNumber, String bundleType, Double bundleAmount) throws java.rmi.RemoteException;
    public String bonusBalanceEnquiry(String mobileNumber, String bundleType) throws java.rmi.RemoteException, Exception;
    public String creditPCRF(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String creditPCRFPostPaid(String arg0, String arg1, String arg2) throws java.rmi.RemoteException;
    public String getPcrfWalletName(String arg0) throws java.rmi.RemoteException;
    public String migrate(String arg0) throws java.rmi.RemoteException;
    public String migrateTest() throws java.rmi.RemoteException;
    public String creditPCRFNewSubscriber(String msisdn, String walletName, Double amountValue) throws java.rmi.RemoteException;
    public String creditPCRFNewSubscriberTest(String arg0, String arg1, Double arg2) throws java.rmi.RemoteException;
}
