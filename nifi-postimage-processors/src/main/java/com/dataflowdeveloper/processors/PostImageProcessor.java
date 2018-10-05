/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dataflowdeveloper.processors;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.util.StandardValidators;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tags({"post images"})
@CapabilityDescription("Post Image to HTTP")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="url, fieldname, imagename, imagetype", description="Need URL, Field Name, Image Name and Image Type.")})
@WritesAttributes({@WritesAttribute(attribute="post.results, post.header, post.status, post.statuscode", description="Output result of HTTP Post call.")})
public class PostImageProcessor extends AbstractProcessor {

	/** output attribute name post.results will contain JSON **/
	public static final String ATTRIBUTE_OUTPUT_NAME = "post.results";

	/** output attribute name post.header will contain JSON **/
	public static final String ATTRIBUTE_OUTPUT_HEADER = "post.header";
	
	/** output attribute name post.status will contain JSON **/
	public static final String ATTRIBUTE_OUTPUT_STATUS = "post.status";

	/** output attribute name post.statuscode will contain JSON **/
	public static final String ATTRIBUTE_OUTPUT_STATUS_CODE = "post.statuscode";
	
	/** url http://127.0.0.1:9999/squeezenet/predict  */
	public static final PropertyDescriptor URL_NAME = new PropertyDescriptor.Builder().name("url")
			.description("URL Name like http://127.0.0.1:9999/squeezenet/predict").required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR).expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES).build();

	/** fieldname "data" */
	public static final PropertyDescriptor FIELD_NAME = new PropertyDescriptor.Builder().name("fieldname")
			.description("Field Name like data").required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR).expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES).build();

	/** imagename imageName  "/TimKafka.jpg" */
	public static final PropertyDescriptor IMAGE_NAME = new PropertyDescriptor.Builder().name("imagename")
			.description("Image Name like TimLovesNiFi.jpg").required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR).expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES).build();

	/** imageType "image/jpeg" */
	public static final PropertyDescriptor IMAGE_TYPE = new PropertyDescriptor.Builder().name("imagetype")
			.description("Image Type like image/jpeg").required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR).expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES).build();
	
	
	/** Success of Relationship */
	public static final Relationship REL_SUCCESS = new Relationship.Builder().name("success")
			.description("Successfully determined image.").build();

	/** Failure of Relationship **/
	public static final Relationship REL_FAILURE = new Relationship.Builder().name("failure")
			.description("Failed to determine image.").build();

	/** Descriptors */
    private List<PropertyDescriptor> descriptors;

    /** Relationships */
    private Set<Relationship> relationships;

    /** Initialize */
    @Override
    protected void init(final ProcessorInitializationContext context) {
		final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
		descriptors.add(URL_NAME);
		descriptors.add(FIELD_NAME);
		descriptors.add(IMAGE_NAME);
		descriptors.add(IMAGE_TYPE);
		
		this.descriptors = Collections.unmodifiableList(descriptors);

		final Set<Relationship> relationships = new HashSet<Relationship>();
		relationships.add(REL_SUCCESS);
		relationships.add(REL_FAILURE);
		this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {
    	return;
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        if ( flowFile == null ) {
            return;
        }
		try {
			flowFile.getAttributes();
			
			String urlName = flowFile.getAttribute("url");
			if (urlName == null) {
				urlName = context.getProperty("url").evaluateAttributeExpressions(flowFile).getValue();
			}
			if (urlName == null) {
				urlName = "http://localhost:8080/nifi";
			}

			String fieldname = flowFile.getAttribute("fieldname");
			if (fieldname == null) {
				fieldname = context.getProperty("fieldname").evaluateAttributeExpressions(flowFile).getValue();
			}
			if (fieldname == null) {
				fieldname = "data";
			}
			
			String imagename = flowFile.getAttribute("imagename");
			if (imagename == null) {
				imagename = context.getProperty("imagename").evaluateAttributeExpressions(flowFile).getValue();
			}
			if (imagename == null) {
				imagename = "test.jpg";
			}		
			
			String imagetype = flowFile.getAttribute("imagetype");
			if (imagetype == null) {
				imagetype = context.getProperty("imagetype").evaluateAttributeExpressions(flowFile).getValue();
			}
			if (imagetype == null) {
				imagetype = "images/jpeg";
			}
			
			final String url = urlName;
			final String field = fieldname;
			final String image = imagename;
			final String imgtype = imagetype;
			
			try {
				final HashMap<String, String> attributes = new HashMap<String, String>();

				session.read(flowFile, new InputStreamCallback() {
					@Override
					public void process(InputStream input) throws IOException {
						if ( input == null ) { 
							return;
						}
						HTTPPostResults results = HTTPPostUtility.postImage(url, field, image, imgtype, input);
						
						if (results != null && results.getJsonResultBody() != null) {
							try {
								attributes.put(ATTRIBUTE_OUTPUT_NAME, results.getJsonResultBody());
								attributes.put(ATTRIBUTE_OUTPUT_HEADER, results.getHeader());
								attributes.put(ATTRIBUTE_OUTPUT_STATUS, results.getStatus());
								attributes.put(ATTRIBUTE_OUTPUT_STATUS_CODE, String.valueOf(results.getStatusCode()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						else {
							try {
								System.out.println("====> url" + url + "," + field + ","+ image + "," + imgtype );
								attributes.put(ATTRIBUTE_OUTPUT_NAME, "Fail");
								attributes.put(ATTRIBUTE_OUTPUT_HEADER, "Fail");
								attributes.put(ATTRIBUTE_OUTPUT_STATUS, "FAIL");
								attributes.put(ATTRIBUTE_OUTPUT_STATUS_CODE, "500");
							} catch (Exception e) {
								e.printStackTrace();
							}							
						}
					}
				});
				if (attributes.size() == 0) {
					session.transfer(flowFile, REL_FAILURE);
				} else {
					flowFile = session.putAllAttributes(flowFile, attributes);
					session.transfer(flowFile, REL_SUCCESS);
				}
			} catch (Exception e) {
				throw new ProcessException(e);
			}

			session.commit();
		} catch (

		final Throwable t) {
			getLogger().error("Unable to process Post Image Processor file " + t.getLocalizedMessage());
			throw new ProcessException(t);
		}
    }
}