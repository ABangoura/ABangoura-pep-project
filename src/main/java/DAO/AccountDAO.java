package DAO;

import Model.Account;

// Interface to abstract table 'account' data access implementation.
public interface AccountDAO {
    
    public Account insertNewAccount(Account newAccount);
    
    public Account logAccountIn(Account account);

    public Account getAccount(String userName, String password);

}
