# day22_1
文件上传
## 上传下载JavaMail

#### 1.上传对表单和Servlet的限制，以及对上传数据的抓包

	- method="post"
	- enctype="multipart/form-data"
	- 表单中需要添加文件表单项 
	`	<input type="file" name="xxx">`
```
<form action="xxx" method="post" enctype="multipart/form-data">
	用户名：<input type="text" name="username" /><br/>
	照片：<input type="file" name="zhaopian"/>
	<input type="submit" value="上传" />
</form>
```
		 
#### 2.上传对Servlet限制
	- request.getParameter("xxx");
	这个方法在表单为enctype="mutipart/form-data"时,它作废了。永远返回null
  - ServletInputStream request.getInputStream();包含整个请求的体。使用这个方法获取表单数据

多部件表单的体：
1.分隔出多个部件，即一个表单一个部件
2.一个部件中自己包含请求和空行，以及请求体。
3.普通表单项：
	> 1个头： Content-Disposition:包含name="xxxx" ,即表单名称‘
	> 体就是表单项的值
4.文件表单项
	> 2个头： 
	- Content-Disposition:包含name="xxxx" ,即表单名称‘;还有一个filename="xxx" 表示上传文件的名称
	- Content-Type:它是上传文件的MIME类型，例如：image/pjpeg;表示上传的是图片，图上中jpg扩展名的图片

#### 3.Servlet获取到上传表单的数据
```
	Upload1Servlet.java
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//表单数据获取类
		ServletInputStream in = request.getInputStream();
		String s = IOUtils.toString(in);
		System.out.println(s);
	}
```
	
#### 4.comons-fileupload组件
**所需jar包：**
* commons-fileupload.jar
* commons-io.jar

>这个组件，作用是解析request中的上传数据，解析后的结果是一个表单项封装到一个FileItem对象中，我们只需要调用FileItem的方法即可。

1.上传三步
相关类
* 工厂：DiskFileItemFactory
* 解析器：ServletFileUpload
* 表单项：FileItem 
1.创建工厂：
`DiskFileItemFactory factory = new DiskFileItemFactory()`
2.创建解析器
`ServletFileUplaod sfu = new ServletFileUpload(factory)`
3.使用解析器来解析request,得到FileItem集合
`List<FileItem> fileItemList = sfu.parseRequest(request)`

2.FileItem
	*  boolean isFormField():是否为普通表单项！返回为true为普通表单项，如果未false即为文件表单项！
	* String getFieldName() 返回当前表单项的名称
	* String getString(String cahrset):返回表单项的值
	* String getName()：返回上传文件的名称
	* long getSize()：返回上传文件的字节数
	* InputStream getInputStream():返回上传文件对应的输入流
	* InputStream getInputStream():返回上传文件对应的输入流
	* void write(File destFile):把上传的文件内容保存到指定的文件中
	* String getContentType() 获取类型

#### 5.上传文件代码演示
```
Upload2Servlet.java
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

```
#### 上传的细节：
**1.文件必须保存到WEB-INF下！**
	>* 目的是不让浏览器直接访问到
	* 把文件保存到WEB—INF目录下

**2.文件名称相关问题**
   >* 有的浏览器上传的文件名是绝对路径，这需要切割！切割出文件名和类型即可。
```
	String filename = fi2.getName();
	int index = filename.lastIndexOf("\\");
	if(index!=-1){
		filename=filename.subString(index+1);
	}
```
   * 文件名乱码 或者普通表单项乱码！ 
   > `request.setCharacterEncoding("utf-8");`
   因为fileupload内部会调用`request.getCharacterEncoding("utf-8")`
  **方法二**：servletFileUpload.setHeaderEncoding("utf-8")优先级高
   * 文件同名问题：
   >我们要为每个文件添加名称前缀，这个前缀保证不能重复，uuid
   `filename = CommonUtils.uuid()+"_"+filename;`
 
 ** 3.目录打散**

  > * 不能再同一个目录中存放多个文件 
  > * 首字母打散，使用文件的首字母作为目录名称，例如:abc.txt;那么我们把文件保存到a目录下。如果a目录不存在，那么 创建。
  > * 时间打散：使用当前的日期作为目录。
  > * 哈希打散：
	   * 通过文件名称得到int值，即调用hashCode（）
	   * 他的int值转换成16进制，0-9 A-F
	   *    获取16进制的前两位生成目录，目录为两层！例如1B2C3D4E5F; /1/B/保存文件
```
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
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
```

**4.上传文件的大小限制**
* 单个文件大小限制
* 整个请求大小限制	
	
```
//限制单个文字大小为100K(需要在解析开始之前使用			sfu.setFileSizeMax(100*1024);
//限制整个表单大小为1m	sfu.setSizeMax(1024*1024);

```
> 如果大小超出设定的大小，抛出异常
	单个文件
	`FileUploadBase.FileSizeLimitExceededException `
	FileUploadBase.SizeLimitExceedException
```
catch(FileUploadException e){
				if(e instanceof FileUploadBase.FileSizeLimitExceededException){
					request.setAttribute("msg", "你上传的文件超出100KB");
					System.out.println("捕获异常！！！");
					request.getRequestDispatcher("/form3.jsp").forward(request, response);
				}
			} 
```
5.缓存大小与临时目录
	* 超出多大，才向硬盘保存！默认为10KB
	* 临时目录：向硬盘的什么目录保存
	
`public DiskFileItemFactory(int sizeTheshold,File repository)`
	设置缓存大小与临时目录：
	`new DiskFileItemFactory(20*1024,new File("F:/"))`
	注释上传限制
### 下载
1.下载就是想客户端响应字节数据！
	原来我们响应的都是html的字符数据！
	把一个文件变成字节数组，使用`response.getOutputStream()`来响应給浏览器
2.下载的要求
* 两个头一个流
> `Content-Type`：传递给客户端的文件是什么MINE类型，例如：image/pjpeg
> `Content-Disposition`:	他的默认值为inline，表示在浏览器窗口中打开！
`attachment;filename-xxx`
> 流：要下载的文件数据

####下载的细节
1.显示在下载框的中文名称会出现乱码
>  *  FireFox：Base64编码 
>  * 其他部分浏览器采用URL编码
> 通用方案：
> `filename = new String(filename.getBytes("GBK"),"IOS-8859-1");`
```
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
```
### JavaMail
是Java提供的一组API，用来发送和接收邮件

**1.收发邮件**
```sequence
客户端1->>邮件服务器: 发邮件
邮件服务器->>客户端2: 收邮件
```
**2.邮件协议概述**
	>与HTTP协议相同，收发邮件也是需要有传输协议的。
	* SMTP：(simple Mail Transfer Protocol,简单邮件传输协议)发邮件协议
	* POP3：(Post Office Protocol Version 3,邮局协议第3版)
	* IMAP：(Internet Message Access Protocol,因特网消息访问协议)收发邮件协议，暂不涉及


**3.邮件收发过程**
![邮件系统](https://github.com/bingo906/day22_1/blob/master/imagefolder/JavaMail1.png?raw=true)
**跨服务器：**
![邮件收发2](https://github.com/bingo906/day22_1/blob/master/imagefolder/JavaMail2.png?raw=true)
