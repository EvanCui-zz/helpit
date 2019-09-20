package com.microsoft.helpit.controller;

import com.microsoft.helpit.model.PostModel;
import com.microsoft.helpit.model.SearchParam;
import com.microsoft.helpit.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public List<PostModel> getAllByPage(@RequestParam("page") int page) {
        return searchService.getAllByPage(page);
    }

    @RequestMapping(value = "/getByKeyword", method = RequestMethod.POST)
    public List<PostModel> getByKeyword(@RequestParam("keyword") String keyword, @RequestParam("page") int page) {
        return searchService.searchPostsByKeyword(keyword,page);
    }

//    @Async
    @RequestMapping(value = "/getInSubscriptions", method = RequestMethod.POST)
    public List<PostModel> searchPosts(@RequestBody SearchParam params) {

        if(params.getKeywords()!=null&&params.getKeywords().length()>0)
            return searchService.searchPosts(params.getTopics(), params.getSources(), params.getKeywords(), params.getPage());
        else
            return searchService.getPostsBySourceAndTopics(params.getTopics(), params.getSources(), params.getPage());
//        if(params.getKeywords()!=null&&params.getKeywords().length()>0&&params.getTopics()!=null&&params.getTopics().size()>0) {
//            return searchService.searchPosts(params.getTopics(), params.getSources(), params.getKeywords(), params.getPage());
//        }else if(params.getKeywords()!=null&&params.getKeywords().length()>0){
//            return searchService.searchPostsBySourceAndKeywords(params.getSources(),params.getKeywords(), params.getPage());
//        }else if(params.getTopics()!=null&&params.getTopics().size()>0)
//            return searchService.getPostsBySourceAndTopics(params.getTopics(), params.getSources(), params.getPage());
//        else if(params.getSources()!=null&&params.getSources().size()>0)
//            return searchService.getPostsBySource(params.getSources(), params.getPage());
//        else
//            return null;
    }


}
