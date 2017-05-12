package com.nhandler;

import com.beans.*;
import com.nhandler.handlerImpl.HandlerInterface;
import com.utils.HibernateUtil;
import org.apache.mina.core.session.IoSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/24.
 */
public class HandleCreate implements HandlerInterface{

    @Override
    public String getOrder() {
        return "create";
    }

    @Override
    public Json handle(IoSession iossession, Chater chater) {

        Lab lab=Lab.getLab();
        Map<String,Object> obj = new HashMap<>();
        obj=(Map<String, Object>)chater.getObject();
        if (obj == null) {
            return null;
        }
        String roomname=(String) obj.get("roomName");
        RoomUser roomuser = new RoomUser();
        Room room = new Room();

        String suf= String.valueOf((int)(Math.random()*1000));//通过当前时间产生一个随机数作为后缀
        SessionFactory sessionFactory =  HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        int pre = (int) session.createQuery("select id from User where userId=:userId")
                .setParameter("userId", chater.getUserId())
                .uniqueResult();

        session.getTransaction().commit();

        //设定room的属性
        room.setRoomname(roomname);
        System.out.println(roomname+"设置");
        room.setRoomId(pre+suf);
        Date date = new Date();
        room.setStarttime(date);
        room.setRoomnum(1);
        room.setHostId(chater.getUserId());

        //设定创建人属性
        roomuser.setSession(iossession);
        roomuser.setUserId(chater.getUserId());
        roomuser.setNickname("考拉"+room.getRoomnum());
        room.getUserlist().add(roomuser);
        lab.setList(room);

        //设定返回的chater2
        Chater chater2= new Chater();
        chater2.setObject(obj);
        chater2.setRoomId(room.getRoomId());
        chater2.setUserId(chater.getUserId());
        chater2.setMessage("SUCCEED");
        chater2.setOrder("create");
        room.sendSingle(chater2,iossession);
        System.out.println("End");
        return null;
    }
}
