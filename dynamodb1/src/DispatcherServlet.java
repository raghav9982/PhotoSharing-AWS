
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;
 
/**
 * This class receives request from the client , forwards it for processing and sends the reponse back.
 * @author Team Mavericks
 *
 */
public class DispatcherServlet extends HttpServlet{
	
	   private static final long serialVersionUID = 1L;
	   String TAG="nothing";   
	   
	   TagHandler handler=new TagHandler();	   
	   PrintWriter out;

  /**
   * Receives the client request. It forwards the request to the doPost for further processing.
   */
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      doPost(request, response);      
  }
  
  /**
   * Receives the request from the Client. It forwards the request parameters to the Tag Handler. Once it gets the response from the Tag Handler
   * it sends the response back to the Client in the form of a JSONObject. 
   * 
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
  {
		 
	  TAG=request.getParameter("TAG");
	  
	  JSONObject json=handler.processTAG(TAG, request);
	  
	  out=response.getWriter();
	  out.print(json);
	  System.out.println(json);
	  
	  
  }
  
} 