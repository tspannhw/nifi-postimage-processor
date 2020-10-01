package com.dataflowdeveloper.processors;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//import org.apache.http.entity.ContentType;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.ContentType;
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
											InputStream stream, String headerName, String headerValue,
											String basicUsername, String basicPassword, Map<String, Object> fields) {

		if ( urlName == null || fieldName == null || imageName == null || imageType == null || stream == null ) {
			return null;
		}
		
		HTTPPostResults results = new HTTPPostResults();

		try {
			/** Do we want a timeout
			// do we want to allow users to set this
			// connectionTimeout
			// connectionTimeout
			// http://unirest.io/java.html

			 Need base auth

			 .basicAuth("username", "password")

			 add

			 headers

			 .header("accept", "application/json")

			*/
		//	Unirest.setTimeouts(90000, 180000); //setTimeouts MUST only be used one time. It create a new client-pool and leaves the old client-pool dangling == memory leak
			//Unirest.verifySsl(false);
//			Unirest.config().verifySsl(false);
			
			HttpResponse<JsonNode> resp = null;

			if (headerName == null || headerName.length() <= 0 || headerValue == null || headerValue.length() <= 0) {
				if(imageType != null && imageType.length() > 0)
				{
					resp = Unirest.post(urlName).fields(fields)
						.field(fieldName, stream, ContentType.create(imageType) ,imageName)
						.asJson();
				}
				else
				{
					resp = Unirest.post(urlName).fields(fields)
						.field(fieldName, stream ,imageName)
						.asJson();
				}
			}
			else {
				if(imageType != null && imageType.length() > 0)
				{
					resp = Unirest.post(urlName)
						.header(headerName,headerValue).fields(fields)
						.field(fieldName, stream, ContentType.create(imageType), imageName)
						.asJson();
				}
				else
				{
					resp = Unirest.post(urlName)
						.header(headerName,headerValue).fields(fields)
						.field(fieldName, stream, imageName)
						.asJson();
				}
			}

			if (resp.getBody() != null && resp.getBody().getArray() != null && resp.getBody().getArray().length() > 0) {
				for (int i = 0; i < resp.getBody().getArray().length(); i++) {
					if (resp.getBody().getArray().get(i) != null) {
						results.setJsonResultBody(resp.getBody().getArray().get(i).toString());
					}
				}
			}

			if ( resp.getHeaders() != null) { 
				results.setHeader( resp.getHeaders().toString() );
			}
			if ( resp.getStatusText() != null ) { 
				results.setStatus(resp.getStatusText());
			}
			
			results.setStatusCode(resp.getStatus());
			
//			try {
//				Unirest.shutdown();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return results;
	}
}
