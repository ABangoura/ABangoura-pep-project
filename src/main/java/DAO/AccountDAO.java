package DAO;

import Model.Account;

// Interface to abstract table 'account' data access implementation.
public interface AccountDAO {
    
    public Account insertNewAccount(Account newAccount);
    
    public Account geAccountByID(int id);

    public Account updateAccount(Account newAccount);

    public boolean deleteAccountByID(int id);

}
