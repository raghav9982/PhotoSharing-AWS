
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

/**
 * This class analyses the TAG and calls the respective method of DataHandler for further processing.
 * @author Team Mavericks
 *
 */
public class TagHandler {
	
	
	JSONObject json=new JSONObject();
	
	
	/**
	 * Processes the TAG received from client and then calls the appropriate method of the Model DataHandler
	 * @param TAG
	 * @param HTTPrequest
	 * @return JSONObject
	 */
	public JSONObject processTAG(String TAG, HttpServletRequest request)
	{	
		driver dv= new driver();
		System.out.println("hhhhhhhhhhhhh");
		
		if(TAG.equals("LOGIN"))
		{
			String UserName=request.getParameter("USERNAME");
			String Password=request.getParameter("PASSWORD");
			
			json=dv.LoginUser(UserName, Password);
		}
		else if(TAG.equals("REGISTRATION"))
		{
			String UserName=request.getParameter("USERNAME");
			String Password=request.getParameter("PASSWORD");
			json=dv.RegisterUser(UserName, Password);
		}
		else if(TAG.equals("DELETE"))
		{
			String UserName=request.getParameter("USERNAME");
			long keyname=Long.parseLong(request.getParameter("KEYNAME"));
			//String ImageName=request.getParameter("IMAGENAME");
			System.out.println("uuuuuu"+UserName+" : "+keyname);
			json=dv.DeleteImage(keyname, UserName);
		}
		else if(TAG.equals("RETRIEVE"))
		{
			json=dv.retrieveImages();
		}
		
		return json;
	}
		
}
