package com.jerry.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpSend {
	public static void main(String[] args) throws Exception {
		(new HttpSend()).run();
	}

	private void run() throws Exception {
		URI url = new URI("http://127.0.0.1:8080/test/api.htm");
		HttpPost request = new HttpPost(url );
		// set heads
		String appkey="123456";
		String coop_id="C0001";
		request.addHeader("coop_id", coop_id);
		request.addHeader("req_id", "REQ000001");
		request.addHeader("send_time", "1494405810");
		// json obj
		JSONObject param = new JSONObject();
		param.put("loanId", "e700260bf1554799a81b67a3a8a2d189");
		param.put("name", "鲁小帅");
		param.put("account", "13400010001");
		
		// set data block
		ByteArrayEntity se = new ByteArrayEntity(param.toString().getBytes());
		request.setEntity(se);
//		request.
		
		//create sign
		byte[] head=new String(coop_id+appkey).getBytes("UTF-8");
		byte[] data=contentReader(se);
		byte[] all = new byte[head.length + data.length];  
	    System.arraycopy(head, 0, all, 0, head.length);  
	    System.arraycopy(data, 0, all, head.length, data.length);
	    // set sign
	    request.addHeader("sign", com.jerry.test.MD5Utils.md5(all));
		
		// send request
		HttpResponse httpResponse =  new DefaultHttpClient().execute(request);
		// response
		String retSrc = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("response-->"+retSrc);
	}
	
	 private byte[] contentReader(AbstractHttpEntity request) {
	    	try {
				InputStream si = request.getContent();
				byte[] buffer = new byte[(int) request.getContentLength()];
				si.read(buffer, 0, (int)request.getContentLength());
				return buffer;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	    	}
}
