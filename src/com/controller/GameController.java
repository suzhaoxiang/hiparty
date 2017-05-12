package com.controller;

import com.beans.*;
import com.google.gson.Gson;
import com.utils.HibernateUtil;
import com.utils.LabUtils;
import org.apache.mina.core.session.IoSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/21.
 */

@Controller
@RequestMapping("/game")
public class GameController {
    private SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
    //暖场游戏
    @RequestMapping(value="/warmgame")
    @ResponseBody
    public Chater WarmGame(String level, HttpServletRequest request){
        String decode=null;
        try {
            decode= URLDecoder.decode(level,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new Chater();
        }

        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<WarmGame> warmGamelist = session.createQuery("from WarmGame where warmGameLevel=:WarmGameLevel")
                .setParameter("WarmGameLevel", decode)
                .list();
        session.getTransaction().commit();
        String localAddr = request.getLocalAddr();
        // 设定返回值
        for (int i = 0; i <warmGamelist.size(); i++) {
            String url=warmGamelist.get(i).getWarmGameUrl();
            url=url.replace("\\","/");
            warmGamelist.get(i).setWarmGameUrl("http://"+localAddr+":8099/user/download?url=/ROOT/hiparty/WEB-INF/classes/loadfile/warm_game"+url);
        }
        Chater chater = new Chater();
        chater.setOrder("warmgame");
        Map<String, Object> object = new HashMap<>();
        object.put("size", warmGamelist.size());
        object.put("list", new Gson().toJson(warmGamelist));
        chater.setObject(object);
        chater.setMessage("SUCCEED");

        return chater;
    }

    @RequestMapping("/getlist")
    @ResponseBody
    public Chater gelist(Chater chater){
        Chater chater2 = new Chater();
        chater2.setOrder("getlist");
        Room room = LabUtils.FindRoom(chater.getRoomId());
        if(chater.getRoomId()==null||chater.getUserId()==null||room==null) {
            chater2.setMessage("FALSE");
            return chater2;
        }
        List<RoomUserDTO> roomUserDTOList =new ArrayList<>();
        List<RoomUser> roomUserList = LabUtils.FindRoom(chater.getRoomId()).getUserlist();
        int size = roomUserList.size();
        for(int i=0;i<size;i++){
            RoomUserDTO roomUserDTO = new RoomUserDTO();
            roomUserDTO.setUserId(roomUserList.get(i).getUserId());
            roomUserDTO.setNickname(roomUserList.get(i).getNickname());
            roomUserDTO.setSeat(roomUserList.get(i).getSeat());
            roomUserDTOList.add(roomUserDTO);
        }
        chater2.setMessage("SUCCEED");
        chater2.setRoomId(chater.getRoomId());
        chater2.setUserId(chater.getUserId());
        String s = new Gson().toJson(roomUserDTOList);
        chater2.setObject(s);
        return chater2;
    }
    @RequestMapping("/getPlayerList")
    @ResponseBody
    public void getPlayerList(Chater chater,String[] ids){
        Chater chater2=new Chater();
        Room room=LabUtils.FindRoom(chater.getRoomId());
        if(chater.getRoomId()==null||chater.getUserId()==null){
            chater2.setMessage("False");
            room.sendSingle(chater2,LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()).getSession());
        }
        List<RoomUser> playerlist = room.getUserlist();
        for (int i = 0; i < ids.length; i++) {
            playerlist.remove(LabUtils.FindRoomUser(chater.getRoomId(),ids[i]));
        }
        String s=new Gson().toJson(playerlist);
        chater2.setRoomId(chater.getRoomId());
        chater2.setUserId(chater.getUserId());
        chater2.setMessage("SUCCEED");
        chater2.setObject(s);
        chater2.setOrder("getPlayerList");
        room.sendSingle(chater2,LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()).getSession());
    }
    @RequestMapping("/werewolf")
    @ResponseBody
    public void BeginWerewolf(Chater chater,String[] ids) {
        String obj =  chater.getObject().toString();
        Werewolf werewolf=new Gson().fromJson(obj,Werewolf.class);
        Room room=LabUtils.FindRoom(chater.getRoomId());
        List<RoomUser> playerlist = room.getUserlist();
        for (int i = 0; i < ids.length; i++) {
            playerlist.remove(LabUtils.FindRoomUser(chater.getRoomId(),ids[i]));
        }
        Chater chater2 = new Chater();
        chater2.setOrder("werewolf");
        chater2.setRoomId(chater.getRoomId());

        //上帝
        if (werewolf.getGod() == null) {
            werewolf.setGod(chater.getUserId());
        }
        chater2.setMessage("God");
        playerlist.remove(LabUtils.FindRoomUser(chater.getRoomId(),werewolf.getGod()));
        room.sendSingle(chater2, LabUtils.FindRoomUser(chater.getRoomId(),werewolf.getGod()).getSession());
        //预言家
        if (werewolf.isSeerIs()) {
            chater2.setMessage("预言家");
            playerlist=createPlayer(chater2,playerlist);
        }
        //女巫
        if (werewolf.isWitchIs()) {
            chater2.setMessage("女巫");
            playerlist=createPlayer(chater2,playerlist);
        }
        //猎人
        if (werewolf.isHunterIs()) {
            chater2.setMessage("猎人");
            playerlist=createPlayer(chater2,playerlist);
        }
        //盗贼
        if (werewolf.isThiefIs()) {
            chater2.setMessage("盗贼");
            playerlist=createPlayer(chater2,playerlist);
        }
        //白痴
        if (werewolf.isIdiotIs()) {
            chater2.setMessage("白痴");
            playerlist=createPlayer(chater2,playerlist);
        }
        //丘比特
        if (werewolf.isCupidIs()) {
            chater2.setMessage("丘比特");
            playerlist=createPlayer(chater2,playerlist);
        }
        //守卫
        if (werewolf.isGuardIs()) {
            chater2.setMessage("守卫");
            playerlist=createPlayer(chater2,playerlist);
        }
        //小女孩
        if (werewolf.isGirlIs()) {
            chater2.setMessage("小女孩");
            playerlist=createPlayer(chater2,playerlist);
        }
        //长老
        if (werewolf.isPresbyterIs()) {
            chater2.setMessage("长老");
            playerlist=createPlayer(chater2,playerlist);
        }
        //狼人
        for (int i = 0; i < werewolf.getWerewolfnum(); i++) {
            chater2.setMessage("狼人");
            playerlist=createPlayer(chater2,playerlist);
        }
        //村民
        for (int i = 0; i < werewolf.getVillagernum(); i++) {
            chater2.setMessage("村民");
            playerlist=createPlayer(chater2,playerlist);
        }
    }

    private List<RoomUser> createPlayer(Chater chater,List<RoomUser> playerlist){
        Room room=LabUtils.FindRoom(chater.getRoomId());
        RoomUser player = playerlist.get((int) (Math.random() * 1000) % playerlist.size());
        playerlist.remove(player);
        chater.setUserId(player.getUserId());
        room.sendSingle(chater, player.getSession());
        return playerlist;
    }
    @RequestMapping("/youguess")
    @ResponseBody
    public Chater YouGuess(Chater chater){
        Map<String,Object> objectMap=(Map<String, Object>) chater.getObject();
        String type= (String) objectMap.get("type");
        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<String> wordList=session.createQuery("SELECT word FROM GameWords where type=:type")
                .setParameter("type",type)
                .list();
        session.getTransaction().commit();
        List<String> sendList=new ArrayList<>();
        Chater chater2=new Chater();
        chater2.setRoomId(chater.getRoomId());
        chater2.setUserId(chater.getUserId());
        chater2.setOrder("YouGuess");
        if (wordList.size()==0){
            chater2.setMessage("type error");
            return chater2;
        }
        int size=wordList.size();
        for (int i = 0; i <size ; i++) {
            int index= (int) ((Math.random()*1000)%wordList.size());
            sendList.add(wordList.get(index));
            wordList.remove(index);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("wordList",sendList);
        chater2.setObject(map);
        return chater2;
    }
}
