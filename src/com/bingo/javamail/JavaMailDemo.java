package com.bingo.javamail;


import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.junit.Test;

public class JavaMailDemo {
	@Test
	public void fun1() throws AddressException, MessagingException{
		//获得Session
		
		Properties prop = new Properties();
		prop.setProperty("mail.host", "smtp.126.com");
		prop.setProperty("mail.smtp.auth", "true");
		
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("bingo906@126.com", "*****");
			}
			
		};
		
		Session session = Session.getInstance(prop,auth);
		
		//创建MimeMessage
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("bingo906@126.com"));//设置发件人
		msg.setRecipients(RecipientType.TO, "906014227@qq.com");//设置收件人
//		msg.setRecipients(RecipientType.CC, "906014227@qq.com");//抄送
//		msg.setRecipients(RecipientType.BCC, "906014227@qq.com");//设置暗送  
		
		msg.setSubject("这是来自Bingo的测试邮件-");
		msg.setContent("一封来自JavaMail的邮件","text/html;charset=utf-8");
		
		//发送邮件
		Transport.send(msg);
	}
	@Test
	public void fun2() throws AddressException, MessagingException, IOException{
		//获得Session
		
		Properties prop = new Properties();
		prop.setProperty("mail.host", "smtp.126.com");
		prop.setProperty("mail.smtp.auth", "true");
		
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("bingo906@126.com", "xxxx");
			}
			
		};
		
		Session session = Session.getInstance(prop,auth);
		
		//创建MimeMessage
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("bingo906@126.com"));//设置发件人
		msg.setRecipients(RecipientType.TO, "bingo906@126.com");//设置收件人
//		msg.setRecipients(RecipientType.CC, "906014227@qq.com");//抄送
//		msg.setRecipients(RecipientType.BCC, "906014227@qq.com");//设置暗送  
		
		msg.setSubject("这是来自Bingo的测试邮件--包含附件");
		/*
		 * 当发送包含福建的邮件时，邮件体就为多部件形式！
		 * 1.创建一个多部件的部件内容！MimeMultipart
		 * 		MimeMultipart就是一个集合，用来装载多个主提部件！
		 * 2.我们需要创建两个主体部件，一个是文本内容，另一个是福建的。
		 * 	主体部件叫MimeBodyPart
		 * 3.把MimeMultipart设置给MimeMessage的内容！
		 * 	
		 */
		MimeMultipart list = new MimeMultipart();//创建多部分主体
		
		//创建MimeBodyPart
		MimeBodyPart part = new MimeBodyPart();
		//设置主体内容
		part.setContent("这是一封包含附件的邮件","text/html;charset=utf-8");
		//把主体部件添加到集合中
		list.addBodyPart(part);
		
		//创建MimeBodyPart
		MimeBodyPart part2 = new MimeBodyPart();
		part2.attachFile(new File("F:/666.jpg"));
		//设置显示文件的名称，其中encodeText用来表示处理中文乱码问题
		part2.setFileName(MimeUtility.encodeText("测试图.jpg"));
		list.addBodyPart(part2);
		
		msg.setContent(list);//把它设置给邮件作为邮件的内容
		
		//发送邮件
		Transport.send(msg);
	}
}
