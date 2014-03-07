package oso;
import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/main.jsp")
public class Test extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request,
            HttpServletResponse response)
    throws ServletException, IOException {
	String query = request.getParameter("query");
	response.setContentType("text/html");
	 PrintWriter out = response.getWriter();
	    out.println
	      ("<!DOCTYPE html>\n" +
	       "<html>\n" +
	       "<head><title>OSO Alpha</title></head>\n" +
	       "<body bgcolor=\"#fdf5e6\">\n" +
	       "<h1>" + query + "</h1>\n" +
	       "<p>Simple servlet for testing.</p>\n" +
	       "</body></html>");
	}
	
}
