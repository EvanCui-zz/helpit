package com.microsoft.helpit.service;

import com.microsoft.helpit.model.AccountModel;
import com.microsoft.helpit.model.AccountSuggestionModel;

import java.util.List;

public interface UserService {

    public boolean isExisted(String upn);

    public List<AccountModel> getAccounts(String upn);

//    public boolean initAccounts(String upn,String username);

    public boolean checkAccount(Long id,String source);

    public boolean modifyAccount(List<AccountModel> accounts);

    public List<AccountSuggestionModel> getSuggestedAccounts(String upn);

    public boolean isBlank(String upn);

    public int getNumber();

    public boolean changeSendEmail(String upn,boolean sendEmail);

    public boolean getSendEmail(String upn);
}
