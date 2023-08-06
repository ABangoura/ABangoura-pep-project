package DAO;

import Model.*;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to provide the functionalities to access and modify account related data.
 * Implements the Account Data Access Object interface.
 */
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
    public Account register(Account newAccount) {
        try {
            String sql = "INSERT INTO account(username, password) VALUES(?, ?)"; // SQL query to create a new account in the database.
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // Dabase will automatically assign account ids.
            ps.setString(1, newAccount.getUsername());
            ps.setString(2, newAccount.getPassword());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys(); // Retrieve the key that was automatically generated.
            if(rs.next()){ // If an id was generated...
                int accountId = (int) rs.getInt(1);
                return new Account(accountId, newAccount.getUsername(), newAccount.getPassword()); // ...return a new account with the generated id,
            }                                                                                      // and the usernae/password from the 'newAccount' parameter fields.

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null; // No account was registered.
    }

    /**
     * Method to log an account in.
     * @param account user's account object. Contains username and password.
     * @return Account the account that logged in, if login was successful.
     */
    public Account login(Account account) {
        try { // SQL query to match a given username/password (from 'account' parameter) to the ones in the database.
            String sql = "SELECT account.account_id, account.username, account.password FROM account WHERE account.username = ? AND account.password = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()) // If there was a match, return the account, including its id.
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));

        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return null; // No match was found.
    }

    /**
     * Method to get an account by the username.
     * @param username the username to look for int the table.
     * @return Account the account associated with the username.
     */
    @Override
    public Account getAccountByUsername(String username) {
        try{
            String sql = "SELECT * FROM account WHERE account.username = ?"; // Query to retrieve an account with the username provided.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) { // If there was a match...
                // ...create and return a new Account object  by getting its fields from 'rs'.
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null; // No account was found by the given username.
    }
}
