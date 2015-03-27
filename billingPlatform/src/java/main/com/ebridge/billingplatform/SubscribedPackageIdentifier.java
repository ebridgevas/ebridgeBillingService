package com.ebridge.billingplatform;

import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public interface SubscribedPackageIdentifier {
    public String subscribedPackage(String mobileNumber) throws RemoteException;
}
