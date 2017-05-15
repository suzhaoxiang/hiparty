package com.controller;

import com.beans.Chater;
import com.beans.Punishment;
import com.beans.Room;
import com.google.gson.Gson;
import com.utils.HibernateUtil;
import com.utils.LabUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15.
 */
@Controller
@RequestMapping("/punishment")
public class PunishmentController {

    @RequestMapping("/list")
    @ResponseBody
    public Chater listPunishMent(String level){
        Chater chater =  new Chater();
        if (level!=null){
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session=sessionFactory.getCurrentSession();
            session.beginTransaction();
            List<Punishment> punishmentlist = session.createQuery("from Punishment where punishmentLevel=:punishmentLevel")
                    .setParameter("punishmentLevel", level).list();
            session.getTransaction().commit();
            chater.setOrder("punishment");
            Map<String, Object> object = new HashMap<>();
            object.put("size", String.valueOf(punishmentlist.size()));
            object.put("list", new Gson().toJson(punishmentlist));
            chater.setObject(object);
            chater.setMessage("SUCCEED");
        }


        return chater;
    }

    @ResponseBody
    @RequestMapping("/submit")
    public Chater submit(Chater chater){
        if (chater!=null){
            String userId = chater.getUserId();
            String roomId = chater.getRoomId();
            String punishmentId =(String) chater.getObject();

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session=sessionFactory.getCurrentSession();
            session.beginTransaction();
            Punishment punishment = (Punishment) session.createQuery("from Punishment where punishmentId=:punishmentId")
                    .setParameter("punishmentId", punishmentId)
                    .uniqueResult();
            session.getTransaction().commit();

            Map<String, Object> object2 = new HashMap<>();
            if(LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId())==null){
                System.out.println("no this room");
            }
            String nickname = LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()).getNickname();
            System.out.println(nickname);
            if(nickname!=null&&!nickname.equals("")){
                object2.put("punishment", nickname+":"+punishment.getPunishment());
            }
            else{
                object2.put("punishment", chater.getUserId()+":"+punishment.getPunishment());
            }

            Chater chater2 = new Chater();
            chater2.setObject(object2);
            chater2.setMessage("SUCCEED");
            chater2.setOrder("ensure_punishment");
            chater2.setRoomId(chater.getRoomId());
            Room room = LabUtils.FindRoom(chater.getRoomId());
            room.sendAll(chater2);
        }
        chater.setMessage("SUCCEED");
        return chater;
    }

}
