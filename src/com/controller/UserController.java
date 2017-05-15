package com.controller;

import java.io.*;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beans.*;
import com.google.gson.Gson;
import com.utils.LabUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utils.HibernateUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="/user")
public class UserController {

	private SessionFactory sessionFactory=HibernateUtil.getSessionFactory();
	//用户登录
	@RequestMapping(value="/login")
	@ResponseBody
 	public Chater UserLogin(String userId,String password){
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		User user = (User) session.createQuery("from User where userId=:userId")
		.setParameter("userId", userId)
		.uniqueResult();
		session.getTransaction().commit();
		Chater chater = new Chater();
		chater.setUserId(userId);
		chater.setOrder("login");

		if(user==null){
			chater.setMessage("NO USER");
			return chater;
		}
		if(!user.getPassword().equals(password)){
			chater.setMessage("PASSWORD FAILED");
			return chater;
		}
		chater.setMessage("SUCCEED");


		return chater;
	}
	//用户注册
	@RequestMapping(value="/regist")
	@ResponseBody
	public Chater UserRegist(String userId,String username,String password){

		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		User user = (User) session.createQuery("from User where userId=:userId")
		.setParameter("userId", userId)
		.uniqueResult();
		Chater chater = new Chater();
		chater.setUserId(userId);
		chater.setOrder("regist");

		if(user!=null){
			chater.setMessage("You have already Registed");
			return chater;
		}

		user =new User();
		user.setPassword(password);
		user.setUserId(userId);
		user.setUsername(username);
		session.save(user);
		session.getTransaction().commit();
		chater.setMessage("SUCCEED");
		return chater;
	}


	@RequestMapping("/download")
    @ResponseBody
	public Chater downloadFile(String url,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="+url);
		try {
			File file=new File(url);
			System.out.println(file.getAbsolutePath());
			InputStream inputStream= null;
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			OutputStream os=response.getOutputStream();
			byte[] b=new byte[1024];
			int length;
			while((length=inputStream.read(b))>0){
				os.write(b,0,length);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Chater chater=new Chater();
		chater.setOrder("download");
		chater.setMessage("SUCCEED");
		return chater;
	}

    @RequestMapping("/upload")
    @ResponseBody
    public Chater uploadFile(HttpServletRequest request,MultipartFile file){
        //获取上传文件的名称
        String filename = file.getOriginalFilename();
		String path = this.getClass().getClassLoader().getResource("/").getPath();
		File file2 = new File(path+"loadfile");
        if (!file2.exists()) {
            //创建临时目录
            file2.mkdir();
        }
        try {
            //文件存放的路径
        FileOutputStream fileOutputStream = new FileOutputStream(file2+"/"+filename);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		String type =request.getContentType();

		Session session=sessionFactory.getCurrentSession();
        session.beginTransaction();
		LoadFile loadFile=new LoadFile();
		loadFile.setFilename(filename);
		loadFile.setFileUrl(path+"loadfile"+filename);
		loadFile.setType(type);
		session.save(loadFile);
		session.getTransaction().commit();

		Chater chater =new Chater();
		chater.setMessage("SUCCEED");
		chater.setOrder("upload");
		return  chater;
	}

	@RequestMapping("/out")
	@ResponseBody
	public Chater out(Chater chater){
		Chater chater1 = new Chater();
		if (chater == null||chater.getUserId() == null||chater.getRoomId() == null){
			chater1.setMessage("failed");
			return chater1;
		}
		if (LabUtils.FindRoomUser(chater.getRoomId(),chater.getUserId()) == null){
			chater1.setMessage("failed");
			return chater1;
		}
		Room room=new Room();
		room=LabUtils.FindRoom(chater.getRoomId());
		room.getUserlist().remove(LabUtils.FindRoomUser(chater.getRoomId(), chater.getUserId()));
		room.setRoomnum(room.getRoomnum()-1);
		Chater chater3=new Chater();
		chater3.setMessage(chater.getUserId()+"已经退出房间");
		chater3.setOrder("talk_out");
		chater3.setUserId(chater.getUserId());
		chater3.setRoomId(chater.getRoomId());
		room.sendAll(chater3);
		chater1.setMessage("SUCCEED");
		return chater1;

	}

}
