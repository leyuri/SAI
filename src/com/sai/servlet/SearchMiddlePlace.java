package com.sai.servlet;

import java.awt.Image;
import java.awt.Toolkit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchMiddlePlace {
	
	public static JSONObject getHotPlace() {
		
		JSONObject place = new JSONObject();
		
		try {

			String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=LzJ73NnpJ9i8FwimSqcbaJpLp6x9nN4TCDnDBSPhf8TEA05I5fi6G%2FIhjRdbQcD5FZ%2FH778Vpm4vE%2F9OTB6D6Q%3D%3D&mapX=" 
					+ MakePathToJson.mapX + "&mapY=" + MakePathToJson.mapY + "&radius=1000&listYN=Y&arrange=A&MobileOS=ETC&MobileApp=AppTest&arrange=B";

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document doc = parser.parse(url);
			Element eRoot = doc.getDocumentElement();

			System.out.println(eRoot.getTagName());

			NodeList items = doc.getElementsByTagName("item");

			for(int i = 0; i < items.getLength(); i++) {
				Node itemNode = items.item(i);

				Element itemElmnt = (Element)itemNode;

				NodeList addr1 = itemElmnt.getElementsByTagName("addr1").item(0).getChildNodes();
				Node addr1Info = (Node) addr1.item(0);
				String add1 = addr1Info.getNodeValue();

				NodeList firstImage = itemElmnt.getElementsByTagName("firstimage").item(0).getChildNodes();
				Node firstImageInfo = (Node) firstImage.item(0);
				String firstIm = firstImageInfo.getNodeValue();
				Image image = Toolkit.getDefaultToolkit().getImage(firstIm);

				NodeList readcount = itemElmnt.getElementsByTagName("readcount").item(0).getChildNodes();
				Node readcountInfo = (Node) readcount.item(0);
				String read = readcountInfo.getNodeValue();

				if(itemElmnt.getElementsByTagName("title").item(0).getChildNodes().getLength() != 0) {
					NodeList title = itemElmnt.getElementsByTagName("title").item(0).getChildNodes();
					Node titleInfo = (Node) title.item(0);
					String title1 = titleInfo.getNodeValue();

					System.out.println("이름: " + title1);
				}
				
				if(itemElmnt.getElementsByTagName("tel").item(0).getChildNodes().getLength() != 0) {
					NodeList tel = itemElmnt.getElementsByTagName("tel").item(0).getChildNodes();
					Node telInfo = (Node) tel.item(0);
					String tele = telInfo.getNodeValue();

					System.out.println("전화번호: " + tele);
				}
				
				System.out.println("이미지: " + image);
				System.out.println("위치: " + add1);
				System.out.println("조회수: " + read);

				System.out.println("----------------------------------------------");

			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
}
