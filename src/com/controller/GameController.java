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
            warmGamelist.get(i).setWarmGameUrl("http://"+localAddr+":8080/user/download?url=C:/Users/Administrator/Desktop/hiparty/hipartyDB/loadfile/warm_game/"+url);
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

    @RequestMapping("/submitWarmGame")
    @ResponseBody
    public Chater submitWarmGame(Chater chater){
        Chater chater1 = new Chater();
        if (chater == null||chater.getUserId() == null||chater.getRoomId() == null){
            chater1.setMessage("failed");
            return chater1;
        }
        if (LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()) == null){
            chater1.setMessage("failed");
            return chater1;
        }
        String warmgameId=(String) chater.getObject();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
        WarmGame warmgame = (WarmGame) session.createQuery("from WarmGame where warmGameId=:WarmGameId")
                .setParameter("WarmGameId", warmgameId)
                .uniqueResult();
        session.getTransaction().commit();

        Map<String, Object> object2 = new HashMap<>();
        object2.put("warmgame",warmgame.getWarmGame());
        Chater chater2 = new Chater();
        chater2.setObject(object2);
        chater2.setMessage(CheckHost(chater.getRoomId(), chater.getUserId()));
        chater2.setOrder("ensure_warmgame");
        chater2.setRoomId(chater.getRoomId());
        chater2.setUserId(chater.getUserId());
        Room room = LabUtils.FindRoom(chater.getRoomId());
        room.sendAll(chater2);

        chater1.setMessage("SUCCEED");
        return chater1;
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
    public Chater getPlayerList(Chater chater,String[] ids){
        Chater chater2=new Chater();
        Room room=LabUtils.FindRoom(chater.getRoomId());
        if(chater.getRoomId()==null||chater.getUserId()==null){
            chater2.setMessage("False");
            room.sendSingle(chater2,LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()).getSession());
        }
        List<RoomUser> playerlist = room.getUserlist();
        List<RoomUserDTO> players = new ArrayList<>();
        if(ids != null){
        for (int i = 0; i < ids.length; i++) {
            playerlist.remove(LabUtils.FindRoomUser(chater.getRoomId(),ids[i]));
        }}
        for (RoomUser roomUser:playerlist){
            RoomUserDTO roomUserDTO = new RoomUserDTO();
            roomUserDTO.setNickname(roomUser.getNickname());
            roomUserDTO.setSeat(roomUser.getSeat());
            roomUserDTO.setUserId(roomUser.getUserId());
            players.add(roomUserDTO);
        }
        String s=new Gson().toJson(players);
        chater2.setRoomId(chater.getRoomId());
        chater2.setUserId(chater.getUserId());
        chater2.setMessage("SUCCEED");
        chater2.setObject(s);
        chater2.setOrder("getPlayerList");
        return chater2;
    }
    @RequestMapping("/werewolf")
    @ResponseBody
    public Chater BeginWerewolf(Chater chater,String[] ids) {
        String obj =  chater.getObject().toString();
        Werewolf werewolf=new Gson().fromJson(obj,Werewolf.class);
        Room room=LabUtils.FindRoom(chater.getRoomId());
        List<RoomUser> playerlist = room.getUserlist();
        if (ids != null) {
            for (int i = 0;i < ids.length; i++) {
                playerlist.remove(LabUtils.FindRoomUser(chater.getRoomId(),ids[i]));
            }
        }
        Chater chater1 = new Chater();
        Chater chater2 = new Chater();
        chater2.setOrder("werewolf");
        chater2.setRoomId(chater.getRoomId());
        Map<String,Boolean> playerboolean=new HashMap<>();
        //上帝
        if (werewolf.getGod() == null) {
            werewolf.setGod(chater.getUserId());
        }
        int num=1;
        //预言家
        if (werewolf.getSeerIs()) {
            num++;
        }
        //女巫
        if (werewolf.getWitchIs()) {
            num++;
        }
        //猎人
        if (werewolf.getHunterIs()) {
            num++;
        }
        //盗贼
        if (werewolf.getThiefIs()) {
            num++;
        }
        //白痴
        if (werewolf.getIdiotIs()) {
            num++;
        }
        //丘比特
        if (werewolf.getCupidIs()) {
            num++;
        }
        //守卫
        if (werewolf.getGuardIs()) {
            num++;
        }
        //小女孩
        if (werewolf.getThiefIs()) {
            num++;
        }
        //长老
        if (werewolf.getPresbyterIs()) {
            num++;
        }
         if(num+werewolf.getWerewolfnum()+werewolf.getVillagernum()==playerlist.size()){
            chater1.setMessage("WRONG NUMBER");
            return chater1;
         }

        chater2.setMessage("God");
        playerlist.remove(LabUtils.FindRoomUser(chater.getRoomId(),werewolf.getGod()));
        room.sendSingle(chater2, LabUtils.FindRoomUser(chater.getRoomId(),werewolf.getGod()).getSession());
        //预言家
        if (werewolf.getSeerIs()) {
            chater2.setMessage("预言家");
            playerlist=createPlayer(chater2,playerlist);
        }
        //女巫
        if (werewolf.getWitchIs()) {
            chater2.setMessage("女巫");
            playerlist=createPlayer(chater2,playerlist);
        }
        //猎人
        if (werewolf.getHunterIs()) {
            chater2.setMessage("猎人");
            playerlist=createPlayer(chater2,playerlist);
        }
        //盗贼
        if (werewolf.getThiefIs()) {
            chater2.setMessage("盗贼");
            playerlist=createPlayer(chater2,playerlist);
        }
        //白痴
        if (werewolf.getIdiotIs()) {
            chater2.setMessage("白痴");
            playerlist=createPlayer(chater2,playerlist);
        }
        //丘比特
        if (werewolf.getCupidIs()) {
            chater2.setMessage("丘比特");
            playerlist=createPlayer(chater2,playerlist);
        }
        //守卫
        if (werewolf.getGuardIs()) {
            chater2.setMessage("守卫");
            playerlist=createPlayer(chater2,playerlist);
        }
        //小女孩
        if (werewolf.getThiefIs()) {
            chater2.setMessage("小女孩");
            playerlist=createPlayer(chater2,playerlist);
        }
        //长老
        if (werewolf.getPresbyterIs()) {
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
        chater1.setMessage("SUCCEED");
        chater1.setOrder("werewolf");
        return chater1;
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
    private String CheckHost(String roomId,String userId){
        if(LabUtils.FindRoom(roomId).getHostId().equals(userId)){
            return "SUCCEED";
        }
        return "Not Host";
    }
}
