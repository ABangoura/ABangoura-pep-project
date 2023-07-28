package DAO;

import Model.Account;

// Interface to abstract table 'account' data access implementation.
public interface AccountDAO {
    
    public Account insertNewAccount(Account newAccount);
    
    public boolean logAccountIn(Account account);

    public Account getAccountByID(int id);

}
