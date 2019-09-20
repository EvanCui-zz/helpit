package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.KeywordModel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordDao extends MongoRepository<KeywordModel, ObjectId> {

    List<KeywordModel> findByUpn(String upn, Sort sort);

    Page<KeywordModel> findByUpn(String upn, Pageable page);

//    List<KeywordModel> findTop(Pageable page);

    KeywordModel findByUpnAndKeyword(String upn,String keyword);
}
