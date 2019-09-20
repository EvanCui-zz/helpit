package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.AnalysisModel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AnalysisDao extends MongoRepository<AnalysisModel, ObjectId> {

    @Query("{upn:?0,post_time:{$lt:?2,$gte:?1}}")
    List<AnalysisModel> findByUpnAndPost_time(String upn, Date s, Date e);

    List<AnalysisModel> findByUpn(String upn);
}
