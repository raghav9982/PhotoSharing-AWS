import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class LoginAndSeeDetails {

	public HashMap<String,String> userLogin(Table tableName, String aUserName, String aPassword) {

		
		if (processLogin(tableName, aUserName, aPassword)) {
				return Util.retriveImages();
		} else {
			HashMap<String,String> result = new HashMap<>();
			result.put("failure", "unable to login");
			return result;
		}
	}

	public boolean processLogin(Table tableName, String aUserName,
			String aPassword) {

		Item item = tableName.getItem("UserName", aUserName);

		if (item != null) {
			String password = item.getString("Password");
			if (password.equals(aPassword)) {

				return true;
			}
		}
		return false;
	}

	
}
