package com.controller;

import com.beans.Chater;
import com.beans.Room;
import com.utils.LabUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/5/15.
 */
@Controller
@RequestMapping("/introduce")
public class IntroduceController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Chater introduce(Chater chater){
        Chater chater1 = new Chater();
        if (chater == null||chater.getUserId() == null||chater.getRoomId() == null){
            chater1.setMessage("failed");
            return chater1;
        }
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            LabUtils.FindRoom(chater.getRoomId()).getRoomutils().setIntroducestart(format.parse(chater.getDate()));
        } catch (ParseException e) {

            e.printStackTrace();
            chater1.setMessage("failed");
            return chater1;
        }
        String roomId=chater.getRoomId();
        if (LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()) == null){
            chater1.setMessage("failed");
            return chater1;
        }
        LabUtils.FindRoom(chater.getRoomId()).getRoomutils().setCount(LabUtils.FindRoom(chater.getRoomId()).getRoomnum());

        //设定返回值
        Chater chater2= new Chater();
        chater2.setOrder("introduce");
        chater2.setRoomId(roomId);
        chater2.setUserId(chater.getUserId());
        chater2.setMessage(CheckHost(roomId, chater.getUserId()));
        Room room = LabUtils.FindRoom(chater.getRoomId());
        room.sendAll(chater2);

        chater1.setMessage("SUCCEED");
        return chater1;
    }
    private String CheckHost(String roomId,String userId){
        if(LabUtils.FindRoom(roomId).getHostId().equals(userId)){
            return "SUCCEED";
        }
        return "Not Host";
    }
}
