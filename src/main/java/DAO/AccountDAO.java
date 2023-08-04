package DAO;

import Model.Account;

// Interface to abstract table 'account' data access implementation.
public interface AccountDAO {
    
    public Account register(Account newAccount);
    
    public Account login(Account account);

    public Account getAccountByUsername(String username);

}
