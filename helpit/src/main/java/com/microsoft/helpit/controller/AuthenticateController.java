package com.microsoft.helpit.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.microsoft.helpit.util.AuthHelper;
import com.microsoft.aad.adal4j.AuthenticationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthenticateController {

    @Value("${admin.upn}")
    private String admins;

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public String login(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        AuthenticationResult result = (AuthenticationResult) session.getAttribute(AuthHelper.PRINCIPAL_SESSION_NAME);

        if (result != null) {
            try {
                DecodedJWT jwt = JWT.decode(result.getIdToken());
                String username = jwt.getClaim("name").asString();
                String unique_name = jwt.getClaim("unique_name").asString();
                session.setAttribute("username",username);
                session.setAttribute("upn",unique_name);

                String[] adminArr = admins.split(",");
                for(String admin:adminArr){
                    if(admin.trim().equals(unique_name)){
                        session.setAttribute("permission","admin");
                        break;
                    }
                }

                if(session.getAttribute("url")!=null){
                    String url = session.getAttribute("url").toString();
                    session.removeAttribute("url");
                    return "redirect:"+url;
                }else
                    return "redirect:/subscribed";
            } catch (JWTDecodeException exception) {
                //Invalid token
                exception.printStackTrace();
            }
        }
        return "error";
    }
}
