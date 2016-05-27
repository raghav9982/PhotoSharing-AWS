import java.io.File;
import java.util.HashMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class InsertAndDeleteItem {

	String bucketName = "krazzybucket";

	public HashMap<String, String> insertItem(Table tableName,
			String aUserName, String aFileName, String aKeyName) {

		try {

			// Insert image into S3
			AmazonS3 s3client = new AmazonS3Client(new AWSCredentials() {

				@Override
				public String getAWSSecretKey() {
					return "BBUMwI35mGxEUyRwIwZtNeI3QcjiPtZye+u8U/vn";
				}

				@Override
				public String getAWSAccessKeyId() {
					return "AKIAJZJMI3F5MTWEHHNA";
				}
			});

			System.out.println("Uploading a new file to S3 ..\n");
			File file = new File(aFileName);
			PutObjectRequest por = new PutObjectRequest(bucketName, aKeyName,
					file);
			por.setCannedAcl(CannedAccessControlList.PublicRead);
			s3client.putObject(por);

			// Store keyname in DynamoDB(imageTableName)
			long hashId = System.currentTimeMillis();

			Item item = new Item().withPrimaryKey("Id", hashId)
					.withString("ImageName", aKeyName)
					.withString("UserName", aUserName);
			PutItemOutcome outcome = tableName.putItem(item);

		} catch (Exception e) {
			System.out.println(e);
		}
		return Util.retriveImages();
	}

	public HashMap<String, String> deleteItem(Table tableName, long aId,
			String aUserName) {

		Item item = tableName.getItem("Id", aId);
		String imageName = item.getString("ImageName");
		System.out.println(imageName + " : ------" + aId);
		if (!item.getString("UserName").equalsIgnoreCase(aUserName))
			return null;

		// Delete the row from the DynamoDB
		DeleteItemOutcome outcome = tableName.deleteItem("Id", aId);

		// Delete from Amazon S3
		try {
			AmazonS3 s3client = new AmazonS3Client(new AWSCredentials() {

				@Override
				public String getAWSSecretKey() {
					return "BBUMwI35mGxEUyRwIwZtNeI3QcjiPtZye+u8U/vn";
				}

				@Override
				public String getAWSAccessKeyId() {
					return "AKIAJZJMI3F5MTWEHHNA";
				}
			});

			s3client.deleteObject(bucketName, imageName);

		} catch (Exception e) {

		}
		return Util.retriveImages();

	}
}
