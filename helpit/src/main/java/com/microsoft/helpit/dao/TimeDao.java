package com.microsoft.helpit.dao;

import com.microsoft.helpit.model.TimeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeDao extends MongoRepository<TimeModel,String> {

//    @Query("{source:?0}")
//    @Nullable
//    TimeModel findBySource(String source);

    @Query(value="{}", fields="{source:1}")
    List<TimeModel> findAllSources();

}
