package com.utils;

import com.beans.Chater;
import com.beans.Room;
import com.google.gson.Gson;
import org.apache.mina.core.session.IoSession;

import java.util.Date;
import java.util.List;

public class RoomUtils {

	private Date introducestart= new Date();
	private int count;

	public Date getIntroducestart() {
		return introducestart;
	}
	public void setIntroducestart(Date introducestart) {
		this.introducestart = introducestart;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

}
