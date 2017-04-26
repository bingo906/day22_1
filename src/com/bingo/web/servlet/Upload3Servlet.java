package com.bingo.web.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;

public class Upload3Servlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			/*
			 * 上传三步
			 */
			//工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//解析器
			ServletFileUpload sfu = new ServletFileUpload(factory);
			//限制单个文字大小为100K(需要在解析开始之前使用)
			sfu.setFileSizeMax(100*1024);
			//限制整个表单大小为1m
			sfu.setSizeMax(1024*1024);
			//解析，得到List
			try{
				List<FileItem> list = sfu.parseRequest(request);
				FileItem fi = list.get(1);
				/////////////////////////
				
				/*
				 * 1.得到文件保存的路径
				 * 
				 */
				String root = this.getServletContext().getRealPath("/WEB-INF/files");
				/*
				 *2.生成两层目录
				 * 	得到文件名称
				 * 	得到hashCode
				 * 	转发成16进制
				 * 	获取前二个字符生成目录
				 *  
				 */
				String filename = fi.getName();
				int index = filename.lastIndexOf("\\");
				if(index != -1){
					filename = filename.substring(index+1);
				}
				
				String savename = CommonUtils.uuid()+"_"+ filename;
				
				//得到HashCode()
				int hCode = filename.hashCode();
				String hex =  Integer.toHexString(hCode);
				
				//获取hex的前两个字母，与root连接在一起，生成一个完整的路径
				File dirFile = new File(root,hex.charAt(0)+"/"+hex.charAt(1));
				
				//创建目录链
				dirFile.mkdirs();
				
				//创建目标文件
				File destFile = new File(dirFile,savename);
				
				//保存
				fi.write(destFile);
				/////////////////////////
			}catch(FileUploadException e){
				if(e instanceof FileUploadBase.FileSizeLimitExceededException){
					request.setAttribute("msg", "你上传的文件超出100KB");
					System.out.println("捕获异常！！！");
					request.getRequestDispatcher("/form3.jsp").forward(request, response);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}

}
