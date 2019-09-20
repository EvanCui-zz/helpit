package com.microsoft.helpit.service;

import com.microsoft.helpit.model.KeywordModel;
import com.microsoft.helpit.model.KeywordSummaryModel;

import java.util.List;

public interface KeywordService {

    public boolean saveKeywordRecord(KeywordModel keywordModel);

    public List<KeywordSummaryModel> getHotKeywords(int num);

    public List<KeywordModel> getMyHotSearchWords(String upn);

    public List<KeywordModel> getMyRecentSearchWords(String upn);

//    public List<KeywordModel> getMySearchWordsHistory(String upn);

    public List<KeywordSummaryModel> getAllKeywords();
}
