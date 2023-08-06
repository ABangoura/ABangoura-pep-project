package DAO;

import Model.Account;

// Interface to set the foundation of how one will interract with the account table.
public interface AccountDAO {
    
    public Account register(Account newAccount);
    
    public Account login(Account account);

    public Account getAccountByUsername(String username);

}
