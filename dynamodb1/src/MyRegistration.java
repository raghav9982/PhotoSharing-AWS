import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;


public class MyRegistration {

	public boolean registerUser(Table tableName, String UserName, String Password) {
		
		return processRegistration(tableName, UserName, Password);
	}
	
	public boolean processRegistration(Table tableName, String aUserName, String aPassword) {

		Item item = tableName.getItem("UserName",aUserName);
		if(item==null) {
			// Insert
			
			item = new Item().withPrimaryKey("UserName", aUserName).withString("Password",
					aPassword);
			PutItemOutcome outcome = tableName.putItem(item);
			return true;
		}
		else return false;
	}
	
	
}
