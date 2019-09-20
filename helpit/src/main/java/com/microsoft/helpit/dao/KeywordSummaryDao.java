package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.KeywordSummaryModel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface KeywordSummaryDao  extends MongoRepository<KeywordSummaryModel, ObjectId> {

    @Query("{}")
    Page<KeywordSummaryModel> findTop(Pageable page);

    @Query(value="{}",fields="{ 'keyword' : 1,'_id':0}")
    List<String> findAllKeywords();

    KeywordSummaryModel findByKeyword(String keyword);
}
