/**
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
package org.jclouds.googlecomputeengine.internal;

import org.jclouds.googlecomputeengine.GoogleComputeEngineApi;

import java.util.Properties;

/**
 * @author Adrian Cole
 */
public class BaseGoogleComputeEngineApiExpectTest extends BaseGoogleComputeEngineExpectTest<GoogleComputeEngineApi> {

   public static final String GOOGLE_COMPUTE_BASE_URL = "https://www.googleapis.com/compute/v1beta13";
   public static final String MYPROJECT_BASE_URL = GOOGLE_COMPUTE_BASE_URL + "/projects/myproject";
   public static final String GOOGLE_PROJECT_BASE_URL = GOOGLE_COMPUTE_BASE_URL + "/projects/google";

   @Override
   protected Properties setupProperties() {
      Properties properties = super.setupProperties();
      properties.put("google-compute-engine.identity", "myproject");
      return properties;
   }
}
