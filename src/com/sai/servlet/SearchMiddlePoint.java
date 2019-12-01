package com.sai.servlet;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sai.dao.MemberDAO;

public class SearchMiddlePoint implements Action {

	/* 프론트에서 넘어오는 인원 수 파악해서 동적으로 배열 생성해야하며, 넘어오는 데이터로 디비에 접근해서 좌표 가져와야 함.
	 * 좌표를 가져와서 xList, yList에 동적으로 넣어야 함.
	 */
	public static String[] friends; //넘어온 인원 수 파악해서 동적으로 크기 지정, friends에는 알고리즘에 참여한 사람들의 역이 들어가야함.
	double[] xList;
	double[] yList;

	@Override
	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Point2D.Double mCenterPoint = null;
		ArrayList<Point2D> mVertexs = new ArrayList<Point2D>();

		System.out.println("중간 지점 찾기 페이지 연결");

		String sData = request.getParameter("data");
		System.out.println("sData의 내용 : " + sData);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject = (JSONObject) jsonParser.parse(sData);

			System.out.println("jsonObject의 내용은 : " + jsonObject);

			//			friends = new String[memberList.size()];

			JSONArray stationInfoList = (JSONArray) jsonObject.get("stationList");

			System.out.println("stationInfoList의 사이즈는 : " + stationInfoList.size());

			xList = new double[stationInfoList.size()];
			yList = new double[stationInfoList.size()];

			for(int i = 0; i < stationInfoList.size(); i++) {
				JSONObject list = (JSONObject) stationInfoList.get(i);
				String stationName = (String) list.get("stationName");
				Double mapX = Double.parseDouble(list.get("mapX").toString());
				Double mapY = Double.parseDouble(list.get("mapY").toString());

				mVertexs.add(new Point2D.Double(mapX, mapY));

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mCenterPoint != null) 
			System.out.println("mCenterPoint != null");

		double centerX = 0.0, centerY = 0.0;
		double area = 0.0;

		mCenterPoint = new Point2D.Double(0.0, 0.0);
		int firstIndex, secondIndex, sizeOfVertexs = mVertexs.size();

		Point2D.Double  firstPoint;
		Point2D.Double  secondPoint;

		double factor = 0.0;

		for (firstIndex = 0; firstIndex < sizeOfVertexs; firstIndex++) {
			secondIndex = (firstIndex + 1) % sizeOfVertexs;

			firstPoint  = (java.awt.geom.Point2D.Double) mVertexs.get(firstIndex);
			secondPoint = (java.awt.geom.Point2D.Double) mVertexs.get(secondIndex);

			factor = ((firstPoint.getX() * secondPoint.getY()) - (secondPoint.getX() * firstPoint.getY()));

			area += factor;

			centerX += (firstPoint.getX() + secondPoint.getX()) * factor;
			centerY += (firstPoint.getY() + secondPoint.getY()) * factor;
		}

		area /= 2.0;
		area *= 6.0f;

		factor = 1 / area;

		centerX *= factor;
		centerY *= factor;

		System.out.println("centerX : " + centerX);
		System.out.println("centerY : " + centerY);

		mCenterPoint.setLocation(centerX, centerY);
		System.out.println("출력 : " + mCenterPoint.toString());


		System.out.println("중간 지점의 midX  : " +  mCenterPoint.getX());
		System.out.println("중간 지점의 midY  : " +  mCenterPoint.getY());
		System.out.print("면적 : " + area);

		JSONObject result = new JSONObject();

		result	= SearchTenSub.search(mCenterPoint.getX(), mCenterPoint.getY());

		PrintWriter out = response.getWriter();
		out.println(result);

		return null; //반환 페이지

	}
}


