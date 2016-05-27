
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

public class driver {

	static String userTableName = "UserTable";
	static String imageTableName = "ImageTable";
	static String UserName = "raj";

	static DynamoDB dynamoDB;
	static AmazonDynamoDBClient client;
	static Table userTable, imageTable;
	JSONObject json;

	public static void main(String[] args) {

		new driver().process();
	}

	driver() {
		client = new AmazonDynamoDBClient(new AWSCredentials() {
			
			@Override
			public String getAWSSecretKey() {
				return "BBUMwI35mGxEUyRwIwZtNeI3QcjiPtZye+u8U/vn";
			}
			
			@Override
			public String getAWSAccessKeyId() {
				return "AKIAJZJMI3F5MTWEHHNA";
			}
		});
		client.setRegion(Region.getRegion(Regions.US_WEST_1));
		dynamoDB = new DynamoDB(client);
		userTable = dynamoDB.getTable(userTableName);
		imageTable = dynamoDB.getTable(imageTableName);
	}

	public void process() {

		//System.out.println(InsertImage("Tuan.jpg"));
		//System.out.println(DeleteImage(Long.parseLong("1427185448200"), "raj"));
		//InsertImage(akeyName)
		//retrieveImages();
		System.out.println(RegisterUser("Instructor", "Instructor"));
		/*
		 * UserRegistration usr1 = new UserRegistration();
		 * usr1.registerUser(userTable, "raj", "raj");
		 * usr1.registerUser(userTable, "mahesh", "mahesh");
		 */

		/*
		 * LoginAndSeeDetails login = new LoginAndSeeDetails();
		 * 
		 * HashMap<String,String> result = login.userLogin(userTable, "raj1",
		 * "raj"); if(result.size()<1)
		 * System.out.println(result+"No images to display"); else
		 * if(result.containsKey("failure"))
		 * System.out.println("Unable to login"); else
		 * System.out.println(result);
		 */
	}
	
	public JSONObject retrieveImages() {
		json = new JSONObject();
		JSONObject jsonObject = null;
		json.put("SUCCESS", 1);
		
	
		
		HashMap<String, String> result = Util.retriveImages();
		ArrayList sorted = new ArrayList(result.keySet());
		Collections.sort(sorted);
		
		if(result==null) {
			json.put("SUCCESS", 0);
		}
		if (result.size() < 1){
			json.put("MSG","NOIMAGES");
		}
		else {
			JSONArray IMAGES=new JSONArray();
			Iterator it=sorted.iterator();
			json.put("MSG","IMAGES");
			
			while(it.hasNext()) {
				jsonObject=new JSONObject();
				
				String keyName = (String) it.next();
				
				String ImageURL = result.get(keyName);
				String UserName = ImageURL.substring(0, ImageURL.indexOf("http"));
				ImageURL = ImageURL.substring(ImageURL.indexOf("http"));
				jsonObject.put("KEYNAME", keyName);
				jsonObject.put("URL", ImageURL);
				jsonObject.put("DESCRIPTION", UserName);
				
				IMAGES.add(jsonObject);
			}
			json.put("IMAGEURL", IMAGES);
		}
		System.out.println(json);
		return json;
		
	}
	
	public String getUserName(String input) {
		if(input.contains("nge"))
			return "Angel";
		if(input.contains("struc"))
			return "Instructor";
		if(input.contains("uan"))
			return "Tuan";
		else
			return
					"raj";
	}
	public JSONObject DeleteImage(long akeyName, String aUserName) {
		json = new JSONObject();
		JSONObject jsonObject = null;
		json.put("SUCCESS", 1);
		
		InsertAndDeleteItem isd = new InsertAndDeleteItem();
		HashMap<String, String> result = isd.deleteItem(imageTable, akeyName, aUserName);
		if(result==null) {
			json.put("SUCCESS", 0);
		}
		if (result.size() < 1){
			json.put("MSG","NOIMAGES");
		}
		else {
			JSONArray IMAGES=new JSONArray();
			Iterator it=result.keySet().iterator();
			json.put("MSG","IMAGES");
			
			while(it.hasNext()) {
				jsonObject=new JSONObject();
				
				String keyName = (String) it.next();
				String ImageURL = result.get(keyName);
				String UserName = ImageURL.substring(0, ImageURL.indexOf("http"));
				ImageURL = ImageURL.substring(ImageURL.indexOf("http"));
				jsonObject.put("KEYNAME", keyName);
				jsonObject.put("URL", ImageURL);
				jsonObject.put("DESCRIPTION", UserName);
				
				IMAGES.add(jsonObject);
			}
			json.put("IMAGEURL", IMAGES);
		}
			System.out.println(result);
			
		return json;
	}
		
