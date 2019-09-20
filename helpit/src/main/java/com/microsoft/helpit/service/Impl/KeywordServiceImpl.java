package com.microsoft.helpit.service.Impl;

import com.microsoft.helpit.dao.KeywordDao;
import com.microsoft.helpit.dao.KeywordSummaryDao;
import com.microsoft.helpit.model.KeywordModel;
import com.microsoft.helpit.model.KeywordSummaryModel;
import com.microsoft.helpit.service.KeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class KeywordServiceImpl implements KeywordService {
    @Autowired
    private KeywordDao keywordDao;
    @Autowired
    private KeywordSummaryDao keywordSummaryDao;
    private Lock updateLock = new ReentrantLock();
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordServiceImpl.class);

    @Override
    public boolean saveKeywordRecord(KeywordModel keywordModel) {
        KeywordModel model = keywordDao.findByUpnAndKeyword(keywordModel.getUpn(),keywordModel.getKeyword());
        KeywordSummaryModel keywordSummary = keywordSummaryDao.findByKeyword(keywordModel.getKeyword());

        try{
            updateLock.lock();
            if(model!=null){
                model.setTime(keywordModel.getTime());
                model.setNum(model.getNum()+1);
                keywordDao.save(model);
            }else{
                keywordModel.setNum(1);
                keywordDao.save(keywordModel);
            }

            if(keywordSummary!=null){
                keywordSummary.setNum(keywordSummary.getNum()+1);
                keywordSummaryDao.save(keywordSummary);
            }else{
                keywordSummaryDao.save(new KeywordSummaryModel(keywordModel.getKeyword(),1));
            }
        }finally{
            updateLock.unlock();
        }

        LOGGER.info("KeywordServiceImpl.saveKeywordRecord - {},{}",keywordModel.getUpn(),keywordModel.getTime());
        return true;
    }

    @Override
    public List<KeywordSummaryModel> getHotKeywords(int num) {
        LOGGER.info("KeywordServiceImpl.getHotKeywords - {}",num);
        Pageable page = new PageRequest(0,num,new Sort(Sort.Direction.DESC,"num"));
        return keywordSummaryDao.findTop(page).getContent();
    }

    @Override
    public List<KeywordModel> getMyHotSearchWords(String upn) {
        LOGGER.info("KeywordServiceImpl.getMyHotSearchWords - {}",upn);
        Pageable page = new PageRequest(0,15,new Sort(Sort.Direction.DESC,"num"));
        return keywordDao.findByUpn(upn,page).getContent();
    }

    @Override
    public List<KeywordModel> getMyRecentSearchWords(String upn) {
        LOGGER.info("KeywordServiceImpl.getMyRecentSearchWords - {}",upn);
        Pageable page = new PageRequest(0,28,new Sort(Sort.Direction.DESC,"time"));
        return keywordDao.findByUpn(upn,page).getContent();
    }

//    @Override
//    public List<KeywordModel> getMySearchWordsHistory(String upn) {
//        return keywordDao.findByUpn(upn,new Sort(Sort.Direction.DESC,"time"));
//    }

    @Override
    public List<KeywordSummaryModel> getAllKeywords() {
        LOGGER.info("KeywordServiceImpl.getAllKeywords");
        return keywordSummaryDao.findAll();
//        Aggregation aggregation = newAggregation(
//                group("keyword")
//                        .sum("num").as("count"),
//                project("count").and("keyword").previousOperation()
//        );
//
//        return getGroupedKeywords(aggregation);
    }

//    private List<String> getGroupedKeywords(Aggregation aggregation){
//        AggregationResults<KeywordCount> aggregationResults = mongoTemplate.aggregate(aggregation, KeywordModel.class, KeywordCount.class);
//        List<String> res = new ArrayList<>();
//        for(KeywordCount t : aggregationResults){
//            res.add(t.getKeyword());
//        }
//        return res;
//    }
}
