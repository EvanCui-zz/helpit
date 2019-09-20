package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao extends MongoRepository<PostModel,Long> {

    @Query("{}")
    Page<PostModel> findAll(Pageable page);

//    @Query("{topics:{$in:?0}})")
//    Page<PostModel> findByTopic(List<String> topics, Pageable page);

    @Query("{source:{$in:?0}}")
    Page<PostModel> findBySource(List<String> source, Pageable page);

//    @Query("{title:?0}")
//    Page<PostModel> findByTitle(String title, Pageable page);
//
//    @Query("{title:{$regex: ?0, $options: 'i'}}")
//    Page<PostModel> findByKeywords(String keywords, Pageable page);

    @Query("{topics:{$in:?0},source:{$in:?1}}")
    Page<PostModel> findByTopicAndSource(List<String> topics, List<String> source, Pageable page);

    @Query("{title:{$regex: ?0, $options: 'i'}}")
    Page<PostModel> findByKeywords(String keywords, Pageable page);

    @Query("{topics:{$in:?0},source:{$in:?1},title:{$regex: ?2, $options: 'i'}}")
    Page<PostModel> findByTopicAndSourceAndKeywords(List<String> topics, List<String> source, String keywords, Pageable page);
}
