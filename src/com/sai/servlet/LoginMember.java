package com.sai.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sai.dao.MemberDAO;
import com.sai.vo.MemberVO;

public class LoginMember implements Action {

	public String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
    	String memberId = request.getParameter("memberId");
		String memberPw = request.getParameter("memberPw");
		
		System.out.println("로그인이 요청된 아이디: " + memberId);
		System.out.println("로그인이 요청된 패스워드: " + memberPw);
		
		MemberVO vo = new MemberDAO().login(memberId, memberPw);
		
		if(vo.getMEMBER_ID() != null) {			
			session = request.getSession();
			session.setAttribute("sessId", vo.getMEMBER_ID());
			System.out.println("vo에 저장된 아이디: " + vo.getMEMBER_ID());
			System.out.println(session.getAttribute("sessId"));
			
			session.setAttribute("sessPw", vo.getMEMBER_PW());
			session.setAttribute("sessName", vo.getMEMBER_NAME());
			session.setAttribute("sessStation", vo.getMEMBER_STATION());
			session.setAttribute("sessPhone", vo.getMEMBER_PHONE());
			
			System.out.println("로그인 페이지에서 세션 설정 값: " + session.getAttribute("sessId"));
			
			return "mainPage.html";
		}
		
		response.setContentType("text/html;charset=UTF-8");
		return "index.html";	
	}
	
}
