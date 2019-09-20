package com.microsoft.helpit.service;

import java.util.List;

public interface SourceService {

    public List<String> getAllSources();

    public List<String> getSubscribedSources(String upn);

    public boolean subscribeSource(String upn,String source);

    public boolean unsubscribeSource(String upn,String source);
}
