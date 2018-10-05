package com.dataflowdeveloper.processors;

import java.io.Serializable;

/**
 * 
 * @author tspann
 *
 */
public class HTTPPostResults implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8940653484664074569L;
	
	private String header;
	private String status;
	private String jsonResultBody;
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getJsonResultBody() {
		return jsonResultBody;
	}
	public void setJsonResultBody(String jsonResultBody) {
		this.jsonResultBody = jsonResultBody;
	}
	
	
	
	public HTTPPostResults() {
		super();
	}

	
	public HTTPPostResults(String header, String status, String jsonResultBody) {
		super();
		this.header = header;
		this.status = status;
		this.jsonResultBody = jsonResultBody;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((jsonResultBody == null) ? 0 : jsonResultBody.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HTTPPostResults other = (HTTPPostResults) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (jsonResultBody == null) {
			if (other.jsonResultBody != null)
				return false;
		} else if (!jsonResultBody.equals(other.jsonResultBody))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HTTPPostResults [header=");
		builder.append(header);
		builder.append(", status=");
		builder.append(status);
		builder.append(", jsonResultBody=");
		builder.append(jsonResultBody);
		builder.append("]");
		return builder.toString();
	}	
}