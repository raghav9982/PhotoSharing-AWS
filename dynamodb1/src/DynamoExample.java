import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

public class DynamoExample {

	static String userTableName = "UserTable";
	static String imageTableName = "ImageTable";
	
	static DynamoDB dynamoDB;
	static AmazonDynamoDBClient client;
	static Table table;

	public static void main(String[] args) throws Exception {

		DynamoExample db = new DynamoExample();
		
		
		//db.createTable();
		
		db.listMyTables();
		// db.getTableInformation();
		// db.updateExampleTable();

		 //deleteExampleTable();
		//db.insertUsrIds(103, "testing");
		
		//db.deleteUsrIds(100);
		//db.retrieveUsrIds(100);
		//db.scanItems();
	}
	
	static String getDescription(){
		return "";
	}

	public DynamoExample() {
		client = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
		client.setRegion(Region.getRegion(Regions.US_WEST_2));
		dynamoDB = new DynamoDB(client);
		table = dynamoDB.getTable("test");
	}
	public void createTable() {
		try {

			ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition()
					.withAttributeName("Id").withAttributeType("N"));

			ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
			keySchema.add(new KeySchemaElement().withAttributeName("Id")
					.withKeyType(KeyType.HASH));

			CreateTableRequest request = new CreateTableRequest()
					.withTableName(imageTableName)
					.withKeySchema(keySchema)
					.withAttributeDefinitions(attributeDefinitions)
					.withProvisionedThroughput(
							new ProvisionedThroughput().withReadCapacityUnits(
									5L).withWriteCapacityUnits(6L));

			System.out.println("Issuing CreateTable request for " + imageTableName);
			Table table = dynamoDB.createTable(request);

			System.out.println("Waiting for " + imageTableName
					+ " to be created...this may take a while...");
			table.waitForActive();

			getTableInformation();

		} catch (Exception e) {
			System.err.println("CreateTable request failed for " + imageTableName);
			System.err.println(e.getMessage());
		}

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

		TableDescription tableDescription = dynamoDB.getTable(imageTableName)
				.describe();
		System.out.format("Name: %s:\n" + "Status: %s \n"
				+ "Provisioned Throughput (read capacity units/sec): %d \n"
				+ "Provisioned Throughput (write capacity units/sec): %d \n",
				tableDescription.getTableName(), tableDescription
						.getTableStatus(), tableDescription
						.getProvisionedThroughput().getReadCapacityUnits(),
				tableDescription.getProvisionedThroughput()
						.getWriteCapacityUnits());
	}

	public void updateExampleTable() {

		Table table = dynamoDB.getTable(imageTableName);
		System.out.println("Modifying provisioned throughput for " + imageTableName);

		try {
			table.updateTable(new ProvisionedThroughput()
					.withReadCapacityUnits(6L).withWriteCapacityUnits(7L));

			table.waitForActive();
		} catch (Exception e) {
			System.err.println("UpdateTable request failed for " + imageTableName);
			System.err.println(e.getMessage());
		}
	}

	public void deleteExampleTable() {

		Table table = dynamoDB.getTable(imageTableName);
		try {
			System.out.println("Issuing DeleteTable request for " + imageTableName);
			table.delete();

			System.out.println("Waiting for " + imageTableName
					+ " to be deleted...this may take a while...");

			table.waitForDelete();
		} catch (Exception e) {
			System.err.println("DeleteTable request failed for " + imageTableName);
			System.err.println(e.getMessage());
		}
	}

	public void insertUsrIds(int Id, String imageName) {

		Item item = new Item().withPrimaryKey("Id", Id).withString("ImageName",
				imageName);

		
		PutItemOutcome outcome = table.putItem(item);
	}
	
	public void retrieveUsrIds(int aId) {

		Item item = table.getItem("Id",aId);
		System.out.println(item.getString("ImageName"));
	}
	
	public void deleteUsrIds(int aId) {

		DeleteItemOutcome outcome = table.deleteItem("Id",aId);
		System.out.println(outcome);
	}
	
	public void scanItems() {
		ScanRequest scanRequest = new ScanRequest()
				.withLimit(10)
			    .withTableName(imageTableName);

			ScanResult result = client.scan(scanRequest);
			for (Map<String, AttributeValue> item : result.getItems()){
				AttributeValue av = item.get("ImageName");
				System.out.println(av.getS());
				
			}
	}
	
	
	
}