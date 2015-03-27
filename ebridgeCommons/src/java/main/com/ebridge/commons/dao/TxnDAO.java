package com.ebridge.commons.dao;

import com.ebridge.commons.dto.TxnDto;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


/**
 * @author david@tekeshe.com
 *
 */
public class TxnDAO {

    private static Connection connection;

    static {
        try {
            connection = DataBaseConnectionPool.getConnection();
        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }
    }

    public static void persist(TxnDto txn) {

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql = " INSERT INTO txns (uuid, source_id, destination_id, delivery_channel, transaction_date," +
                "                   transaction_type, product_code, amount, status_code, narrative," +
                "                   short_message, account_type, source_balance, beneficiary_balance," +
                "                   payment_method ) " +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setLong(  1, txn.getUuid().longValue());
            stmt.setString(2, txn.getSourceId());
            stmt.setString(3, txn.getDestinationId());
            stmt.setString(4, txn.getDeliveryChannel());
            stmt.setTimestamp(5, new Timestamp(txn.getTransactionDate().getTime()));
            stmt.setString(6, txn.getTransactionType());
            stmt.setString(7, txn.getProductCode());
            stmt.setBigDecimal(8, txn.getAmount());
            stmt.setString(9, txn.getStatusCode());
            stmt.setString(10,txn.getNarrative());
            stmt.setString(11, txn.getShortMessage());
            stmt.setString(12,txn.getAccountType());
            stmt.setBigDecimal( 13, txn.getSourceBalance() );
            stmt.setBigDecimal( 14, txn.getBeneficiaryBalance() );
            stmt.setString(15, txn.getPaymentMethod() );

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
        }

    }

    public static void persistFeedback(String mobileNumber, String feedback) {

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql = " INSERT INTO outbound_msg (message_id, message_status, source_id, payload, channel_type)" +
                " VALUES ( ?, ?, ?, ?, ? )";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setLong(  1, System.currentTimeMillis());
            stmt.setString(2, "RECEIVED");
            stmt.setString(3, mobileNumber);
            stmt.setString(4, feedback);
            stmt.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
        }

    }

    public boolean isLimitReached(TxnDto txn, BigDecimal limit) {

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql = " INSERT INTO txns (uuid, source_id, destination_id, delivery_channel, transaction_date," +
                "                   transaction_type, product_code, amount, status_code, narrative," +
                "                   short_message ) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        sql = "SELECT sum(amount) as usageValue FROM txns WHERE source_id = ? AND transaction_date >= ?  AND " +
                " transaction_type = 'DataBundlePurchase' AND status_code = '000' ";

        System.out.println("SQL::" + sql);
        System.out.println("LIMIT::" + limit);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, txn.getSourceId());
            stmt.setTimestamp(2, getBillingCycleStartDate() );

            rs = stmt.executeQuery();
            if (rs.next()) {

                try {
                    System.out.println("Usage + Amount::" + rs.getBigDecimal("usageValue").add(txn.getAmount()));
                    return limit.compareTo(rs.getBigDecimal("usageValue").add(txn.getAmount())) == -1;
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("########## FATAL - Failed to check credit limit for " + txn.getSourceId() + " : " + e.getMessage() );
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
            try {rs.close();} catch (Exception e){}
        }
        return false;
    }

    public static Set<TxnDto> history( String mobileNumber ) {

        Set<TxnDto> history = new TreeSet<>();

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql =
              " select uuid, transaction_date, transaction_type, source_id, " +
              " destination_id, product_code, amount, status_code, " +
              " source_balance, beneficiary_balance, delivery_channel, account_type," +
                      " payment_method " +
              "   from txns where source_id = ? or destination_id = ?  " +
              "  order by transaction_date desc ";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, mobileNumber);
            stmt.setString(2, mobileNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {

                try {
                    TxnDto txn = new TxnDto( new BigInteger(
                                                    rs.getString("uuid")),
                                                    rs.getString("delivery_channel"),
                                                    rs.getString("source_id"),
                                                    rs.getString("delivery_channel"),
                                                    rs.getTimestamp("transaction_date"));

                    txn.setDestinationId( rs.getString("destination_id") );
                    txn.setProductCode( rs.getString("product_code"));
                    txn.setAmount(rs.getBigDecimal("amount"));
                    txn.setTransactionType(rs.getString("transaction_type"));
                    txn.setStatusCode("000");
                    txn.setAccountType(rs.getString("account_type"));
                    txn.setSourceBalance( rs.getBigDecimal("source_balance") );
                    txn.setBeneficiaryBalance( rs.getBigDecimal("beneficiary_balance"));
                    txn.setPaymentMethod( rs.getString("payment_method"));
                    history.add( txn );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
            try {rs.close();} catch (Exception e){}
        }
        return history;
    }

    protected Timestamp getBillingCycleStartDate() {

        DateTime startFrom = new DateTime();

        DateTime today = new DateTime();

        if (today.getDayOfMonth() <= 25 ) {
            startFrom = startFrom.minusMonths(1);
        }

        startFrom = startFrom.withDayOfMonth(25).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);

        return new Timestamp(startFrom.toDate().getTime());
    }

    public static void main(String[] args) {
//        TxnDto txn = new TxnDto(new BigInteger("" + System.currentTimeMillis()),"000","263733803480","manual", new java.util.Date());
//        TxnDAO.persist(txn);
        TxnDAO.persistFeedback("263733803480", "Test comment");
    }
}
