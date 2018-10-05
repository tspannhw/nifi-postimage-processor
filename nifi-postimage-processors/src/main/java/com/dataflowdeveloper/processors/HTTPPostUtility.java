package com.dataflowdeveloper.processors;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.entity.ContentType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * 
 * @author tspann
 *
 */
public class HTTPPostUtility {

	/**
	 * postImage
	 * 
	 * @param urlName
	 *            name of URL to post to
	 * @param fieldName
	 *            data or whatever your server needs. this example works with mms
	 *            model server
	 * @param imageName
	 *            make this a real name like test.jpg
	 * @param imageType
	 *            must be a valid content type like image/jpeg
	 * @param stream
	 *            InputStream of image / flowfile
	 * @return JSON results from the POST
	 */
	public static HTTPPostResults postImage(String urlName, String fieldName, String imageName, String imageType,
			InputStream stream) {

		HTTPPostResults results = new HTTPPostResults();

		try {
			HttpResponse<JsonNode> resp = Unirest.post(urlName)
					.field(fieldName, stream, ContentType.parse(imageType), imageName).asJson();

			if (resp.getBody() != null && resp.getBody().getArray() != null && resp.getBody().getArray().length() > 0) {
				for (int i = 0; i < resp.getBody().getArray().length(); i++) {
					if (resp.getBody().getArray().get(i) != null) {
						results.setJsonResultBody(resp.getBody().getArray().get(i).toString());
					}
				}
			}

			results.setHeader( resp.getHeaders().toString() );
			results.setStatus(resp.getStatusText());
			
			try {
				Unirest.shutdown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return results;
	}
}