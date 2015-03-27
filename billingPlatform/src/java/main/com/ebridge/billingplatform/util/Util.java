package com.ebridge.billingplatform.util;

import com.ebridge.commons.dao.TxnDAO;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.DataBundleDTO;
import com.ebridge.commons.dto.LanguageDTO;
import com.ebridge.commons.dto.TxnDto;
import com.ebridge.commons.util.TransactionException;
import org.apache.log4j.Category;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author david@tekeshe.com
 */
public class Util {

    private static Category CAT = Category.getInstance(Util.class);

    public static String balanceString( String subscriberPackage,
                                        Set<BalanceDTO> balances) throws TransactionException {

        BigDecimal coreBalance = balance("Core", balances).getBalance();
        BigDecimal dataBalance = balance("Gprs_bundle", balances).getBalance();
        Date dataBalanceExpiryDate = balance("Gprs_bundle", balances).getExpiryDate().getTime();

        return ( "PREPAID".equalsIgnoreCase(subscriberPackage) ? "Airtime bal is " + coreBalance + "usd. " : "") +
                (dataBalance != null ?
                        ("Data Bundle is " + dataBalance +
                            "mb exp on " + new SimpleDateFormat("dd MMM yyyy").format( dataBalanceExpiryDate ))
                                : "Data Bundle is 0.00") ;
    }

    public static BalanceDTO balance(String balanceName, Set<BalanceDTO> balances) throws TransactionException {

        for ( BalanceDTO balance : balances ) {

            if (balanceName.equalsIgnoreCase(balance.getBalanceName().getSystemValue())) {
                return balance;
            }
        }

        for ( BalanceDTO balance : balances ) {

            if ("PostpaidCore".equalsIgnoreCase(balance.getBalanceName().getSystemValue())) {
                return balance;
            }
        }

        throw new TransactionException("Missing account.");
    }

    public static BalanceDTO dataBalance(Set<BalanceDTO> balances) throws TransactionException {

        for ( BalanceDTO balance : balances ) {
            CAT.debug("Balance Name: " + balance.getBalanceName().getSystemValue() +
                    "unit of measure : " + balance.getBalanceName().getUnitOfMeasure() );
            if ("megabyte".equalsIgnoreCase(balance.getBalanceName().getUnitOfMeasure())) {
                return balance;
            }
        }

        BalanceDTO result = new BalanceDTO();

        result.setBalanceName( new LanguageDTO("Gprs_bundle", "Data Account", "TEL_COS",2, "megabyte") );
        result.setBalance(BigDecimal.ZERO);
        result.setExpiryDate(Calendar.getInstance());

        return result;
//        throw new TransactionException("Missing account.");
    }

    public static String[] dataBundleResponse(  String subscriberPackage,
                                                BalanceDTO[] balances,
                                                DataBundleDTO dataBundle,
                                                String paymentMethod,
                                                String dataBundleServiceCommand )
            throws TransactionException {

        String[] result = null;
        if ( "UN-SUBSCRIBE".equalsIgnoreCase( dataBundleServiceCommand )) {
            result = new String[1];
            result[0] = balances[0].getNarrative();
            return result;
        }

        String beneficiaryMobileNumber = null;
        try {
            beneficiaryMobileNumber = balances[1].getMobileNumber();
        } catch(Exception e ){
        }

        Boolean ownPhone = true;
        if ( beneficiaryMobileNumber != null ) {
            ownPhone = balances[0].getMobileNumber().equals(beneficiaryMobileNumber);
        }

        result = new String[ ownPhone ? 1 : 2 ];

        if ("STANDARD".equalsIgnoreCase( dataBundle.getProductType())) {
            result[0] =
                    "You have bought the " + dataBundle.getBundleSize().setScale(2, RoundingMode.HALF_UP) + "mb bundle " +
                            (!ownPhone ? " for 0" + balances[1].getMobileNumber().substring(3) + "" : "") +
                            ("TELECASH".equalsIgnoreCase(paymentMethod) ?
                                    " from your Telecash account." :
                                    ("PREPAID".equalsIgnoreCase(subscriberPackage) ? ". Your main balance is now " +
                                            balances[0].getBalance() + "usd. " : "")) +
                            (ownPhone ?
                                    "Your data balance is " + balances[balances.length - 1].getBalance() +
                                            "mb exp on " +
                                            String.format("%1$td/%1$tm/%1$tY", balances[balances.length - 1].getExpiryDate()) : "");

            if (!ownPhone) {
                result[1] =
                        "You received " + dataBundle.getBundleSize().setScale(2, RoundingMode.HALF_UP) + "mb bundle " +
                                "from 0" + balances[0].getMobileNumber().substring(3) +
                                ". Your data balance is now " + balances[1].getBalance() +
                                "mb exp on " +
                                String.format("%1$td/%1$tm/%1$tY", balances[1].getExpiryDate());
            }
        } else {

            result[0] = balances[1].getNarrative() +
                ( ownPhone ? "" : " for 0" + balances[1].getMobileNumber().substring(3) );
//                    "You have subscribed to the " + dataBundle.getShortDescription() + " data bundle" +
//                            (!ownPhone ? " for 0" + balances[1].getMobileNumber().substring(3) + "" : "") +
//                            ("TELECASH".equalsIgnoreCase(paymentMethod) ?
//                                    " from your Telecash account." :
//                                    ". Your main balance is now " + balances[0].getBalance() + "usd. ");

            if (!ownPhone) {
                result[1] =
                        "You received " + dataBundle.getShortDescription() + " bundle subscription " +
                                "from 0" + balances[0].getMobileNumber().substring(3);
            }
        }
        return result;
    }

