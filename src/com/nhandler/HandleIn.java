package com.nhandler;

import com.beans.Chater;
import com.beans.Json;
import com.beans.Room;
import com.beans.RoomUser;
import com.nhandler.handlerImpl.HandlerInterface;
import com.utils.LabUtils;
import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/26.
 */
public class HandleIn implements HandlerInterface{

    @Override
    public String getOrder() {
        return "in";
    }

    @Override
    public Json handle(IoSession iossession, Chater chater) {

            //先设定该房员属性
            RoomUser roomuser = new RoomUser();
            roomuser.setSession(iossession);
            roomuser.setUserId(chater.getUserId());
            //找到此房间，并将此房员加入列表
            //设定返回的chater2
            Chater chater2= new Chater();
            //此时利用chater中的obj传送roomId
            Map<String,Object> obj = new HashMap<>();
            obj=(Map<String, Object>)chater.getObject();
            String roomId=(String) obj.get("roomId");
            Room room =LabUtils.FindRoom(roomId);

            Chater chater3=new Chater();
            chater3.setMessage(chater.getUserId()+"已经进入房间");
            chater3.setOrder("talk_in");
            chater3.setUserId(chater.getUserId());
            chater3.setRoomId(roomId);

            if(room==null){
                chater2.setMessage("No Room");
            }
            else{
                if(LabUtils.FindRoomUser(roomId, chater.getUserId())!=null){
                    chater2.setMessage("Already In");
                }
                //无错误
                chater2.setMessage("SUCCEED");
                room.getUserlist().add(roomuser);
                room.setUserlist(room.getUserlist());
                room.setRoomnum(room.getRoomnum()+1);
                roomuser.setNickname("考拉"+room.getRoomnum());

                System.out.println(room.getRoomnum());

                room.sendAll(chater3);
            }
            Map<String,Object> object=new HashMap<>();
            object.put("roomName", room.getRoomname());
            chater2.setObject(object);
            chater2.setOrder("in");
            chater2.setRoomId(roomId);
            chater2.setUserId(chater.getUserId());

            room.sendSingle(chater2,iossession);
            room.sendSingle(chater3,iossession);

        return null;
    }
}
