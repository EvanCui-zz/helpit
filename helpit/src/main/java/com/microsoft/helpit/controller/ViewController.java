package com.microsoft.helpit.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Controller
public class ViewController {

    @RequestMapping(value = "/subscribed", method = RequestMethod.GET)
    public String subscribed(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            return "mainPage";
        }else{
            session.setAttribute("url","/subscribed");
        }
        return "redirect:/login";
    }

        @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String all(HttpServletRequest httpRequest, Map<String,Object> map,@RequestParam(value="keyword",required = false) String keyword) throws UnsupportedEncodingException {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");

        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            if(keyword!=null&&(!keyword.trim().isEmpty())){
                map.put("keyword", URLDecoder.decode(keyword.trim(), "utf-8"));
            }
            return "all";
        }else{
            String url = "/all";
            if(keyword!=null&&(!keyword.trim().isEmpty())){
                url+="?keyword="+keyword.trim();
            }
            session.setAttribute("url",url);
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public String topic(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            return "topic";
        }else{
            session.setAttribute("url","/topic");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/source", method = RequestMethod.GET)
    public String sources(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            return "source";
        }else{
            session.setAttribute("url","/source");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/keyword", method = RequestMethod.GET)
    public String keywords(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            return "keyword";
        }else{
            session.setAttribute("url","/keyword");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String report(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            long[] visits = addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            if(session.getAttribute("permission")!=null&&session.getAttribute("permission").toString().equals("admin")){
                if(visits!=null){
                    map.put("visit_total",visits[0]);
                    map.put("visit_today",visits[1]);
                }
                return "report_admin";
            }else{
                return "report";
            }

        }else{
            session.setAttribute("url","/report");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/invitation", method = RequestMethod.GET)
    public String invitation(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            return "invitation";
        }else{
            session.setAttribute("url","/invitation");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            return "account";
        }else{
            session.setAttribute("url","/account");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public String statistics(HttpServletRequest httpRequest, Map<String,Object> map) {
        HttpSession session = httpRequest.getSession();
        String upn = (String) session.getAttribute("upn");
        if(upn!=null){
            addVisitCount();
            map.put("upn",upn);
            map.put("username",session.getAttribute("username"));
            return "statistics";
        }else{
            session.setAttribute("url","/statistics");
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String init(){
        return "redirect:/login";
    }

    private long[] addVisitCount(){
        try {
//            Resource resource = new ClassPathResource("data/visit_count.txt");
//            File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+ "data/visit_count.txt");
            File file  = new File("visit_count.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
//            BufferedReader in = new BufferedReader(new InputStreamReader((resource.getInputStream()));

            String str = in.readLine();
//            String totalStr = "";
//            String tc_str = "";
            String date_str = "";
            long total = 1;
            long tcount = 1;
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String today_str = sdf.format(today);

            if(str!=null){
                if(!str.isEmpty()){
                    total = Long.parseLong(str)+1;
                }
                String tmp = in.readLine();
                if(tmp!=null&&(!tmp.isEmpty())){
                    String[] tarr = tmp.split(" ");
                    tcount = Long.parseLong(tarr[0]);
                    if(tarr.length==2){
                        date_str = tarr[1];
                        if(date_str.equals(today_str)){
                            tcount+=1;
                        }else{
                            tcount = 1;
                        }
                    }
                }
            }
            in.close();

            FileWriter out =new FileWriter(file);
            out.write(total+"\n"+tcount+" "+today_str);
            out.flush();
            out.close();

            return new long[]{total,tcount};
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
