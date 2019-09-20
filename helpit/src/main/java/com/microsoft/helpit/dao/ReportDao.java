package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.ReportModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface ReportDao extends MongoRepository<ReportModel, ObjectId> {

    List<ReportModel> findByUpn(String upn);

    @Query("{upn:?0,post_time:{$lt:?2,$gte:?1}}")
    List<ReportModel> findByUpnAndTime(String upn, Date stime, Date etime);
}
