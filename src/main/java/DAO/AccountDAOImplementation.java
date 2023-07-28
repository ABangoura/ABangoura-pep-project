package DAO;

import Model.*;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AccountDAOImplementation implements AccountDAO{
    private Connection connection;

    /**
     * Constructor for new AccountDAOImplementation
     * It will create a new connection to the database whenever instantiated.
     */
    public AccountDAOImplementation() {
        connection = ConnectionUtil.getConnection();
    }

    /**
     * Method to insert a new account into the database.
     * @param newAccount
     * @return Account account that was insersted.
     */
    public Account insertNewAccount(Account newAccount) {
        try {
            String sql = "INSERT INTO account VALUES(default, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, newAccount.getUsername());
            ps.setString(2, newAccount.getPassword());
            
            ResultSet rows = ps.executeQuery();
            if(rows.next()) {
                newAccount.setAccount_id(rows.getInt("account_id"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return newAccount;
    }

    /**
     * Method to log an account in.
     * @param account the account to be logged in.
     * @return boolean true if 'username' and 'password' match in the database, false otherwise.
     */
    public boolean logAccountIn(Account account) {
        try {
            String sql = "SELECT account.username, account.password FROM account";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                if((rs.getString("username") == account.getUsername() && (rs.getString("password")) == account.getPassword()))
                    return true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public Account getAccountByID(int id){

        try {
            String sql = "SELECT * FROM account WHERE account.account_id = ?"; // SQL statement to return an account by id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            // Retrieve data from 'rs' and return the account.
            if(rs.next()) {
                // Create a new Account object  and set its fields from 'rs'.
                Account account = new Account();
                account.setAccount_id(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));

                return account;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null; // No message was found by the specified 'id'.
    }

}
