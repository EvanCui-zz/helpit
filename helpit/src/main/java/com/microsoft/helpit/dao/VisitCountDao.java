package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.VisitCountModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitCountDao extends MongoRepository<VisitCountModel, ObjectId> {

    VisitCountModel findByUpnAndDate(String upn, long date);
}
