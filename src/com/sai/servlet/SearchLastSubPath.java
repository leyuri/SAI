package com.sai.servlet;

import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchLastSubPath {
	
	static String shortT, minT, shtStateName, minStateName;

	static public JSONObject findPath(String sub) {
		
		String lastSub = sub;
		String shtState = "";
		
		JSONObject result = new JSONObject();

		for(int i = 0; i < SearchTenSub.friends.length; i++) {

			try {
				String url = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/shortestRoute/0/1/";
				String fURL = URLEncoder.encode(SearchTenSub.friends[i], "UTF-8");
				String sURL = URLEncoder.encode(lastSub, "UTF-8");
				String pathURL = url + fURL + "/" + sURL;

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(pathURL);

				document.getDocumentElement().normalize();

				NodeList shortList = document.getElementsByTagName("row");

				//소요시간 구하는 식
				for(int k = 0; k < shortList.getLength(); k++){
					Node nNode = shortList.item(k);
					if(nNode.getNodeType() == Node.ELEMENT_NODE){

						Element eElement = (Element) nNode;

						NodeList shtStatnId = eElement.getElementsByTagName("shtStatnId").item(0).getChildNodes();
						Node shtStatnIdInfo = (Node) shtStatnId.item(0);
						shtState = shtStatnIdInfo.getNodeValue();
						
						NodeList shtStatnNm = eElement.getElementsByTagName("shtStatnNm").item(0).getChildNodes();
						Node shtStatnNmInfo = (Node) shtStatnNm.item(0);
						shtStateName = shtStatnNmInfo.getNodeValue();
						
						NodeList minStatnId = eElement.getElementsByTagName("minStatnId").item(0).getChildNodes();
						Node minStatnIdInfo = (Node) minStatnId.item(0);
						String minState = minStatnIdInfo.getNodeValue();
						
						NodeList minStatnNm = eElement.getElementsByTagName("minStatnNm").item(0).getChildNodes();
						Node minStatnNmInfo = (Node) minStatnNm.item(0);
						minStateName = minStatnNmInfo.getNodeValue();
		
						NodeList shtTravelTm = eElement.getElementsByTagName("shtTravelTm").item(0).getChildNodes();
						Node shtTrabelInfo = (Node) shtTravelTm.item(0);
						shortT = shtTrabelInfo.getNodeValue();
						
						NodeList minTravelTm = eElement.getElementsByTagName("minTravelTm").item(0).getChildNodes();
						Node minTravelTmInfo = (Node) minTravelTm.item(0);
						minT = minTravelTmInfo.getNodeValue();

					}
					
				}
				
				JSONObject res = new JSONObject();
				
				res = MakePathToJson.makeJson(shtState);
				res.put("shtTravelTm", shortT);
				res.put("minTravelTm", minT);
				
				String key = SearchTenSub.friends[i];
				result.put(key, res);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return result;		
	}

}
