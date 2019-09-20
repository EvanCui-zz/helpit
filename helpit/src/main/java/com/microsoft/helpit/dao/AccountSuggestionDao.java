package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.AccountSuggestionModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountSuggestionDao extends MongoRepository<AccountSuggestionModel, ObjectId> {

    List<AccountSuggestionModel> findByUpn(String upn);
}
