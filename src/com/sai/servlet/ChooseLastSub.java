package com.sai.servlet;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ChooseLastSub {
	static String lastSub = "";

	public static String getTotalTime() {
		double sum = 0;
		double avg = 0;
		double var = 0;
		double std = 0;
		double totalTime[] = new double[10];
		double time = 0;
		double sevTime[] = new double[SearchMiddlePoint.friends.length];
		double totalSd[] = new double[10];
		double total[] = new double[10];
		HashMap result = new HashMap();

		//10개의 역에 대한 여러명의 소요시간을 구하는 식
		for(int i = 0; i < SearchTenSub.statName.length; i++) {
			//하나의 역마다 여러 명의 소요시간을 구하는 식
			for(int j = 0; j < SearchTenSub.friends.length; j++) {

				try {			

					String url = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/shortestRoute/0/1/";
					String fURL = URLEncoder.encode(SearchTenSub.friends[j], "UTF-8");
					String sURL = URLEncoder.encode(SearchTenSub.statName[i], "UTF-8");
					String shortURL = url + fURL + "/" + sURL;

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(shortURL);

					document.getDocumentElement().normalize();

					NodeList shortList = document.getElementsByTagName("row");

					//소요시간 구하는 식
					for(int k = 0; k < shortList.getLength(); k++){
						Node nNode = shortList.item(k);
						if(nNode.getNodeType() == Node.ELEMENT_NODE){

							Element eElement = (Element) nNode;

							NodeList shtTravelTm = eElement.getElementsByTagName("shtTravelTm").item(0).getChildNodes();
							Node shtTrabelInfo = (Node) shtTravelTm.item(0);
							String shtTime = shtTrabelInfo.getNodeValue();

							time = Double.parseDouble(shtTime);
							totalTime[i] += time;
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}	
				sevTime[j] = time;
			}

			//한 역에 대한 여러 명의 총 소요시간을 인원 수로 나눈 결과(=평균)
			avg = totalTime[i] / SearchTenSub.friends.length;

			sum = 0; //초기화
			for(int l = 0; l < SearchTenSub.friends.length; l++) {
				sum += Math.pow(sevTime[l] - avg, 2.0);
			}
			
			//분산 구하는 식
			var = sum / SearchTenSub.friends.length;

			//표준 편차 구하는 식
			std = Math.sqrt(var);
			totalSd[i] = std;
			
			//순위 매기기 위한 표준 편차 + 총 소요 시간
			total[i] = totalSd[i] + totalTime[i];
			
			//매핑을 위해 hashMap 사용
			result.put(total[i], SearchTenSub.statName[i]);
		}	
		
		//오름차순으로 정렬
		Arrays.sort(total);
		
		//가장 첫번째 인덱스의 결과값이 최소이므로 최종역
		lastSub = (String)result.get(total[0]);
		System.out.println(lastSub);
		System.out.println("최종 추천역: " + result.get(total[0]));
		
		//최종역 반환
		return lastSub;
	}
}




