/*
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.googlecomputeengine.features;

import org.jclouds.googlecomputeengine.internal.BaseGoogleComputeEngineApiExpectTest;
import org.jclouds.googlecomputeengine.parse.ParseDiskListTest;
import org.jclouds.googlecomputeengine.parse.ParseDiskTest;
import org.jclouds.googlecomputeengine.parse.ParseOperationTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.net.URI;

import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_READONLY_SCOPE;
import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_SCOPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

/**
 * @author David Alves
 */
@Test(groups = "unit")
public class DiskApiExpectTest extends BaseGoogleComputeEngineApiExpectTest {

   static final String DISKS_RESOURCE_URL = MYPROJECT_BASE_URL + "/zones/" + ZONE_US_CENTRAL1_A + "/disks";

   public void testGetDiskResponseIs2xx() throws Exception {
      HttpRequest get = HttpRequest
              .builder()
              .method("GET")
              .endpoint(DISKS_RESOURCE_URL + "/testimage1")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/disk_get.json")).build();

      DiskApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, get, operationResponse).getDiskApiForProjectAndZone("myproject", ZONE_US_CENTRAL1_A);

      assertEquals(api.get("testimage1"),
              new ParseDiskTest().expected());
   }

   public void testGetDiskResponseIs4xx() throws Exception {
      HttpRequest get = HttpRequest
              .builder()
              .method("GET")
              .endpoint(DISKS_RESOURCE_URL + "/testimage1")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(404).build();

      DiskApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, get, operationResponse).getDiskApiForProjectAndZone("myproject", ZONE_US_CENTRAL1_A);

      assertNull(api.get("testimage1"));
   }

   public void testInsertDiskResponseIs2xx() {
      HttpRequest insert = HttpRequest
              .builder()
              .method("POST")
              .endpoint(DISKS_RESOURCE_URL)
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN)
              .payload(payloadFromResourceWithContentType("/disk_insert.json", MediaType.APPLICATION_JSON))
              .build();

      HttpResponse insertDiskResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/operation.json")).build();

      DiskApi api = requestsSendResponses(requestForScopes(COMPUTE_SCOPE),
              TOKEN_RESPONSE, insert,
              insertDiskResponse).getDiskApiForProjectAndZone("myproject", ZONE_US_CENTRAL1_A);

      assertEquals(api.createInZone("testimage1", 1, URI.create(ZoneApiExpectTest.ZONES_RESOURCE_URL + "/us-central1-a"))
              , new ParseOperationTest().expected());
   }

   public void testDeleteDiskResponseIs2xx() {
      HttpRequest delete = HttpRequest
              .builder()
              .method("DELETE")
              .endpoint(DISKS_RESOURCE_URL + "/testimage1")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/operation.json")).build();

      DiskApi api = requestsSendResponses(requestForScopes(COMPUTE_SCOPE),
              TOKEN_RESPONSE, delete, deleteResponse).getDiskApiForProjectAndZone("myproject", ZONE_US_CENTRAL1_A);

      assertEquals(api.delete("testimage1"),
              new ParseOperationTest().expected());
   }

   public void testDeleteDiskResponseIs4xx() {
      HttpRequest delete = HttpRequest
              .builder()
              .method("DELETE")
              .endpoint(DISKS_RESOURCE_URL + "/testimage1")
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(404).build();

      DiskApi api = requestsSendResponses(requestForScopes(COMPUTE_SCOPE),
              TOKEN_RESPONSE, delete, deleteResponse).getDiskApiForProjectAndZone("myproject", ZONE_US_CENTRAL1_A);

      assertNull(api.delete("testimage1"));
   }

   public void testListDisksResponseIs2xx() {
      HttpRequest list = HttpRequest
              .builder()
              .method("GET")
              .endpoint(DISKS_RESOURCE_URL)
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(200)
              .payload(payloadFromResource("/disk_list.json")).build();

      DiskApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, list, operationResponse).getDiskApiForProjectAndZone("myproject", ZONE_US_CENTRAL1_A);

      assertEquals(api.listFirstPage().toString(),
              new ParseDiskListTest().expected().toString());
   }

   public void testListDisksResponseIs4xx() {
      HttpRequest list = HttpRequest
              .builder()
              .method("GET")
              .endpoint(DISKS_RESOURCE_URL)
              .addHeader("Accept", "application/json")
              .addHeader("Authorization", "Bearer " + TOKEN).build();

      HttpResponse operationResponse = HttpResponse.builder().statusCode(404).build();

      DiskApi api = requestsSendResponses(requestForScopes(COMPUTE_READONLY_SCOPE),
              TOKEN_RESPONSE, list, operationResponse).getDiskApiForProjectAndZone("myproject", ZONE_US_CENTRAL1_A);

      assertTrue(api.list().concat().isEmpty());
   }
}
