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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * 
 * @author tspann
 *
 */
public class PostImageProcessorTest {

	/**
	 * 
	 */
    private TestRunner testRunner;

    /**
     * 
     */
    @Before
    public void init() {
        testRunner = TestRunners.newTestRunner(PostImageProcessor.class);
    }

    /**
     * Test is integration and requires a server running.
     */
    @Test
    public void testProcessor() {
//    	testRunner.setProperty("url", "https://api.imgur.com/3/upload");
//    	testRunner.setProperty("fieldname", "image");
//    	testRunner.setProperty("imagename", "IMG_2596.jpg");
//    	testRunner.setProperty("imagetype", "image/jpeg");
//		testRunner.setProperty("headername", "Authorization");
//		testRunner.setProperty("headervalue", "Client-ID abc");

    //	testRunner.enqueue(this.getClass().getClassLoader().getResourceAsStream("IMG_2596.jpg"));

    //	runAndAssertHappy();
    }

    /**
     * 
     */
	private void runAndAssertHappy() {
		testRunner.setValidateExpressionUsage(false);
		testRunner.run();
		testRunner.assertValid();
		testRunner.assertAllFlowFilesTransferred(PostImageProcessor.REL_SUCCESS);
		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(PostImageProcessor.REL_SUCCESS);

		for (MockFlowFile mockFile : successFiles) {
			Map<String, String> attributes =  mockFile.getAttributes();

			for (String attribute : attributes.keySet()) {
				System.out.println("Attribute:" + attribute + " = " + mockFile.getAttribute(attribute));
			}

//			assertEquals("giant panda", mockFile.getAttribute("label_1"));
//			assertEquals("95.23%", mockFile.getAttribute("probability_1"));
			assertNotNull(mockFile.getAttribute("post.header"));
			assertNotNull(mockFile.getAttribute("post.results"));
			assertEquals("OK", mockFile.getAttribute("post.status"));
		}

	}
}