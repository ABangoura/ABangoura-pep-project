package Service;

import DAO.AccountDAOImplementation;
import Model.Account;

/**
 * Account service class. This class provides the functionalities that will allow us
 * to implement the business logic of our account entities.
 */
public class AccountService {
    private AccountDAOImplementation accountDAO;

    /**
     * No-arg constructor.
     * It will instantiate a new AccountDAO implementation.
     */
    public AccountService() {
        accountDAO = new AccountDAOImplementation();
    }
    
    /**
     * Constructor with accountDAO parameter.
     * @param accountDAO
     */
    public AccountService(AccountDAOImplementation accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Method to insert a new account into the database.
     * @param newAccount account to be inserted.
     * @return Account account that was inserted (if successful), null otherwise.
     */
    public Account register(Account newAccount) {
        // Before registering an account, check that it does not exist, 
        // as well as the username and password constraints.
        if((accountDAO.getAccountByUsername(newAccount.getUsername()) == null) &&
            (!newAccount.getUsername().isBlank()) && (newAccount.getPassword().length() >= 4)) {
                return accountDAO.register(newAccount); // If the constrains check out ok, then register a new account.
        } else {
            return null; // Failed to register account.
        }
    }

    /**
     * Method log a user in.
     * @param account user's account object. Contains username and password.
     * @return Account the account that logged in, if login was successful.
     */
    public Account login(Account account) {
        return accountDAO.login(account);
    }

    /**
     * Method to get an account given a username.
     * @param username
     * @return
     */
    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }

}
