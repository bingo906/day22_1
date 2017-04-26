package com.bingo.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Upload2Servlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 上传三步
		 * 1.得到工厂
		 * 2.通过工厂创建解析器
		 * 3.解析request,得到FileItem集合
		 * 4.遍历FileItem集合，调用API完成文件的保存
		 */
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		try {
			List<FileItem> fileItemList = sfu.parseRequest(request);
			FileItem fi1 = fileItemList.get(0);
			FileItem fi2 = fileItemList.get(1);
			System.out.println("普通表单项"+fi1.getFieldName()+"="+fi1.getString("UTF-8"));
			System.out.println("文件表单项演示：");
			System.out.println(fi2.getContentType());
			System.out.println(fi2.getName());
			System.out.println(fi2.getSize());
			
			//保存文件
			File destFile = new File("G:/test.jpg");
			fi2.write(destFile);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
