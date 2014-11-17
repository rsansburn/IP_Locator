package com.servletexample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.ws.fault.ServerSOAPFaultException;

import net.webservicex.GeoIP;
import net.webservicex.GeoIPService;
import net.webservicex.GeoIPServiceSoap;

public class ServletExample extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		/*
		 * make a call to webservice
		 * http://www.webservicex.net/geoipservice.asmx?WSDL
		 */

		//this ensures that null ip address are not passed in for processing
		req.setAttribute("ipAddress", req.getParameter("ipAddress"));

		// holds the IP passed in by user
		String userIP = req.getParameter("ipAddress");
		
		//class used to take IP address for processing
		GeoIP geoIP = null;
		
		try {
			if (userIP.length() == 1 ) {
				System.out.println("You need to pass in one IP address");

			} else {

				// to access remote service call
				// we need a service endpoint interface
				// stub.getCountryName (ipAddress)

				// use method of this class to get instance of port
				GeoIPService ipService = new GeoIPService();

				// this is the stub/ port-> local object representing a remote
				// service
				GeoIPServiceSoap geoIPServiceSoap = ipService.getGeoIPServiceSoap();

				// this returns return type
				/*sometimes a vaild IP will not be processed because of webservice problem*/
				geoIP = geoIPServiceSoap.getGeoIP(userIP);

				// displays country name in console
				System.out.println(geoIP.getCountryName());
				System.out.println(geoIP.getCountryCode());
				
				
				//this variable will store the location based on given ip 
				String ipLocation = geoIP.getCountryName();

				//this will handle Taiwan and omit ROC string
				if (ipLocation.contains("Taiwan; Republic of China (ROC)"))
				{
					ipLocation ="Taiwan";
				}
				
				/*
				 * when the user submits the IP address the follwing page is returned
				 */
				resp.getWriter().println("<html>");
				resp.getWriter().println("<head>");
				resp.getWriter().println("<title> This is the response</title>");
				
				/*this will write IP address to the web page*/
				resp.getWriter().println("IP Address Entered: " + userIP); 
				
				/*display location of IP address to the user on new page*/
				resp.getWriter().println("<br><p class =\"posReturnLocation\">Your IP location is:<br><b>"+ ipLocation + "</b> </p>");
				
				//creates button used to return to homepage
				String backButton = "<br><input id =\"back\" Type=\"submit\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"/>";
				resp.getWriter().println(backButton);
				
				//if IP is found but location is reserved display the reserve pic
				if (ipLocation==null || ipLocation.contains("Reserved"))
				{
					ipLocation = "question2";
				}
				
				
				if (ipLocation.contains("Russian Federation"))
				{
					ipLocation ="Russia";
					
				}
				
				//if IP location is found display the flag of the country
				String flagImage ="<img id =\"flagImage\" src=\"Countries/"+ipLocation+".png\" alt=\"Flag Image\" style=\"width:304px;height:200px; position:relative;left:850px;\">";
				resp.getWriter().println(flagImage);
				resp.getWriter().println("<link rel =\"stylesheet\" type =\"text/css\" href=\"styles.css\">");
				resp.getWriter().println("</head>");
				resp.getWriter().println("</html>");
			}
		} catch (NullPointerException e) { /*if ip country return is null or ip not found show custom error page*/
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("<title> Problem Processing IP</title>");

			resp.getWriter().println("Your IP is: " + userIP);
			
			String ipErrorString ="<p id=\"ipErrorFormat\">The IP address:<br> <b>"+userIP+"</b> <br> could not be processed </p>";
			resp.getWriter().println(ipErrorString);
			
			resp.getWriter().println("<br><p class =\"tryAgain\">Try another IP address</p><br>");
			
			String backButton = "<br><input id =\"backButtonNoProcess\" Type=\"submit\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"/>";
			resp.getWriter().println(backButton);
			
			resp.getWriter().println("<link rel =\"stylesheet\" type =\"text/css\" href=\"styles.css\">");
			resp.getWriter().println("</head>");
			resp.getWriter().println("</html>");
			
			
//			System.out.println(e.getMessage());
//			System.out.println(userIP.length());
		}
		catch(ServerSOAPFaultException se) /*the webservice is not able to process this IP*/
		{
			resp.getWriter().println("<html>");
			resp.getWriter().println("<head>");
			resp.getWriter().println("<title> Problem Processing IP</title>");

			resp.getWriter().println("Your IP is: " + userIP);
			
			String ipErrorString ="<p id=\"ipErrorFormat\">The IP address:<br> <b>"+userIP+"</b> <br> could not be processed </p>";
			resp.getWriter().println(ipErrorString);
			
			resp.getWriter().println("<br><p class =\"tryAgain\">Try another IP address</p><br>");
			
			String backButton = "<br><input id =\"backButtonNoProcess\" Type=\"submit\" VALUE=\"Back\" onClick=\"history.go(-1);return true;\"/>";
			resp.getWriter().println(backButton);
			
			resp.getWriter().println("<link rel =\"stylesheet\" type =\"text/css\" href=\"styles.css\">");
			resp.getWriter().println("</head>");
			resp.getWriter().println("</html>");
			
//			System.out.println(se.getMessage());
//			System.out.println(userIP.length());
		}

	
	}
}
