package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.AccountModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao extends MongoRepository<AccountModel, ObjectId> {

    List<AccountModel> findByUpn(String upn);

    AccountModel findByUpnAndSource(String upn, String source);
}
