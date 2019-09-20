package com.microsoft.helpit.service;

import com.microsoft.helpit.model.PostModel;

import java.util.Date;
import java.util.List;

public interface SearchService {

    public List<PostModel> getAllByPage(int pageIndex);

//    public List<PostModel> getPostsByTopics(List<String> topics, int pageIndex);

//    public List<PostModel> getPostsBySource(List<String> sources, int pageIndex);

//    public List<PostModel> searchPostsByKeywords(String keywords, int pageIndex);

    public List<PostModel> getPostsBySourceAndTopics(List<String> topics,List<String> sources, int pageIndex);

//    public List<PostModel> searchPostsBySourceAndKeywords(List<String> sources, String keywords, int pageIndex);

    public List<PostModel> searchPostsByKeyword(String keywords, int pageIndex);

    public List<PostModel> searchPosts(List<String> topics,List<String> sources, String keywords, int pageIndex);
}
