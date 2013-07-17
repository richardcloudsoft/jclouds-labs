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
package org.jclouds.googlecomputeengine;

import java.io.Closeable;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jclouds.googlecomputeengine.features.DiskApi;
import org.jclouds.googlecomputeengine.features.FirewallApi;
import org.jclouds.googlecomputeengine.features.ImageApi;
import org.jclouds.googlecomputeengine.features.InstanceApi;
import org.jclouds.googlecomputeengine.features.KernelApi;
import org.jclouds.googlecomputeengine.features.MachineTypeApi;
import org.jclouds.googlecomputeengine.features.NetworkApi;
import org.jclouds.googlecomputeengine.features.OperationApi;
import org.jclouds.googlecomputeengine.features.ProjectApi;
import org.jclouds.googlecomputeengine.features.ZoneApi;
import org.jclouds.rest.annotations.Delegate;

import com.google.common.annotations.Beta;


/**
 * Provides access to GoogleCompute.
 * <p/>
 *
 * @author David Alves
 * @see <a href="https://developers.google.com/compute/docs/reference/v1beta13">api doc</a>
 */
@Beta
public interface GoogleComputeEngineApi extends Closeable {

   /**
    * Provides access to Disk features
    *
    * @param projectName the name of the project
    * @param zoneName the name of the zone
    */
   @Delegate
   @Path("/projects/{project}/zones/{zone}")
   DiskApi getDiskApiForProjectAndZone(@PathParam("project") String projectName, @PathParam("zone") String zoneName);

   /**
    * Provides access to Firewall features
    *
    * @param projectName the name of the project
    */
   @Delegate
   @Path("/projects/{project}/global")
   FirewallApi getFirewallApiForProject(@PathParam("project") String projectName);

   /**
    * Provides access to Image features
    *
    * @param projectName the name of the project
    */
   @Delegate
   @Path("/projects/{project}/global")
   ImageApi getImageApiForProject(@PathParam("project") String projectName);

   /**
    * Provides access to Instance features
    *
    * @param projectName the name of the project
    * @param zoneName the name of the zone
    */
   @Delegate
   @Path("/projects/{project}/zones/{zone}")
   InstanceApi getInstanceApiForProjectAndZone(@PathParam("project") String projectName, @PathParam("zone") String zoneName);

   /**
    * Provides access to Kernel features
    *
    * @param projectName the name of the project
    */
   @Delegate
   @Path("/projects/{project}/global")
   KernelApi getKernelApiForProject(@PathParam("project") String projectName);

   /**
    * Provides access to MachineType features
    *
    * @param projectName the name of the project
    */
   @Delegate
   @Path("/projects/{project}/global")
   MachineTypeApi getMachineTypeApiForProject(@PathParam("project") String projectName);

   /**
    * Provides access to Network features
    *
    * @param projectName the name of the project
    */
   @Delegate
   @Path("/projects/{project}/global")
   NetworkApi getNetworkApiForProject(@PathParam("project") String projectName);

   /**
    * Provides access to Operation features on global resources
    *
    * @param projectName the name of the project
    */
   @Delegate
   @Path("/projects/{project}/global")
   OperationApi getGlobalOperationApiForProject(@PathParam("project") String projectName);

   /**
    * Provides access to Operation features in a specific zone
    *
    * @param projectName the name of the project
    * @param zoneName the name of the zone
    */
   @Delegate
   @Path("/projects/{project}/zones/{zone}")
   OperationApi getOperationApiForProjectAndZone(@PathParam("project") String projectName, @PathParam("zone") String zoneName);

   /**
    * Provides access to Project features
    */
   @Delegate
   ProjectApi getProjectApi();

   /**
    * Provides access to Zone features
    *
    * @param projectName the name of the project
    */
   @Delegate
   @Path("/projects/{project}")
   ZoneApi getZoneApiForProject(@PathParam("project") String projectName);


}
