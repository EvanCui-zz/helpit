package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.SubscriptionModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionDao extends MongoRepository<SubscriptionModel, ObjectId> {

    @Nullable
    SubscriptionModel findByUpn(String upn);
}
