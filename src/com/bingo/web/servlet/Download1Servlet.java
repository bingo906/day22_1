package com.bingo.web.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class Download1Servlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			/*
			  * 两个头一个流
			 * 1.Content-Type
			 * 2.Content-Disposition
			 * 3.流：下载文件的数据
			 */
		String filename = "F:/流程.jpg";
		FileInputStream input = new FileInputStream(filename);
		String contentType = this.getServletContext().getMimeType(filename);
		String contentDisposition = "attachment;filename=aa.jpg";
		response.setHeader("Content-Type", contentType);
		response.setHeader("Content-Disposition", contentDisposition);
		
		ServletOutputStream output = response.getOutputStream();
		IOUtils.copy(input, output);
		input.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
