package com.controller;

import com.beans.Chater;
import com.beans.Json;
import com.beans.Lab;
import com.beans.RoomUser;
import com.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */
@RequestMapping("/Test")
@ResponseBody
public class TestController {
    private SessionFactory sessionFactory= HibernateUtil.getSessionFactory();
    @RequestMapping("/test")
    @ResponseBody
    public Chater Test() {
        RoomUser roomuser=new RoomUser();
        roomuser.setUserId("123");
        roomuser.setNickname("nick");

        Chater chater=new Chater();
        chater.setRoomId("111");
        chater.setUserId("123");
        chater.setOrder("getlist");
        Map<String,Object> map = new HashMap<>();
        map.put("type","体育");
        chater.setObject(map);
        Lab lab=Lab.getLab();

        Map<String,Object> objectMap= (Map<String, Object>) chater.getObject();
        String type= (String) objectMap.get("type");
        String path = this.getClass().getClassLoader().getResource("/").getPath();
        System.out.println(path);
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
        Map<String,Object> Wmap = new HashMap<>();
        Wmap.put("wordList",sendList);
        chater2.setObject(Wmap);
        return chater2;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Json testAdd(){
        return null;
    }

}
