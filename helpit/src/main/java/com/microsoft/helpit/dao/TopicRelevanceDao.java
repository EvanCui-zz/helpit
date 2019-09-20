package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.TopicRelevanceModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicRelevanceDao extends MongoRepository<TopicRelevanceModel, ObjectId> {

    List<TopicRelevanceModel> findDistinctByTopic1OrTopic2(String topic1, String topic2);
}
