package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.TopicModel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicDao extends MongoRepository<TopicModel, ObjectId> {

    @Query("{}")
    List<TopicModel> findAll(Sort sort);

    TopicModel findByTopic(String topic);

    @Query("{}")
    Page<TopicModel> findTop10(Pageable page);

    @Query("{}")
    Page<TopicModel> findNewest10(Pageable page);
}
