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
    public Account logAccountIn(Account account) {
        try {
            String sql = "SELECT account.username, account.password FROM account WHERE username = ? AND password = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                Account newAccount = new Account(rs.getString("username"), rs.getString("password" ));
                // newAccount.setUsername(rs.getString("username"));
                // newAccount.setPassword(rs.getString("password"));
                return newAccount;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public Account getAccount(String userName, String password){

        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?"; // SQL statement to return an account by id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            // Retrieve data from 'rs' and return the account.
            if(rs.next()) {
                // Create a new Account object  and set its fields from 'rs'.
                Account account = new Account();
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));

                return account;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