	public JSONObject InsertImage(String akeyName) {
		json = new JSONObject();
		JSONObject jsonObject = null;
		json.put("SUCCESS", 1);
		System.out.println("1111111"+UserName+" : "+akeyName);
		InsertAndDeleteItem isd = new InsertAndDeleteItem();
		String aFileName ="/var/tmp/"+akeyName;
		String aUser = getUserName(akeyName);
		HashMap<String, String> result = isd.insertItem(imageTable, aUser, aFileName, akeyName);
		if (result.size() < 1){
			json.put("MSG","NOIMAGES");
		}
		else {
			JSONArray IMAGES=new JSONArray();
			Iterator it=result.keySet().iterator();
			json.put("MSG","IMAGES");
			
			while(it.hasNext()) {
				jsonObject=new JSONObject();
				
				String keyName = (String) it.next();
				String ImageURL = result.get(keyName);
				String UserName = ImageURL.substring(0, ImageURL.indexOf("http"));
				ImageURL = ImageURL.substring(ImageURL.indexOf("http"));
				jsonObject.put("KEYNAME", keyName);
				jsonObject.put("URL", ImageURL);
				jsonObject.put("DESCRIPTION", UserName);
				
				IMAGES.add(jsonObject);
			}
			json.put("IMAGEURL", IMAGES);
		}
			System.out.println(result);
			
		return json;
	}
	
	public JSONObject RegisterUser(String aUserName, String Password) {
		json = new JSONObject();
		MyRegistration ug = new MyRegistration();
		boolean result = ug.registerUser(userTable, aUserName, Password);

		if (result) {
			json.put("SUCCESS", 1);
			UserName = aUserName;
		}
			
		else
			json.put("SUCCESS", 0);
		return json;
	}

	public JSONObject LoginUser(String aUserName, String Password) {
		json = new JSONObject();
		json.put("SUCCESS", 1);
		System.out.println("kkkkkkk");
		JSONObject jsonObject = null;
		LoginAndSeeDetails login = new LoginAndSeeDetails();
		HashMap<String, String> result = login.userLogin(userTable, aUserName,
				Password);
		
		if (result.size() < 1){
			UserName = aUserName;
			json.put("MSG","NOIMAGES");
		}
		else if (result.containsKey("failure")) {
			json.put("SUCCESS", 0);
			json.put("MSG","Unable to login");
		}
		else {
			UserName = aUserName;
			JSONArray IMAGES=new JSONArray();
			Iterator it=result.keySet().iterator();
			json.put("MSG","IMAGES");
			
			while(it.hasNext()) {
				jsonObject=new JSONObject();
				
				String keyName = (String) it.next();
				String ImageURL = result.get(keyName);
				String UserName = ImageURL.substring(0, ImageURL.indexOf("http"));
				ImageURL = ImageURL.substring(ImageURL.indexOf("http"));
				jsonObject.put("KEYNAME", keyName);
				jsonObject.put("URL", ImageURL);
				jsonObject.put("DESCRIPTION", UserName);
				IMAGES.add(jsonObject);
			}
			json.put("IMAGEURL", IMAGES);
		}
			System.out.println(result);
		return json;
	}

	public void listMyTables() {

		TableCollection<ListTablesResult> tables = dynamoDB.listTables();
		Iterator<Table> iterator = tables.iterator();

		System.out.println("Listing table names");

		while (iterator.hasNext()) {
			Table table = iterator.next();
			System.out.println(table.getTableName());
		}
	}

	public void getTableInformation() {

		System.out.println("Describing " + imageTableName);

		TableDescription tableDescription = userTable.describe();
		System.out.format("Name: %s:\n" + "Status: %s \n"
				+ "Provisioned Throughput (read capacity units/sec): %d \n"
				+ "Provisioned Throughput (write capacity units/sec): %d \n",
				tableDescription.getTableName(), tableDescription
						.getTableStatus(), tableDescription
						.getProvisionedThroughput().getReadCapacityUnits(),
				tableDescription.getProvisionedThroughput()
						.getWriteCapacityUnits());
	}
}
