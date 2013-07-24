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

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.jclouds.googlecomputeengine.domain.Firewall.Rule.IPProtocol;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.jclouds.collect.PagedIterable;
import org.jclouds.googlecomputeengine.domain.Firewall;
import org.jclouds.googlecomputeengine.internal.BaseGoogleComputeEngineApiLiveTest;
import org.jclouds.googlecomputeengine.options.FirewallOptions;
import org.jclouds.googlecomputeengine.options.ListOptions;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

/**
 * @author David Alves
 */
public class FirewallApiLiveTest extends BaseGoogleComputeEngineApiLiveTest {

   private static final String FIREWALL_NAME = "firewall-api-live-test-firewall";
   private static final String FIREWALL_NETWORK_NAME = "firewall-api-live-test-network";
   private static final String IPV4_RANGE = "10.0.0.0/8";
   private static final int TIME_WAIT = 30;

   private FirewallApi api() {
      return api.getFirewallApiForProject(userProject.get());
   }

   @Test(groups = "live")
   public void testInsertFirewall() {

      // need to create the network first
      assertGlobalOperationDoneSucessfully(api.getNetworkApiForProject(userProject.get()).createInIPv4Range
              (FIREWALL_NETWORK_NAME, IPV4_RANGE), TIME_WAIT);

      FirewallOptions firewall = new FirewallOptions()
              .addAllowedRule(
                      Firewall.Rule.builder()
                              .IPProtocol(IPProtocol.TCP)
                              .addPort(22).build())
              .addSourceRange("10.0.0.0/8")
              .addSourceTag("tag1")
              .addTargetTag("tag2");

      assertGlobalOperationDoneSucessfully(api().createInNetwork(FIREWALL_NAME, getNetworkUrl(userProject.get(),
              FIREWALL_NETWORK_NAME), firewall), TIME_WAIT);

   }

   @Test(groups = "live", dependsOnMethods = "testInsertFirewall")
   public void testUpdateFirewall() {

      FirewallOptions firewall = new FirewallOptions()
              .name(FIREWALL_NAME)
              .network(getNetworkUrl(userProject.get(), FIREWALL_NETWORK_NAME))
              .addSourceRange("10.0.0.0/8")
              .addSourceTag("tag1")
              .addTargetTag("tag2")
              .allowedRules(ImmutableSet.of(
                      Firewall.Rule.builder()
                              .IPProtocol(IPProtocol.TCP)
                              .addPort(23)
                              .build()));


      assertGlobalOperationDoneSucessfully(api().update(FIREWALL_NAME, firewall), TIME_WAIT);

   }

   @Test(groups = "live", dependsOnMethods = "testUpdateFirewall")
   public void testPatchFirewall() {

      FirewallOptions firewall = new FirewallOptions()
              .name(FIREWALL_NAME)
              .network(getNetworkUrl(userProject.get(), FIREWALL_NETWORK_NAME))
              .allowedRules(ImmutableSet.of(
                      Firewall.Rule.builder()
                              .IPProtocol(IPProtocol.TCP)
                              .addPort(22)
                              .build(),
                      Firewall.Rule.builder()
                              .IPProtocol(IPProtocol.TCP)
                              .addPort(23)
                              .build()))
              .addSourceRange("10.0.0.0/8")
              .addSourceTag("tag1")
              .addTargetTag("tag2");

      assertGlobalOperationDoneSucessfully(api().update(FIREWALL_NAME, firewall), TIME_WAIT);

   }

   @Test(groups = "live", dependsOnMethods = "testPatchFirewall")
   public void testGetFirewall() {

      FirewallOptions patchedFirewall = new FirewallOptions()
              .name(FIREWALL_NAME)
              .network(getNetworkUrl(userProject.get(), FIREWALL_NETWORK_NAME))
              .allowedRules(ImmutableSet.of(
                      Firewall.Rule.builder()
                              .IPProtocol(IPProtocol.TCP)
                              .addPort(22)
                              .build(),
                      Firewall.Rule.builder()
                              .IPProtocol(IPProtocol.TCP)
                              .addPort(23)
                              .build()))
              .addSourceRange("10.0.0.0/8")
              .addSourceTag("tag1")
              .addTargetTag("tag2");

      Firewall firewall = api().get(FIREWALL_NAME);
      assertNotNull(firewall);
      assertFirewallEquals(firewall, patchedFirewall);
   }

   @Test(groups = "live", dependsOnMethods = "testGetFirewall")
   public void testListFirewall() {

      PagedIterable<Firewall> firewalls = api().list(new ListOptions.Builder()
              .filter("name eq " + FIREWALL_NAME));

      List<Firewall> firewallsAsList = Lists.newArrayList(firewalls.concat());

      assertEquals(firewallsAsList.size(), 1);

   }

   @Test(groups = "live", dependsOnMethods = "testListFirewall")
   public void testDeleteFirewall() {

      assertGlobalOperationDoneSucessfully(api().delete(FIREWALL_NAME), TIME_WAIT);
      assertGlobalOperationDoneSucessfully(api.getNetworkApiForProject(userProject.get()).delete
              (FIREWALL_NETWORK_NAME), TIME_WAIT);
   }

   private void assertFirewallEquals(Firewall result, FirewallOptions expected) {
      assertEquals(result.getName(), expected.getName());
      assertEquals(getOnlyElement(result.getSourceRanges()), getOnlyElement(expected.getSourceRanges()));
      assertEquals(getOnlyElement(result.getSourceTags()), getOnlyElement(expected.getSourceTags()));
      assertEquals(getOnlyElement(result.getTargetTags()), getOnlyElement(expected.getTargetTags()));
      assertEquals(result.getAllowed(), expected.getAllowed());
   }

}
