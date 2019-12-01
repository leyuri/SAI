package com.sai.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.json.simple.JSONObject;

import com.sai.vo.MemberVO;

public class MemberDAO {
	private Connection conn;

	public MemberDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("DB연결 OK");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/hanium_prac?useSSL=false", "root", "qkqh3635^^");				

		}catch(Exception e) {
			e.printStackTrace();
		}	

	}

	public MemberVO login(String memberId, String memberPw) {
		
		System.out.println("MemberDAO의 login()에 진입");
		System.out.println("login 서블릿에서 넘어온 ID: " + memberId);
		System.out.println("login 서블릿에서 넘어온 PW: " + memberPw);
		
		MemberVO vo = new MemberVO();
		String sql = "select memberId, memberPw, memberName, memberPhone, memberStation from member where memberId = ? and memberPw = ?";

		PreparedStatement pstmt = null;
		ResultSet rs= null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPw);

			rs = pstmt.executeQuery();
			
			System.out.println("rs 결과 개수: " + rs.getRow());
			
			System.out.println("rs 실행");

			while(rs.next()){
				System.out.println("rs에 값이 있음");
				/*if(memberPw.equals(rs.getString(2))) {
					//vo.getMEMBER_PHOTO(rs.getInt(1));
				 */					
				vo.setMEMBER_ID(rs.getString(1));
				System.out.println("login()에서 실행하고 난 후 ID: " + rs.getString(1));
				vo.setMEMBER_PW(rs.getString(2));
				vo.setMEMBER_NAME(rs.getString(3));
				vo.setMEMBER_PHONE(rs.getString(4));
				vo.setMEMBER_STATION(rs.getString(5));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return vo;
	}

	public void join(String memberPhoto, String memberId, String memberPw, String memberName, String memberPhone, String memberStation) {
		String sql = "insert into member values(?,?,?,?,?,?)";

		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sql); 	 
			pstmt.setString(1, memberPhoto);
			pstmt.setString(2, memberId);
			pstmt.setString(3, memberPw);
			pstmt.setString(4, memberName);
			pstmt.setString(5, memberPhone);
			pstmt.setString(6, memberStation);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	public JSONObject showMyInfo(String memberId) {
		String sql = "select memberPhoto, memberId, memberPw, memberName, memberStation, memberPhone from member where memberId = ?";

		JSONObject result = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs= null;

		try {
			pstmt = conn.prepareStatement(sql); 	 
			pstmt.setString(1, memberId);

			rs = pstmt.executeQuery();

			if(rs.next()) {
				result.put("memberPhoto", rs.getString(1));
				result.put("memberId", rs.getString(2));
				result.put("memberPw", rs.getString(3));
				result.put("memberName", rs.getString(4));
				result.put("memberStation", rs.getString(5));
				result.put("memberPhone", rs.getString(6));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		return result;
	}

	public void modifyInfo(String memberId, String memberPw, String memberName, String memberPhone, String memberStation) {
		String sql = "update member set memberPw = ?, memberName = ?, memberPhone = ?, memberStation = ? where memberId = ?";
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberPw);
			pstmt.setString(2, memberName);
			pstmt.setString(3, memberPhone);
			pstmt.setString(4, memberStation);
			pstmt.setString(5, memberId);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addLikePlace(String memberId, String contentId, String contentTypeId, String areaCode, String firstImage, String title, String readCount, String addr1, String addr2, String tel, Double mapX, Double mapY) {
		String sql = "insert likePlace values(?, ? ,? ,? ,? ,?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, contentId);
			pstmt.setString(3, contentTypeId);
			pstmt.setString(4, areaCode);
			pstmt.setString(5, firstImage);
			pstmt.setString(6, title);
			pstmt.setString(7, readCount);
			pstmt.setString(8, addr1);
			pstmt.setString(9, addr2);
			pstmt.setString(10, tel);
			pstmt.setDouble(11, mapX);
			pstmt.setDouble(12, mapY);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("좋아요 디비에 추가 완료");

	}
	
	public String[] loadFriendsMap(String sessId) {
		
		String sql = "select f2 from friends where f1 = ?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String[] friendsId = null;

		try {
			pstmt = conn.prepareStatement(sql); 	 
			pstmt.setString(1, sessId);

			rs = pstmt.executeQuery();
			
			rs.last();
			int nFriends = rs.getRow();
			rs.beforeFirst();
			
			friendsId = new String[nFriends + 1];
			friendsId[0] = sessId;
			
			int i = 1;
			
			System.out.println(friendsId.length);
			while(rs.next()) {
				friendsId[i] = rs.getString(1);
				i++;
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} 

		return friendsId;
	}

	public JSONObject searchFriendInfo(String memberId) {
		String sql = "select memberId, memberName, memberStation from member where memberId = ?";
		
		JSONObject json = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				json.put("memberId", rs.getString(1));
				json.put("memberName", rs.getString(2));
				json.put("memberStation", rs.getString(3));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;
		
	}

	public JSONObject searchLocation(String fStation) {
		String sql = "select wgsX, wgsY from subway where stationName = ?";
		
		JSONObject json = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fStation);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				json.put("wgsX", rs.getString(1));
				json.put("wgsY", rs.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return json;		
	}

	public boolean checkId(String checkId) {
		String sql = "select count(memberId) from member where memberId = ?";
		
		System.out.println("checkId 진입 성공");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int result = 1;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, checkId);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("if문 진입");
				result = rs.getInt(1);
				if(result == 0) {
					System.out.println("result 값이 0임");
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

}

