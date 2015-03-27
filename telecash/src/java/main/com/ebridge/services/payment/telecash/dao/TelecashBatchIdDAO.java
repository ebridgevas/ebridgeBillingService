package com.ebridge.services.payment.telecash.dao;

import com.ebridge.commons.dao.DataBaseConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author david@tekeshe.com
 */
public class TelecashBatchIdDAO {

    private static Connection connection;

    static {
        try {
            connection = DataBaseConnectionPool.getConnection();
        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }
    }

    public static Integer batchId( Date paymentDate ) {

        String sql = "SELECT batch_id FROM telecash_batch_id WHERE payment_date = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(paymentDate.getTime()));
            rs = stmt.executeQuery();
            Integer batchId = null;
            if ( rs.next() ) {
                batchId = rs.getInt("batch_id") + 1;
                sql = "UPDATE telecash_batch_id SET batch_id = ?, payment_date =  ?";
                stmt.close();
                stmt = connection.prepareStatement( sql );
                stmt.setInt( 1, batchId );
                stmt.setDate(2, new java.sql.Date(paymentDate.getTime()));
                stmt.executeUpdate();
            } else {
                batchId = 1;
                sql = "DELETE FROM telecash_batch_id;" +
                        "INSERT INTO telecash_batch_id ( payment_date, batch_id ) " +
                        " VALUES (? , ?)";
                stmt.close();
                stmt = connection.prepareStatement( sql );
                stmt.setInt( 1, batchId );
                stmt.setDate(2, new java.sql.Date(paymentDate.getTime()));
                stmt.executeUpdate();
            }

            return batchId;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
        }
        return null;
    }

    /*
    create table telecash_batch_id (payment_date date, batch_id integer);
     */
}
