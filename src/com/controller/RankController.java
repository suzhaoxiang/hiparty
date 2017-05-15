package com.controller;

import com.beans.Chater;
import com.beans.Room;
import com.beans.RoomUser;
import com.utils.LabUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
@Controller
@RequestMapping("/rank")
public class RankController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Chater rank(Chater chater){
        Chater chater1 = new Chater();
        if (chater == null||chater.getUserId() == null||chater.getRoomId() == null){
            chater1.setMessage("failed");
            return chater1;
        }
        if (LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()) == null){
            chater1.setMessage("failed");
            return chater1;
        }
        int num= LabUtils.FindRoom(chater.getRoomId()).getRoomnum();
        Map<Integer,RoomUser> roomusermap=new HashMap<>();
        List<Integer> ranklist=new ArrayList<>();
        //绑定
        for(int i=0;i<num;i++){
            int seat=(int) (Math.random()*1000);
            ranklist.add(seat);
            roomusermap.put(seat, LabUtils.FindRoom(chater.getRoomId()).getUserlist().get(i));
        }
        //排序
        for(int i=0;i<ranklist.size();i++){
            for(int j=0;j<i;j++){
                if(ranklist.get(i)>ranklist.get(j)){
                    int temp=ranklist.get(i);
                    ranklist.set(i, ranklist.get(j));
                    ranklist.set(j, temp);
                }
            }
        }
        //进行编号
        String rankinformation=1+":"+roomusermap.get(ranklist.get(0)).getNickname();

        for(int i=1;i<num;i++){
            String nickname = roomusermap.get(ranklist.get(i)).getNickname();
            if(nickname!=null&&!nickname.equals("")){
                rankinformation=rankinformation+'\n'+(i+1)+":"+roomusermap.get(ranklist.get(i)).getNickname();
            }
            else{
                rankinformation=rankinformation+'\n'+(i+1)+":"+roomusermap.get(ranklist.get(i)).getUserId();
            }
            roomusermap.get(ranklist.get(i)).setSeat(i);
        }
        Map<String,String> object = new HashMap<>();
        object.put("rank", rankinformation);

        Chater chater2 = new Chater();
        chater2.setOrder("rank");
        chater2.setMessage(CheckHost(chater.getRoomId(), chater.getUserId()));
        chater2.setUserId(chater.getUserId());
        chater2.setRoomId(chater.getRoomId());
        chater2.setObject(object);
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
