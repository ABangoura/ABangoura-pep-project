package Service;

import DAO.AccountDAOImplementation;
import Model.Account;

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
    public Account insertNewAccount(Account newAccount) {
        if(accountDAO.getAccount(newAccount.getUsername(), newAccount.getPassword()) != null)
            return accountDAO.insertNewAccount(newAccount);

        return null;
    }

    public Account logAccountIn(Account account) {
        return accountDAO.logAccountIn(account);
    }

}
