package com.controller;

import com.beans.Chater;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/5/14.
 */
@RequestMapping("/download")
@Controller
public class DownloadController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Chater downloadFile(String date, HttpServletResponse response, HttpServletRequest request){

        String remoteAddr = request.getRemoteAddr();
        System.out.println("from ip:"+remoteAddr+" download");
        String url="C:/Users/Administrator/Desktop/ftp/apk/"+date+".apk";
        response.reset();
        response.setContentType("application/octet-stream");// 设置response内容的类型
        try {
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode("hiparty.apk", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
           // e.printStackTrace()
            System.out.println("error");
        }
        Chater chater =new Chater();
        chater.setMessage("SUCCEED");
        return chater;
    }

    @RequestMapping("/download2")
    @ResponseBody
    public Chater download(HttpServletResponse response,String date) {
        InputStream ins = null;
        BufferedInputStream bins = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        try {
            String path="C:/Users/Administrator/Desktop/ftp/apk/"+date+".apk";
            File file = new File(path );
            String finalZipName = "hiparty.apk";
            ins = new FileInputStream(file);
            bins = new BufferedInputStream(ins);// 放到缓冲流里面
            outs = response.getOutputStream();// 获取文件输出IO流
            bouts = new BufferedOutputStream(outs);
            response.reset();
            response.setContentType("application/octet-stream");// 设置response内容的类型
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(finalZipName, "UTF-8"));// 设置头部信息
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }
            bouts.flush();
//            System.out.println(bouts.toString());
        } catch (IOException e) {
            System.out.println("文件下载失败");
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bins != null) {
                try {
                    bins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bouts != null) {
                try {
                    bouts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Chater chater = new Chater();
        chater.setMessage("download");
        return chater;
    }



}
