package com.ebridge.billingplatform.impl;

import com.ebridge.billingplatform.*;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.util.BalanceTransferReversalFailedException;
import com.ebridge.commons.util.ConfigurationService;
import com.ebridge.commons.util.TransactionException;
import com.ebridge.messaging.SecurityTokenSender;
import com.ebridge.messaging.model.OutboundMsg;
import com.google.inject.Inject;

import javax.inject.Named;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

/**
 * @author david@tekeshe.com
 */
public class BillingPlatformInterfaceImpl implements BillingPlatformInterface {

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private SubscribedPackageIdentifier subscribedPackageIdentifier;

    @Inject
    @Named("prepaidAccountBalanceFactory")
    private AccountBalanceFactory prepaidAccountBalanceFactory;

    @Inject
    @Named("postpaidAccountBalanceFactory")
    private AccountBalanceFactory postpaidAccountBalanceFactory;

    @Inject
    @Named("prepaidDataBundlePurchase")
    private DataBundlePurchase prepaidDataBundlePurchase;

    @Inject
    @Named("postpaidDataBundlePurchase")
    private DataBundlePurchase postpaidDataBundlePurchase;

    @Inject
    @Named("prepaidBalanceTransfer")
    private BalanceTransfer prepaidBalanceTransfer;

    @Inject
    @Named("prepaidVoucherRecharge")
    private VoucherRecharge prepaidVoucherRecharge;

    @Inject
    @Named("messageSender")
    private SecurityTokenSender messageSender;

    @Override
    public Map<String, DataBundleDTO> dataBundleList() {
        return configurationService.config().getDataBundles();
    }

    @Override
    public String subscribedPackage( String mobileNumber ) throws RemoteException {
        return subscribedPackageIdentifier.subscribedPackage( mobileNumber );
    }

    @Override
    public Set<BalanceDTO> balances( String mobileNumber ) throws RemoteException {

        return "PREPAID".equalsIgnoreCase( subscribedPackage( mobileNumber ) ) ?
                    prepaidAccountBalanceFactory.balances( mobileNumber )
                        : postpaidAccountBalanceFactory.balances( mobileNumber );
    }

    @Override
    public BalanceDTO[] dataBundlePurchase( String uuid, String mobileNumber, String productCode, String beneficiaryId,
                                            String paymentMethod, String oneTimePassword,
                                            String dataBundleServiceCommand )
            throws RemoteException, TransactionException {

        DataBundleDTO dataBundle = configurationService.config().getDataBundles().get(productCode);

        if ( "PREPAID".equalsIgnoreCase( subscribedPackage( mobileNumber ) ) ) {

            if ( ! "PREPAID".equalsIgnoreCase( subscribedPackage( beneficiaryId )  ) ) {
                throw new TransactionException("This service is for prepaid subscribers only.");
            }

            return prepaidDataBundlePurchase.dataBundlePurchase(
                    uuid, mobileNumber, dataBundle, beneficiaryId, paymentMethod, oneTimePassword,
                    dataBundleServiceCommand);
        } else {

            if ( mobileNumber.equals( beneficiaryId ) ) {
                return postpaidDataBundlePurchase.dataBundlePurchase(
                            uuid, mobileNumber, dataBundle, mobileNumber, paymentMethod, oneTimePassword, null);
            } else {
                throw new TransactionException("You can only buy postpaid bundle for own use.");
            }

        }
    }

    @Override
    public BalanceDTO[] transfer(   String uuid,
                                    String mobileNumber,
                                    String beneficiaryId,
                                    BigDecimal amount,
                                    String paymentMethod,
                                    String oneTimePassword )
            throws RemoteException, BalanceTransferReversalFailedException, TransactionException {

        if ( ! "PREPAID".equalsIgnoreCase( subscribedPackage(mobileNumber) )
                && "AIRTIME".equalsIgnoreCase( paymentMethod ) )
            throw new TransactionException(
                    "Mobile number 0" + mobileNumber.substring(3) + " is not on prepaid package");

        if ( ! "PREPAID".equalsIgnoreCase( subscribedPackage(beneficiaryId) ) )
            throw new TransactionException(
                    "Mobile number 0" + beneficiaryId.substring(3) + " is not on prepaid package");

        return prepaidBalanceTransfer.transfer(
                uuid, mobileNumber, beneficiaryId, amount, paymentMethod, oneTimePassword );
    }

    @Override
    public BalanceDTO recharge( String uuid, String beneficiaryId, String rechargeVoucher )
            throws RemoteException, TransactionException {

        if ( ! "PREPAID".equalsIgnoreCase( subscribedPackage(beneficiaryId) ) )
            throw new TransactionException(
                    "Mobile number 0" + beneficiaryId.substring(3) + " is not on prepaid package");

        return prepaidVoucherRecharge.recharge( uuid, beneficiaryId, rechargeVoucher );
    }

    @Override
    public void sendMessage(BigInteger uuid, String destinationAddress, String messagingAgent, String message)
            throws IOException {

        messageSender.send(
                new OutboundMsg(uuid, "QUEUED", messagingAgent, destinationAddress, "", message ) );
    }
}
