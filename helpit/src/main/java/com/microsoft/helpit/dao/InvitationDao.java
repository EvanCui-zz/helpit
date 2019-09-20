package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.InvitationModel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationDao extends MongoRepository<InvitationModel, ObjectId> {

    @Query("{sender:?0,receiver:?1,web_url:?2}")
    InvitationModel findBySenderAndReceiverAndUrl(String sender,String receiver, String web_url);

    List<InvitationModel> findByReceiverAndStatus(String receiver, boolean status, Sort sort);

}
