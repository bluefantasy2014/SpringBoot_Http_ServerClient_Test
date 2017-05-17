/* 
 * AccessController.java  
 * 
 * version TODO
 *
 * 2016年5月17日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package xm.web.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author Bruce
 *
 */
@Controller
@EnableAutoConfiguration
@SpringBootApplication
public class ApiController {
	public static void main(String[] args)  {
		SpringApplication.run(ApiController.class, args);
	}
	
	// test url
    @RequestMapping("/test/api.htm")
    public ModelAndView mock(HttpSession httpSession, HttpServletResponse response,HttpServletRequest request) throws Exception{
        // print all heads
    	@SuppressWarnings("rawtypes")
		Enumeration headerNames = request.getHeaderNames();
    	while (headerNames.hasMoreElements()) {
    		String element =headerNames.nextElement().toString(); 
    	    System.out.println(element + "\t" + request.getHeader(element));
    	}
    	// recv data by binary
    	byte[] data = contentReader(request);
    	
    	// verify sign
    	byte[] head=new String(request.getHeader("coop_id")+"123456").getBytes("UTF-8");
		byte[] all = new byte[head.length + data.length];  
	    System.arraycopy(head, 0, all, 0, head.length);  
	    System.arraycopy(data, 0, all, head.length, data.length);
	    String sign = request.getHeader("sign");
	    String mySign=MD5Utils.md5(all);
    	// print result
    	System.out.println("远程收到的签名-->"+sign);
    	System.out.println("本地计算的签名-->"+mySign);
    	System.out.println(sign.equals(mySign) ? "签名一致" : "签名不一致");
    	
         return null;
    }
    
    private byte[] contentReader(HttpServletRequest request) {
    	try {
			ServletInputStream si = request.getInputStream();
			byte[] buffer = new byte[request.getContentLength()];
			si.read(buffer, 0, request.getContentLength());
			return buffer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    	}
}
