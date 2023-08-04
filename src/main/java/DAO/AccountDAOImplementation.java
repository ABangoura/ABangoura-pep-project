package DAO;

import Model.*;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            String sql = "INSERT INTO account(username, password) VALUES(?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, newAccount.getUsername());
            ps.setString(2, newAccount.getPassword());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int accountId = (int) rs.getInt(1);
                return new Account(accountId, newAccount.getUsername(), newAccount.getPassword());
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method to log an account in.
     * @param account the account to be logged in.
     * @return boolean true if 'username' and 'password' match in the database, false otherwise.
     */
    public Account login(Account account) {
        try {
            String sql = "SELECT account.id, account.username, account.password FROM account WHERE account.username = ? AND account.password = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));

        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }


    @Override
    public Account getAccountByUsername(String username) {
        try{
            String sql = "SELECT * FROM account WHERE account.username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                // Create a new Account object  and set its fields from 'rs'.
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
