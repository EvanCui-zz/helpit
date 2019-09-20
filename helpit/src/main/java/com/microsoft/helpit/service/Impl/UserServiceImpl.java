package com.microsoft.helpit.service.Impl;

import com.microsoft.helpit.dao.*;
import com.microsoft.helpit.model.*;
import com.microsoft.helpit.service.UserService;
import com.microsoft.helpit.util.URLConnector;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private AccountSuggestionDao suggestionDao;
    @Autowired
    private SubscriptionDao subscriptionDao;
    @Value("${stackoverflow.url.user}")
    private String url_user_sof;
    @Value("${yammer.url.search.user}")
    private String url_user_yam;
    @Value("${yammer.token}")
    private String bearerToken;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean isExisted(String upn) {
        SubscriptionModel model = subscriptionDao.findByUpn(upn);
        if(model==null)
            return false;
        return true;
    }

    @Override
    public List<AccountModel> getAccounts(String upn) {
        LOGGER.info("UserServiceImpl.getAccounts - {}",upn);
        List<AccountModel> res = new ArrayList<>();
        List<TimeModel> timeModels = timeDao.findAllSources();

        for(TimeModel t : timeModels){
            AccountModel accountModel = accountDao.findByUpnAndSource(upn,t.getSource());
            if(accountModel ==null){
                res.add(new AccountModel(upn,null,t.getSource(),null));
            }else{
                res.add(accountModel);
            }
        }
        return res;
    }

//    @Override
//    public boolean initAccounts(String upn, String username) {
//        List<TimeModel> timeModels = timeDao.findAllSources();
//        for(TimeModel t : timeModels){
//            if(userDao.findByUpnAndSource(upn,t.getSource())==null)
//                userDao.save(new AccountModel(upn,username,t.getSource(),null));
//        }
//        return true;
//    }

    @Override
    public boolean checkAccount(Long id, String source) {
        LOGGER.info("UserServiceImpl.checkAccount - {},{}",id,source);
        if(id==null)
            return true;
//        if(id>Long.MAX_VALUE)
//            return false;
        if(source.equals("yammer"))
            return checkYammerAccount(id);
        if(source.equals("stackoverflow"))
            return checkStackoverflowAccount(id);

        return false;
    }

    @Override
    public boolean modifyAccount(List<AccountModel> accounts) {
        LOGGER.info("UserServiceImpl.modifyAccount - {}",accounts.size());
        String upn = "";
        if(accounts.size()>0){
            upn = accounts.get(0).getUpn();
        }
        for(AccountModel accountModel :accounts){
            AccountModel account = accountDao.findByUpnAndSource(upn, accountModel.getSource());
            if(account!=null){
                account.setAccount(accountModel.getAccount());
                accountDao.save(account);
            }else{
                accountDao.save(accountModel);
            }
        }
        return true;
    }

    @Override
    public List<AccountSuggestionModel> getSuggestedAccounts(String upn) {
        LOGGER.info("UserServiceImpl.getSuggestedAccounts - {}",upn);
        return suggestionDao.findByUpn(upn);
    }

    @Override
    public boolean isBlank(String upn) {
        LOGGER.info("UserServiceImpl.isBlank - {}",upn);
        List<AccountModel> accountModels = accountDao.findByUpn(upn);
        if(accountModels.size()==0)
            return true;
        for(AccountModel am:accountModels){
            if(am ==null|| am.getAccount()==null)
                return true;
        }
//        List<TimeModel> timeModels = timeDao.findAllSources();
//        for(TimeModel t : timeModels){
//            AccountModel accountModel = accountDao.findByUpnAndSource(upn,t.getSource());
//            if(accountModel ==null|| accountModel.getAccount()==null) {
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public int getNumber() {
        return subscriptionDao.findAll().size();
    }

    @Override
    public boolean changeSendEmail(String upn, boolean sendEmail) {
        LOGGER.info("UserServiceImpl.changeSendEmail - {},{}",upn,sendEmail);
        SubscriptionModel model = subscriptionDao.findByUpn(upn);
        if(model==null){
            List<TimeModel> timeModels = timeDao.findAllSources();
            List<String> sources = new ArrayList<>();
            for(TimeModel t : timeModels){
                sources.add(t.getSource());
            }
            model = new SubscriptionModel(upn,null,sources,sendEmail);
        }else{
            model.setSendEmail(sendEmail);
        }
        subscriptionDao.save(model);
        return true;
    }

    @Override
    public boolean getSendEmail(String upn) {
        LOGGER.info("UserServiceImpl.changeSendEmail - {}",upn);
        SubscriptionModel model = subscriptionDao.findByUpn(upn);
        if(model==null){
            List<TimeModel> timeModels = timeDao.findAllSources();
            List<String> sources = new ArrayList<>();
            for(TimeModel t : timeModels){
                sources.add(t.getSource());
            }
            model = new SubscriptionModel(upn,null,sources,false);
            subscriptionDao.save(model);
            return  false;
        }else{
            return model.isSendEmail();
        }
    }

    private boolean checkYammerAccount(long id){
        LOGGER.info("UserServiceImpl.checkYammerAccount - {}",id);
        String url_str = url_user_yam.replaceAll("\\{userId\\}",id+"");
        String out = URLConnector.doGet(url_str,bearerToken,false);

        if(out!=null){
            try {
                JSONObject jobj = new JSONObject(out);
                if(jobj.getString("id")!=null)
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkStackoverflowAccount(long id){
        LOGGER.info("UserServiceImpl.checkStackoverflowAccount - {}",id);
        String url_str = url_user_sof.replaceAll("\\{userId\\}",id+"");
        String out = URLConnector.doGet(url_str,null,true);

        if(out!=null){
            try {
                JSONObject jobj = new JSONObject(out);
                if(jobj.getJSONArray("items")!=null&&jobj.getJSONArray("items").length()>0)
                    return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
