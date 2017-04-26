package com.bingo.web.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import sun.misc.BASE64Encoder;

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
		//解决下载名中文编码问题
		String framenname = filenameEncoding("流程.jpg",request);
		FileInputStream input = new FileInputStream(filename);
		String contentType = this.getServletContext().getMimeType(filename);
		String contentDisposition = "attachment;filename="+framenname;
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

	// 用来对下载的文件名称进行编码的！
	public static String filenameEncoding(String filename, HttpServletRequest request) throws IOException {
		String agent = request.getHeader("User-Agent"); //获取浏览器
		if (agent.contains("Firefox")) {
			BASE64Encoder base64Encoder = new BASE64Encoder();
			filename = "=?utf-8?B?"
					+ base64Encoder.encode(filename.getBytes("utf-8"))
					+ "?=";
		} else if(agent.contains("MSIE")) {
			filename = URLEncoder.encode(filename, "utf-8");
		} else {
			filename = URLEncoder.encode(filename, "utf-8");
		}
		return filename;
	}
}
