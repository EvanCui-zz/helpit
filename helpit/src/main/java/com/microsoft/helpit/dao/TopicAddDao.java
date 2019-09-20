package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.TopicAddModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicAddDao extends MongoRepository<TopicAddModel, ObjectId> {

    TopicAddModel findByTopic(String topic);
}
