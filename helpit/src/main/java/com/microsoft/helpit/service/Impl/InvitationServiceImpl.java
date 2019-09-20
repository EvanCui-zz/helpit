package com.microsoft.helpit.service.Impl;

import com.microsoft.helpit.dao.InvitationDao;
import com.microsoft.helpit.model.InvitationModel;
import com.microsoft.helpit.service.InvitationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {
    @Autowired
    private InvitationDao invitationDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(InvitationServiceImpl.class);

    @Override
    public List<InvitationModel> getUnreadReceivedInvitations(String receiver) {
        LOGGER.info("InvitationServiceImpl.getUnreadReceivedInvitations - {}",receiver);
        return invitationDao.findByReceiverAndStatus(receiver,false, new Sort(Sort.Direction.DESC,"time"));
    }

    @Override
    public boolean isSent(String sender, String receiver, String web_url) {
        LOGGER.info("InvitationServiceImpl.isSent - {},{},{}",sender,receiver,web_url);
        return (invitationDao.findBySenderAndReceiverAndUrl(sender,receiver,web_url)!=null);
    }

    @Override
    public String sendInvitation(InvitationModel invitationModel) {
        LOGGER.info("InvitationServiceImpl.isSent - {},{},{}",invitationModel.getSender(),invitationModel.getReceiver(),invitationModel.getWeb_url());
        if(invitationDao.findBySenderAndReceiverAndUrl(invitationModel.getSender(),invitationModel.getReceiver(),invitationModel.getWeb_url())!=null)
            return "duplicate";
        invitationDao.save(invitationModel);
        return "success";
    }

    @Override
    public boolean readInvitation(String sender, String receiver, String web_url) {
        LOGGER.info("InvitationServiceImpl.readInvitation - {},{},{}",sender,receiver,web_url);
        InvitationModel model = invitationDao.findBySenderAndReceiverAndUrl(sender,receiver,web_url);
        model.setStatus(true);
        invitationDao.save(model);
        return true;
    }

    @Override
    public int getUnreadInvitationNum(String upn) {
//        Aggregation aggregation = newAggregation(
//                match(Criteria.where("receiver").is(upn)),
//                group("receiver")
//                        .count().as("count"),
//                project("count").and("receiver").previousOperation()
//        );
        LOGGER.info("InvitationServiceImpl.getUnreadInvitationNum - {}",upn);
        return invitationDao.findByReceiverAndStatus(upn,false, new Sort(Sort.Direction.DESC,"time")).size();
    }
}
