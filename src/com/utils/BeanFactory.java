package com.utils;

import com.beans.Chater;
import com.nhandler.HandleCreate;
import com.nhandler.handlerImpl.HandlerInterface;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jdom2.xpath.jaxen.JaxenXPathFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lurunfa on 2017/4/24.
 *
 * @author lurunfa
 * @version 1.0
 */
public class BeanFactory {

    Map<String,Object> map = new HashMap<>();
    public BeanFactory(String path) throws Exception {
        SAXBuilder sb=new SAXBuilder();
        Document doc=sb.build(getClass().getClassLoader().getResourceAsStream(path));
        XPathFactory factory = JaxenXPathFactory.instance();
        XPathExpression<Element> expression = factory.compile("//beans//bean", Filters.element());
        List<Element> list = expression.evaluate(doc);
        for (Element element:list){
            String id = element.getAttributeValue("id");
          //  System.out.println(id);
            String clazz = element.getAttributeValue("class");
          //  System.out.println(clazz);
            Object o = Class.forName(clazz).newInstance();
            map.put(id,o);
        }
    }
    public Object getObject(String id) throws Exception {
        Object o = map.get(id);
        System.out.println("getObject");
        if (o == null){
     //       throw new Exception("没有"+id+"的类");
            System.out.println("没有"+id+"的类");
            return null;
        }
        return map.get(id);
    }

    public static void main(String[] args) {
        Chater chater=new Chater();
        Map<String,String> map=new HashMap<>();
        map.put("roomName","ero");
        chater.setObject(map);
        chater.setOrder("create");
        chater.setRoomId("111");
        chater.setUserId("123");
        try {
            BeanFactory beanFactory=new BeanFactory("com/nhandler/bean.xml");
            HandlerInterface handle = (HandlerInterface) beanFactory.getObject(chater.getOrder());
            handle.handle(null,chater);
            System.out.println(handle.getOrder());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
