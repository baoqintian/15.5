package com.wgh;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Vector;

/**
 * Servlet实现类ChatServlet
 */
@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ChatServlet() {
        super();
    }

	/**
	 * 处理GET请求的方法
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * 处理POST请求的方法
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		String action = request.getParameter("action");
        if ("send".equals(action)) {	//发送留言
            this.send(request, response);
        }else if("get".equals(action)){
        	this.get(request,response);
        }
		
	}
	public void send(HttpServletRequest request,HttpServletResponse response)  throws ServletException, IOException {
		ServletContext application=getServletContext();//获取application
        response.setContentType("text/html;charset=UTF-8");
        String user=request.getParameter("user");
        String speak=request.getParameter("speak");
        Vector<String> vector=null;
        String message="["+user+"]说："+speak;
        if(null==application.getAttribute("message")){
            vector=new Vector<String>();
        }else {
            vector=(Vector<String>)application.getAttribute("message");
        }
        vector.add(message);
        application.setAttribute("message",vector);
        Random random=new Random();
        request.getRequestDispatcher("ChatServlet?action=get&nocache="+random.nextInt(10000)).forward(request,response);
	}
	public void get(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
	  //设置响应内容类型和编码方式
        response.setContentType("text/html;charset=UTF-8");
      //禁止页面缓存
        response.setHeader("Cache-Control","no-cache");
        //获取输出流
        PrintWriter out=response.getWriter();
        //获取application 对象
        ServletContext application=getServletContext();
        String msg="";
        if(null!=application.getAttribute("message")){
            Vector<String> v_temp=(Vector<String>) application.getAttribute("message");
            for(int i=v_temp.size()-1;i>=0;i--){
                msg=msg+"<br>"+v_temp.get(i);
            }
        }else {
            msg="欢迎来到聊天室";
        }
        //输出聊天信息
        out.println(msg);
        out.close();
	}
}
