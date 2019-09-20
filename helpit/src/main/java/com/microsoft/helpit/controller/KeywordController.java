package com.microsoft.helpit.controller;

import com.microsoft.helpit.model.KeywordModel;
import com.microsoft.helpit.model.KeywordSummaryModel;
import com.microsoft.helpit.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/keyword")
public class KeywordController {
    @Autowired
    private KeywordService keywordService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public boolean saveKeywordRecord(@RequestBody KeywordModel keywordModel){
        return keywordService.saveKeywordRecord(keywordModel);
    }

    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public List<KeywordSummaryModel> getAllKeywords(){
        return keywordService.getAllKeywords();
    }

//    @RequestMapping(value = "/getHot",method = RequestMethod.POST)
//    public List<String> getHotKeywords(){
//        return keywordService.getHotKeywords();
//    }

//    @Async
    @RequestMapping(value = "/getHotByNum",method = RequestMethod.POST)
    public List<KeywordSummaryModel> getHotKeywords(@RequestParam("num") int num){
        return keywordService.getHotKeywords(num);
    }

    @RequestMapping(value = "/getMyHot",method = RequestMethod.POST)
    public List<KeywordModel> getMyHotKeywords(@RequestParam("upn") String upn){
        return keywordService.getMyHotSearchWords(upn);
    }

    @RequestMapping(value = "/getMyRecent",method = RequestMethod.POST)
    public List<KeywordModel> getMyRecentKeywords(@RequestParam("upn") String upn){
        return keywordService.getMyRecentSearchWords(upn);
    }

//    @RequestMapping(value = "/getMyHistory",method = RequestMethod.POST)
//    public List<KeywordModel> getMySearchWordsHistory(@RequestParam("upn") String upn){
//        return keywordService.getMySearchWordsHistory(upn);
//    }
}
