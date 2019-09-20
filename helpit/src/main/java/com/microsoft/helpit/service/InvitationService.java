package com.microsoft.helpit.service;

import com.microsoft.helpit.model.InvitationModel;

import java.util.List;

public interface InvitationService {
    public List<InvitationModel> getUnreadReceivedInvitations(String receiver);

    public boolean isSent(String sender,String receiver,String web_url);

    public String sendInvitation(InvitationModel invitationModel);

    public boolean readInvitation(String sender,String receiver,String web_url);

    public int getUnreadInvitationNum(String upn);
}
