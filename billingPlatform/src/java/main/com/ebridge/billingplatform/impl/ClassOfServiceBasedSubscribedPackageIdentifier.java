package com.ebridge.billingplatform.impl;

import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.billingplatform.SubscribedPackageIdentifier;
import com.ebridge.commons.util.ConfigurationService;
import com.google.inject.Inject;
import org.apache.log4j.Category;

import javax.inject.Named;
import javax.inject.Provider;
import java.rmi.RemoteException;

/**
 * @author david@tekeshe.com
 */
public class ClassOfServiceBasedSubscribedPackageIdentifier implements SubscribedPackageIdentifier {

    private static Category CAT = Category.getInstance(ClassOfServiceBasedSubscribedPackageIdentifier.class);

    @Inject
    @Named("prepaidServiceSoapProvider")
    private Provider<ServiceSoap> prepaidServiceSoapProvider;

    @Inject
    private ConfigurationService configurationService;

    @Override
    public String subscribedPackage(String mobileNumber) throws RemoteException {

        CAT.debug("subscribedPackage ( " + mobileNumber + ")");

        String cosName = null;

        try {
            cosName = prepaidServiceSoapProvider.get()
                    .retrieveSubscriberWithIdentityNoHistory(
                            mobileNumber.substring(3), null, 1)
                    .getSubscriberData().getCOSName();
            CAT.debug("{mobileNumber : " + mobileNumber + ", cosName : " + cosName + "}");
        } catch(Exception e) {
            e.printStackTrace();
        }
        String result = cosName == null
                                || configurationService.config().getPostpaidCos().contains( cosName)
                                        ? "POSTPAID" : "PREPAID";
        CAT.debug("{mobileNumber : " + mobileNumber + ", subscriberPackage : " + result + "}" );

        return result;
    }
}
