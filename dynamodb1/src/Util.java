import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;


public class Util {
	
static HashMap<String,String> retriveImages() {
		
	HashMap<String,String> images=new HashMap<>();
		
		ScanRequest scanRequest = new ScanRequest()
				.withLimit(4)
				.withTableName(driver.imageTableName);
		
		ScanResult result = driver.client.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			AttributeValue avImage = item.get("ImageName");
			AttributeValue avId = item.get("Id");
			AttributeValue avUser = item.get("UserName");
			images.put(avId.getN(), avUser.getS()+"https://s3.amazonaws.com/krazzybucket/"+avImage.getS());			
			}
		
		return images;
	}
}
