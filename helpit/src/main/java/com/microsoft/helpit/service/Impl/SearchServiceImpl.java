package com.microsoft.helpit.service.Impl;

import com.microsoft.helpit.dao.PostDao;
import com.microsoft.helpit.dao.TimeDao;
import com.microsoft.helpit.model.PostModel;
import com.microsoft.helpit.model.TimeModel;
import com.microsoft.helpit.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private PostDao postDao;
    private int pageSize = 3*5;
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

//    @Async
    @Override
    public List<PostModel> getAllByPage(int pageIndex) {
        LOGGER.info("SearchServiceImpl.getAllByPage - {}",pageIndex);
        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
        return postDao.findAll(page).getContent();
    }
//
//    @Override
//    public List<PostModel> getPostsByTopics(List<String> topics, int pageIndex) {
//        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
//        return postDao.findByTopic(topics,page).getContent();
//    }

//    @Async
//    @Override
//    public List<PostModel> getPostsBySource(List<String> source, int pageIndex) {
//        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
//        return postDao.findBySource(source,page).getContent();
//    }

//    @Override
//    public List<PostModel> searchPostsByKeywords(String keywords, int pageIndex) {
//        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
//        return postDao.findByKeywords(keywords,page).getContent();
//    }

//    @Async
    @Override
    public List<PostModel> getPostsBySourceAndTopics(List<String> topics, List<String> sources, int pageIndex) {
        LOGGER.info("SearchServiceImpl.getPostsBySource - {},{},{}",pageIndex,topics.size(),sources.size());
//        long s = System.currentTimeMillis();
        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
//        List<PostModel> res =  postDao.findByTopicAndSource(topics,sources,page).getContent();
//        System.out.println("getPostsBySourceAndTopics: "+(System.currentTimeMillis()-s));
        return postDao.findByTopicAndSource(topics,sources,page).getContent();
    }

    @Override
    public List<PostModel> searchPostsByKeyword(String keywords, int pageIndex) {
        LOGGER.info("SearchServiceImpl.searchPostsByKeyword - {},{}",pageIndex,keywords);
        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
        return postDao.findByKeywords(keywords,page).getContent();
    }

//    @Override
//    public List<PostModel> searchPostsBySourceAndKeywords(List<String> sources, String keywords, int pageIndex) {
//        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
//        return postDao.findBySourceAndKeywords(sources,keywords,page).getContent();
//    }

    @Override
    public List<PostModel> searchPosts(List<String> topics, List<String> sources, String keywords, int pageIndex) {
        LOGGER.info("SearchServiceImpl.searchPosts - {},{},{},{}",pageIndex,keywords,topics.size(),sources.size());
        Pageable page = new PageRequest(pageIndex,pageSize,new Sort(Sort.Direction.DESC,"active_time"));
        return postDao.findByTopicAndSourceAndKeywords(topics,sources,keywords,page).getContent();
    }
}
