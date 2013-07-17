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

import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_READONLY_SCOPE;
import static org.jclouds.googlecomputeengine.GoogleComputeEngineConstants.COMPUTE_SCOPE;

import java.net.URI;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.EmptyIterableWithMarkerOnNotFoundOr404;
import org.jclouds.Fallbacks.EmptyPagedIterableOnNotFoundOr404;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.collect.PagedIterable;
import org.jclouds.googlecomputeengine.domain.Disk;
import org.jclouds.googlecomputeengine.domain.ListPage;
import org.jclouds.googlecomputeengine.domain.Operation;
import org.jclouds.googlecomputeengine.functions.internal.ParseDisks;
import org.jclouds.googlecomputeengine.options.ListOptions;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.oauth.v2.config.OAuthScopes;
import org.jclouds.oauth.v2.filters.OAuthAuthenticator;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.ResponseParser;
import org.jclouds.rest.annotations.SkipEncoding;
import org.jclouds.rest.annotations.Transform;
import org.jclouds.rest.binders.BindToJsonPayload;

/**
 * Provides access to Disks via their REST API.
 *
 * @author David Alves
 * @see <a href="https://developers.google.com/compute/docs/reference/v1beta13/disks"/>
 */
@SkipEncoding({'/', '='})
@RequestFilters(OAuthAuthenticator.class)
public interface DiskApi {

   /**
    * Returns the specified persistent disk resource.
    *
    * @param diskName name of the persistent disk resource to return.
    * @return a Disk resource.
    */
   @Named("Disks:get")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/disks/{disk}")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   Disk get(@PathParam("disk") String diskName);

   /**
    * Creates a persistent disk resource in the specified project specifying the size of the disk.
    *
    *
    * @param diskName the name of disk.
    * @param sizeGb   the size of the disk
    * @param zone     the URi of the zone where the disk is to be created.
    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
    *         you, and look for the status field.
    */
   @Named("Disks:insert")
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/disks")
   @OAuthScopes({COMPUTE_SCOPE})
   @MapBinder(BindToJsonPayload.class)
   Operation createInZone(@PayloadParam("name") String diskName,
                                            @PayloadParam("sizeGb") int sizeGb,
                                            @PayloadParam("zone") @Nullable URI zone);

   /**
    * Deletes the specified persistent disk resource.
    *
    * @param diskName name of the persistent disk resource to delete.
    * @return an Operation resource. To check on the status of an operation, poll the Operations resource returned to
    *         you, and look for the status field.
    */
   @Named("Disks:delete")
   @DELETE
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/disks/{disk}")
   @OAuthScopes(COMPUTE_SCOPE)
   @Fallback(NullOnNotFoundOr404.class)
   @Nullable
   Operation delete(@PathParam("disk") String diskName);

   /**
    * @see DiskApi#listAtMarker(String, org.jclouds.googlecomputeengine.options.ListOptions)
    */
   @Named("Disks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/disks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseDisks.class)
   @Fallback(EmptyIterableWithMarkerOnNotFoundOr404.class)
   ListPage<Disk> listFirstPage();

   /**
    * @see DiskApi#listAtMarker(String, org.jclouds.googlecomputeengine.options.ListOptions)
    */
   @Named("Disks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/disks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseDisks.class)
   @Fallback(EmptyIterableWithMarkerOnNotFoundOr404.class)
   ListPage<Disk> listAtMarker(@QueryParam("pageToken") @Nullable String marker);

   /**
    * Retrieves the listPage of persistent disk resources contained within the specified project.
    * By default the listPage as a maximum size of 100, if no options are provided or ListOptions#getMaxResults() has
    * not been set.
    *
    * @param marker      marks the beginning of the next list page
    * @param listOptions listing options
    * @return a page of the listPage
    * @see ListOptions
    * @see org.jclouds.googlecomputeengine.domain.ListPage
    */
   @Named("Disks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/disks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseDisks.class)
   @Fallback(EmptyIterableWithMarkerOnNotFoundOr404.class)
   ListPage<Disk> listAtMarker(@QueryParam("pageToken") @Nullable String marker, ListOptions options);

   /**
    * A paged version of DiskApi#listPage()
    *
    * @return a Paged, Fluent Iterable that is able to fetch additional pages when required
    * @see PagedIterable
    * @see DiskApi#listAtMarker(String, org.jclouds.googlecomputeengine.options.ListOptions)
    */
   @Named("Disks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/disks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseDisks.class)
   @Transform(ParseDisks.ToPagedIterable.class)
   @Fallback(EmptyPagedIterableOnNotFoundOr404.class)
   PagedIterable<Disk> list();

   @Named("Disks:list")
   @GET
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("/disks")
   @OAuthScopes(COMPUTE_READONLY_SCOPE)
   @ResponseParser(ParseDisks.class)
   @Transform(ParseDisks.ToPagedIterable.class)
   @Fallback(EmptyPagedIterableOnNotFoundOr404.class)
   PagedIterable<Disk> list(ListOptions options);
}
