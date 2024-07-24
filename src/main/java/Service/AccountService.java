package Service;

import static org.mockito.ArgumentMatchers.refEq;

import java.util.List;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    public AccountService(){
        this.accountDAO=new AccountDAO();

    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO=accountDAO;
    }
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();

    }
    public Account insertAccount(Account account){
       
        return accountDAO.insertAccount(account);
        
       
    }
    public Account getAccountByUsername(String username){
       return accountDAO.getAccountByUsername(username);
    }
    public boolean getAccountById(int id){
        return accountDAO.getAccountById(id);
    }
    public boolean updateAccount(Account account){
        return accountDAO.updateAccount(account);
    }
    public boolean deleteAccount(int account_id){
        return accountDAO.deleteAccount(account_id);
    }
    public Account login(String username, String password){
        return accountDAO.login(username, password);
    }
    
}