    public static String[] balanceTransferResponse( BalanceDTO[] balances, BigDecimal amount, String uuid,
                                                    String paymentMethod) {

        String[] result = new String[2];

        if ("AIRTIME".equalsIgnoreCase( paymentMethod )) {
            result[0] = "$" + amount + " transfer to 0" + balances[1].getMobileNumber().substring(3) + " accepted. " +
                    "Your balance is now: $" + balances[0].getBalance() +
                    ". Reference: " + uuid;
        } else {
            result[0] = "$" + amount + " purchase from Telecash account for 0" +
                    balances[1].getMobileNumber().substring(3) + " accepted. " +
                    (balances[0].getMobileNumber().equals( balances[1].getMobileNumber()) ?
                        "Airtime balance is now: $" + balances[1].getBalance()
                        : "") +
                    ". Reference: " + uuid;
        }

        result[1] = "$" + amount + " transfer from 0" + balances[0].getMobileNumber().substring(3) + " accepted. " +
                "Your balance is now: $" + balances[1].getBalance() +
                ". Reference: " + uuid;

        return result;
    }

    public static String[] voucherRechargeResponse( String uuid,
                                                    String mobileNumber,
                                                    BalanceDTO balance ) {

        String[] result = null;
        if ( mobileNumber.substring(3).equals( balance.getMobileNumber() ) ) {
            result = new String[1];

            result[0] = "$" + new DecimalFormat("###,##.00").format( balance.getOtherAmount().doubleValue() ) +
                            " recharge voucher accepted. Your balance is now " +
                            new DecimalFormat("###,##.00").format(balance.getBalance().doubleValue()) +
                            ". Reference: " + uuid;
        } else {
            result = new String[2];

            result[0] = "$" + new DecimalFormat("###,##.00").format( balance.getOtherAmount().doubleValue() ) +
                            " recharge voucher for 0" + balance.getMobileNumber() + " accepted." +
                            " Reference: " + uuid;

            result[1] = "$" + new DecimalFormat("###,##.00").format( balance.getOtherAmount().doubleValue() ) +
                            " recharge voucher from 0" + mobileNumber.substring(3) + " accepted. Your balance is now " +
                            new DecimalFormat("###,##.00").format(balance.getBalance().doubleValue());
        }

        return result;
    }

    public static void persistDataBundleResponse(   String uuid,
                                                    BalanceDTO[] balances,
                                                    DataBundleDTO dataBundle,
                                                    String paymentMethod ) {
        System.out.print("Persisting txn ...");
        TxnDto txn = new TxnDto(new BigInteger(uuid), "WEB", balances[0].getMobileNumber(), "WEB", new Date());
        txn.setDestinationId( balances[1 ].getMobileNumber() );
        txn.setProductCode(dataBundle.getBundleType());
        txn.setAmount(dataBundle.getDebit());
        txn.setTransactionType("DataBundlePurchase");
        txn.setStatusCode("000");
        txn.setAccountType(balances[0].getSubscriberPackage());
        txn.setSourceBalance(balances[0].getBalance());
        txn.setBeneficiaryBalance(balances[1].getBalance());
        txn.setPaymentMethod( paymentMethod );
        System.out.println(" mobile number : " + balances[0].getMobileNumber() + ", amount : " + dataBundle.getDebit());
        TxnDAO.persist(txn);
    }

    public static void persistBalanceTransferResponse(  BalanceDTO[] balances,
                                                        BigDecimal amount,
                                                        String uuid,
                                                        String paymentMethod) {

        TxnDto txn = new TxnDto(new BigInteger(uuid), "WEB", balances[0].getMobileNumber(), "WEB", new Date());
        txn.setDestinationId( balances[ 1 ].getMobileNumber());
        txn.setProductCode("AirtimeTransfer");
        txn.setAmount(amount);
        txn.setTransactionType("AirtimeTransfer");
        txn.setStatusCode("000");
        txn.setAccountType("PREPAID");
        txn.setSourceBalance( balances[ 0 ].getBalance() );
        txn.setBeneficiaryBalance( balances [ 1 ].getBalance() );
        txn.setPaymentMethod( paymentMethod );

        TxnDAO.persist(txn);
    }

    public static void persistVoucherRechargeResponse(  String uuid,
                                                        String mobileNumber,
                                                        BalanceDTO balance,
                                                        String paymentMethod) {

        TxnDto txn = new TxnDto(new BigInteger(uuid), "WEB", mobileNumber, "WEB", new Date());
        txn.setDestinationId( balance.getMobileNumber());
        txn.setProductCode("VoucherRecharge");
        txn.setAmount(balance.getOtherAmount());
        txn.setTransactionType("VoucherRecharge");
        txn.setStatusCode("000");
        txn.setAccountType("PREPAID");
        txn.setPaymentMethod( paymentMethod );

        Boolean ownPhone = mobileNumber.equals( balance.getMobileNumber() );
        txn.setSourceBalance( ownPhone ? balance.getBalance() : BigDecimal.ZERO );
        txn.setBeneficiaryBalance( balance.getBalance() );

        TxnDAO.persist(txn);
    }

    public static Set<TxnDto> transactionHistory( String mobileNumber, Map<String, DataBundleDTO> bundleDTOMap) {

        Set<TxnDto> history = TxnDAO.history(mobileNumber);

        for ( TxnDto txn : history ) {

            if ("DataBundlePurchase".equalsIgnoreCase(txn.getTransactionType())) {
                txn.setNarrative( bundleDTOMap.get(txn.getProductCode()).getShortDescription());
            } else {
                txn.setNarrative(txn.getProductCode());
            }
        }

        return history;
    }
}
