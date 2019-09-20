package com.microsoft.helpit.controller;

import com.microsoft.helpit.model.InvitationModel;
import com.microsoft.helpit.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/invitation")
public class InvitationController {
    @Autowired
    private InvitationService invitationService;

    @RequestMapping(value = "/getUnreadNum",method = RequestMethod.POST)
    public int getUnreadNum(@RequestParam("upn") String upn){
        return invitationService.getUnreadInvitationNum(upn);
    }

    @RequestMapping(value = "/getUnreadReceived",method = RequestMethod.POST)
    public List<InvitationModel> getUnreadReceived(@RequestParam("receiver") String receiver){
        return invitationService.getUnreadReceivedInvitations(receiver);
    }

    @RequestMapping(value = "/send",method = RequestMethod.POST)
    public String send(@RequestBody InvitationModel model){
        return invitationService.sendInvitation(model);
    }

    @RequestMapping(value = "/read",method = RequestMethod.POST)
    public boolean read(@RequestParam("sender") String sender,@RequestParam("receiver") String receiver,@RequestParam("web_url") String web_url){
        return invitationService.readInvitation(sender, receiver, web_url);
    }
}
