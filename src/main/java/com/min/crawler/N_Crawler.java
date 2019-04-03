package com.min.crawler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class N_Crawler {
	String path="C:\\Users\\40270\\Documents\\pic\\";
	
	@Test
	public void start() throws IOException {
		
		List<String> urllist=new ArrayList<String>();

		for(String book:urllist) {
			String bookurl="https://nhentai.net/g/"+book;

			Map<String,String> headers=new HashMap<String,String>();
			
			headers.put("authority", "nhentai.net");
			headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			headers.put("user-agent", "Mozilla/5.0");
			
			Connection conn=Jsoup.connect(bookurl).headers(headers);
			
			//连接获取dom
			Document document=conn.get();
			//抽取img元素
			Elements nodes=document.select("a[class='gallerythumb']>img");
			//总页数
			int counts=nodes.size();
			//获取图片src
			String src=nodes.attr("data-src");
			//获取图片格式
			String type=src.substring(src.lastIndexOf("."),src.length());
			
			//获取路径中具体名称
			String pattern="(\\d+)";
			Pattern r=Pattern.compile(pattern);
			
			Matcher m=r.matcher(src);
			//匹配子串
			m.find();

			String name=m.group();
			//设置保存路径，以名称作文件夹名
			File f=new File(path+name);
			if(!f.exists())
				f.mkdir();
			
			String imageurl="https://i.nhentai.net/galleries/";
			
			FileOutputStream file=null;
			InputStream inputStream=null;
			BufferedInputStream bis=null;
			
			for(int i=1;i<=counts;i++) {
				//每一张图片的具体url
				String imageUrl=imageurl+name+"/"+i+type;
				
				try {
					URL link=new URL(imageUrl);
					URLConnection urlConn=link.openConnection();
					inputStream=urlConn.getInputStream();
					
					bis=new BufferedInputStream(inputStream);
					
					byte[] b=new byte[2048];
					
					int len;
					
					//图片保存路径以及命名
					File imageName=new File(f.toString()+"\\"+i+type);
					//存在则跳过
					if(imageName.exists())
						continue;
					
					System.out.println(name+":第"+i+"张");
					file=new FileOutputStream(imageName);
					
					//保存
					while((len=inputStream.read(b))!=-1) {
						file.write(b,0,len);
					}
					file.close();
					
					
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}	
			}
		}
	}

}
