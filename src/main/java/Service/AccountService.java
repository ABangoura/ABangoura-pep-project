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
    public Account register(Account newAccount) {
        if((accountDAO.getAccountByUsername(newAccount.getUsername()) == null) &&
            (!newAccount.getUsername().isBlank()) && (newAccount.getPassword().length() >= 4))
            return accountDAO.register(newAccount);

        return null;
    }

    public Account login(Account account) {
        return accountDAO.login(account);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }

}
