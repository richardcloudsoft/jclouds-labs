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
package org.jclouds.googlecomputeengine.config;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Suppliers.compose;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import org.jclouds.domain.Credentials;
import org.jclouds.googlecomputeengine.GoogleComputeEngineApi;
import org.jclouds.googlecomputeengine.domain.Operation;
import org.jclouds.googlecomputeengine.domain.Project;
import org.jclouds.googlecomputeengine.handlers.GoogleComputeEngineErrorHandler;
import org.jclouds.googlecomputeengine.predicates.OperationDonePredicate;
import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.Uris;
import org.jclouds.http.annotation.ClientError;
import org.jclouds.http.annotation.Redirection;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.json.config.GsonModule.DateAdapter;
import org.jclouds.json.config.GsonModule.Iso8601DateAdapter;
import org.jclouds.location.Provider;
import org.jclouds.rest.ConfiguresHttpApi;
import org.jclouds.rest.config.HttpApiModule;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;

/**
 * Configures the GoogleCompute connection.
 *
 * @author David Alves
 */
@ConfiguresHttpApi
public class GoogleComputeEngineHttpApiModule extends HttpApiModule<GoogleComputeEngineApi> {
   public GoogleComputeEngineHttpApiModule() {
   }

   @Override
   protected void configure() {
      bind(DateAdapter.class).to(Iso8601DateAdapter.class);
      bind(new TypeLiteral<Predicate<AtomicReference<Operation>>>() {}).to(OperationDonePredicate.class);
      super.configure();
   }

   @Override
   protected void bindErrorHandlers() {
      bind(HttpErrorHandler.class).annotatedWith(Redirection.class).to(GoogleComputeEngineErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ClientError.class).to(GoogleComputeEngineErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ServerError.class).to(GoogleComputeEngineErrorHandler.class);
   }

   @Provides
   @Singleton
   @UserProject
   public Supplier<String> supplyProject(@org.jclouds.location.Provider final Supplier<Credentials> creds, final GoogleComputeEngineApi googleComputeEngineApi) {
      return Suppliers.memoize(new Supplier<String>() {
         @Override
         public String get() {
            List<String> split = ImmutableList.copyOf(Splitter.on("@").split(creds.get().identity));

            // If identity does not contain an "@", then use it directly
            if (split.size() == 1)
               return split.get(0);

            // Otherwise assume before the @ is a project ID, and query the API to get the project name
            String tempProjectName = Iterables.get(split, 0);
            Project project = googleComputeEngineApi.getProjectApi().get(tempProjectName);
            return project.getName();
         }
      });
   }

   @Provides
   @Singleton
   @Named("machineTypes")
   public Function<String, URI> provideMachineTypeNameToURIFunction(final @Provider Supplier<URI> endpoint,
                                                                    final @UserProject Supplier<String> userProject) {
      return new Function<String, URI>() {
         @Override
         public URI apply(String input) {
            return Uris.uriBuilder(endpoint.get()).appendPath("/projects/").appendPath(userProject.get()).appendPath
                    ("/machineTypes/").appendPath(input).build();
         }
      };
   }

   @Provides
   @Singleton
   @Named("networks")
   public Function<String, URI> provideNetworkNameToURIFunction(final @Provider Supplier<URI> endpoint,
                                                                final @UserProject Supplier<String> userProject) {
      return new Function<String, URI>() {
         @Override
         public URI apply(String input) {
            return Uris.uriBuilder(endpoint.get()).appendPath("/projects/").appendPath(userProject.get()).appendPath
                    ("/networks/").appendPath(input).build();
         }
      };
   }

   @Provides
   @Singleton
   @Named("zones")
   public Function<String, URI> provideZoneNameToURIFunction(final @Provider Supplier<URI> endpoint,
                                                             final @UserProject Supplier<String> userProject) {
      return new Function<String, URI>() {
         @Override
         public URI apply(String input) {
            return Uris.uriBuilder(endpoint.get()).appendPath("/projects/").appendPath(userProject.get()).appendPath
                    ("/zones/").appendPath(input).build();
         }
      };
   }
}
