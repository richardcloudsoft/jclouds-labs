package org.jclouds.googlecomputeengine.compute.functions;

import com.google.common.base.Predicate;
import org.jclouds.compute.functions.GroupNamingConvention;

import javax.inject.Inject;

public class FirewallTagNamingConvention {

   public static class Factory {

      private final GroupNamingConvention.Factory namingConvention;

      @Inject
      public Factory(GroupNamingConvention.Factory namingConvention) {
         this.namingConvention = namingConvention;
      }

      public FirewallTagNamingConvention get(String groupName) {
         return new FirewallTagNamingConvention(namingConvention.create().sharedNameForGroup(groupName));
      }
   }

   private final String sharedResourceName;

   public FirewallTagNamingConvention(String sharedResourceName) {
      this.sharedResourceName = sharedResourceName;
   }

   public String name(int port) {
      return String.format("%s-port-%s", sharedResourceName, port);
   }

   public Predicate<? super String> isFirewallTag() {
      return new Predicate<String>() {
         @Override
         public boolean apply(String input) {
            return input != null && input.startsWith(sharedResourceName + "-port-");
         }
      };
   }

}
