package com.sai.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface Action {   
	
	String execute(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
