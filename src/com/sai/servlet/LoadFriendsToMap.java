package com.sai.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.json.simple.JSONObject;

import com.sai.dao.MemberDAO;
import com.sai.vo.MemberVO;

public class LoadFriendsToMap implements Action {

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		System.out.println("서블릿 테스트");

		String sessId = (String) session.getAttribute("sessId");
		System.out.println(sessId);

		String[] friendsId = null;
		
		//알고리즘에 참여한 memberI d값들을 담는 JSONObject
		JSONObject friendsInfo = new JSONObject();
		//알고리즘에 참여한 member들의 지하철역의 이름을 담은 JSONObject
		JSONObject stationList = new JSONObject();
		
		JSONObject result = new JSONObject();
		//loadFriendsMap()을 통해서 friendsId배열에 친구들 ID값들 담음
		friendsId = new MemberDAO().loadFriendsMap(sessId); 
		
		//searchFriendInfo()를 통해서 알고리즘에 참여한 멤버의 memberId, memberName, memberStation을 fInfo에 삽입
		for(int i = 0; i < friendsId.length; i++) {
			JSONObject fInfo = new MemberDAO().searchFriendInfo(friendsId[i]);
			//검색한 모든 친구들의 데이터(memberId, memberName, memberStation)를 friendsInfo에 넣음.
			//friendsInfo.put(i, fInfo); 

			//System.out.println(friendsInfo);

			//friendsId JSONObject에서 객체를 하나씩 꺼내 그 안의 memberStation 정보를 꺼내옴
			//for(int j = 0; j < friendsId.length; j++) {
			String fStation = (String) fInfo.get("memberStation");

			//꺼내온 지하철역명으로 해당 역의 x, y 좌표를 얻어옴.
			JSONObject station = new MemberDAO().searchLocation(fStation);
			System.out.println("station 값은 " + station);

			//stationList에 각각의 역 좌표를 삽입함.
			//stationList.put(fStation, station);

			//System.out.println(stationList.get("덕정"));

			String data1 = (String) fInfo.get("memberId");
			String data2 = (String) fInfo.get("memberName");
			String data3 = (String) fInfo.get("memberStation");
			String data4 = (String) station.get("wgsX");
			String data5 = (String) station.get("wgsY");
			
			JSONObject total = new JSONObject();
			
			total.put("memberId", data1);
			total.put("memberName", data2);
			total.put("memberStation", data3);
			total.put("wgsX", data4);
			total.put("wgsY", data5);

			result.put(i, total);
			
			System.out.println("단계 데이터" + result.get(i));
			//}
		}
		
		
		
		/*for(int i = 0; i < friendsId.length; i++) {
			JSONObject fData = (JSONObject) friendsInfo.get(i);
			JSONObject sData = (JSONObject) stationList.get(i);
			result
		}*/
		
		System.out.println("최종 데이터" + result);

		PrintWriter out = response.getWriter();
		out.println(result);
		
		return null;
	}

}
