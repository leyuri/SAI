package com.sai.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sai.dao.MemberDAO;

public class AddLikePlace implements Action {
	
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contentId = null;
		String contentTypeId = null;
		String areaCode = null;
		String firstImage = null;
		String title = null;
		String readCount = null;
		String addr1 = null;
		String addr2 = null;
		String tel = null;
		Double mapX = null;
		Double mapY = null;
		
		System.out.println("좋아요 페이지 연결");
		
		String data = request.getParameter("data");
		System.out.println(data.toString());

		/*String fData = data.replace("[\"", "");
		String SData = fData.replace("]", "");
		String str[] = SData.split("\",\"");
		
		for(int i = 0; i < str.length; i++) {
			System.out.println(str[i]);
		}*/
		
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		try {
			json = (JSONObject) parser.parse(data);
			contentId = (String) json.get("contentId");
			contentTypeId = (String) json.get("contentTypeId");
			areaCode = (String) json.get("areaCode");
			firstImage = (String) json.get("firstImage");
			System.out.println(firstImage);
			title = (String) json.get("title");
			readCount = (String) json.get("readCount");
			addr1 = (String) json.get("addr1");
			addr2 = (String) json.get("addr2");
			tel = (String) json.get("tel");
			mapX = Double.parseDouble((String) json.get("mapX"));
			mapY = Double.parseDouble((String) json.get("mapY"));
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		session = request.getSession();
		String sessId = (String) session.getAttribute("sessId");

		System.out.println(sessId);
		
		new MemberDAO().addLikePlace(sessId, contentId, contentTypeId, areaCode, firstImage, title, readCount, addr1, addr2, tel, mapX, mapY);
		
		JSONObject test = new JSONObject();
		
		PrintWriter out = response.getWriter();
		out.println(json);
		
		return null;
		
	}

}
