package com.sai.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchTenSub {
	
	static String statName[] = new String[10]; 
	//friends배열에는 지하철역명이 들어가야함.
	static String friends[] = new String[3]; //동적으로 생성해야함.
	static String lastSub = "";
	
	public static JSONObject search(double mapX, double mapY) {
		
		JSONObject result = new JSONObject();
		JSONObject place = new JSONObject();
		
		try{

			String locURL = "http://swopenapi.seoul.go.kr/api/subway/5947735178616c77393747474d664c/xml/nearBy/0/10/" + mapX + "/" + mapY;

			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(locURL);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("row");

			for(int i = 0; i < nList.getLength(); i++){
				Node nNode = nList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){

					Element eElement = (Element) nNode;

					NodeList statnNm = eElement.getElementsByTagName("statnNm").item(0).getChildNodes();
					Node statnNmInfo = (Node) statnNm.item(0);
					String state = statnNmInfo.getNodeValue();

					NodeList subwayNm = eElement.getElementsByTagName("subwayNm").item(0).getChildNodes();
					Node subwayNmInfo = (Node) subwayNm.item(0);
					String subway = subwayNmInfo.getNodeValue();

					NodeList subwayXcnts = eElement.getElementsByTagName("subwayXcnts").item(0).getChildNodes();
					Node subwayXcntsInfo = (Node) subwayXcnts.item(0);
					String subwayX = subwayXcntsInfo.getNodeValue();

					NodeList subwayYcnts = eElement.getElementsByTagName("subwayYcnts").item(0).getChildNodes();
					Node subwayYcntsInfo = (Node) subwayYcnts.item(0);
					String subwayY = subwayYcntsInfo.getNodeValue();

					//역 10개 배열에 삽입
					statName[i] = state;
				}
			}

			lastSub = ChooseLastSub.getTotalTime();
			result = SearchLastSubPath.findPath(lastSub);
			
			System.out.println(result);
			
			place = SearchMiddlePlace.getHotPlace();
			
		} catch (Exception e){   
			e.printStackTrace();
		}
		
		return result; //!결과값이랑 플레이스 목록이 같이 리턴되어야 함.
	} 
}
