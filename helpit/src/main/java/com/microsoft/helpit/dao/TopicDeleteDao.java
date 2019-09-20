package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.TopicDeleteModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicDeleteDao extends MongoRepository<TopicDeleteModel, ObjectId> {

    TopicDeleteModel findByTopicAndVoter(String topic,String voter);

    List<TopicDeleteModel> findByVoter(String voter);
}
