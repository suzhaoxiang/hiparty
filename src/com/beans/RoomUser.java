package com.beans;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
import com.utils.RoomUserUtils;
import org.apache.mina.core.session.IoSession;

import java.io.Serializable;

//@Entity
//@Table(name="roomuser")
public class RoomUser implements Serializable{
	
	private String userId;//用户id
	private String nickname;//昵称
	private int seat;//座位号
	private IoSession session;

	public RoomUser() {
		super();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}
}
