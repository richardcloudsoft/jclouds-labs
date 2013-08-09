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

package org.jclouds.googlecomputeengine.domain;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.Date;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

/**
 * Represents a disk image to use on an instance.
 *
 * @author David Alves
 * @see <a href="https://developers.google.com/compute/docs/reference/v1beta15/images"/>
 */
@Beta
public final class Image extends Resource {

   private final String sourceType;
   private final Optional<URI> preferredKernel;
   private final RawDisk rawDisk;
   private final Optional<Deprecated> deprecated;

   @ConstructorProperties({
           "id", "creationTimestamp", "selfLink", "name", "description", "sourceType", "preferredKernel",
           "rawDisk", "deprecated"
   })
   protected Image(String id, Date creationTimestamp, URI selfLink, String name, String description,
                   String sourceType, URI preferredKernel, RawDisk rawDisk, Deprecated deprecated) {
      super(Kind.IMAGE, id, creationTimestamp, selfLink, name, description);
      this.sourceType = checkNotNull(sourceType, "sourceType of %s", name);
      this.preferredKernel = fromNullable(preferredKernel);
      this.rawDisk = checkNotNull(rawDisk, "rawDisk of %s", name);
      this.deprecated = fromNullable(deprecated);
   }

   /**
    * @return must be RAW; provided by the client when the disk image is created.
    */
   public String getSourceType() {
      return sourceType;
   }

   /**
    * @return An optional URL of the preferred kernel for use with this disk image.
    */
   public Optional<URI> getPreferredKernel() {
      return preferredKernel;
   }

   /**
    * @return the raw disk image parameters.
    */
   public RawDisk getRawDisk() {
      return rawDisk;
   }

   /**
    * @return the deprecation information for this image
    */
   public Optional<Deprecated> getDeprecated() {
      return deprecated;
   }

   /**
    * {@inheritDoc}
    */
   protected Objects.ToStringHelper string() {
      return super.string()
              .omitNullValues()
              .add("sourceType", sourceType)
              .add("preferredKernel", preferredKernel.orNull())
              .add("rawDisk", rawDisk)
              .add("deprecated", deprecated.orNull());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return string().toString();
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return new Builder().fromImage(this);
   }

   public static final class Builder extends Resource.Builder<Builder> {

      private String sourceType;
      private URI preferredKernel;
      private RawDisk rawDisk;
      private Deprecated deprecated;

      /**
       * @see Image#getSourceType()
       */
      public Builder sourceType(String sourceType) {
         this.sourceType = checkNotNull(sourceType, "sourceType");
         return this;
      }

      /**
       * @see Image#getPreferredKernel()
       */
      public Builder preferredKernel(URI preferredKernel) {
         this.preferredKernel = preferredKernel;
         return this;
      }

      /**
       * @see Image#getDeprecated()
       */
      public Builder deprecated(Deprecated deprecated) {
         this.deprecated = checkNotNull(deprecated, "deprecated");
         return this;
      }

      /**
       * @see Image#getRawDisk()
       */
      public Builder rawDisk(RawDisk rawDisk) {
         this.rawDisk = checkNotNull(rawDisk);
         return this;
      }

      @Override
      protected Builder self() {
         return this;
      }

      public Image build() {
         return new Image(super.id, super.creationTimestamp, super.selfLink, super.name,
                 super.description, sourceType, preferredKernel, rawDisk, deprecated);
      }

      public Builder fromImage(Image in) {
         return super.fromResource(in)
                 .sourceType(in.getSourceType())
                 .preferredKernel(in.getPreferredKernel().orNull())
                 .rawDisk(in.getRawDisk())
                 .deprecated(in.getDeprecated().orNull());
      }

   }

   /**
    * Deprecation information for an image
    */
   public static class Deprecated {
      private final Optional<String> state;
      private final Optional<URI> replacement;
      private final Optional<String> deprecated;
      private final Optional<String> obsolete;
      private final Optional<String> deleted;

      @ConstructorProperties({"state", "replacement", "deprecated", "obsolete", "deleted"})
      public Deprecated(String state, URI replacement, String deprecated, String obsolete,
                        String deleted) {
         this.state = fromNullable(state);
         this.replacement = fromNullable(replacement);
         this.deprecated = fromNullable(deprecated);
         this.obsolete = fromNullable(obsolete);
         this.deleted = fromNullable(deleted);
      }

      /**
       * @return The deprecation state of this image.
       */
      public Optional<String> getState() {
         return state;
      }

      /**
       * @return A fully-qualified URL of the suggested replacement for the deprecated image.
       */
      public Optional<URI> getReplacement() {
         return replacement;
      }

      /**
       * @return An optional RFC3339 timestamp for when the deprecation state of this resource will be changed to DEPRECATED.
       */
      public Optional<String> getDeprecated() {
         return deprecated;
      }

      /**
       * @return An optional RFC3339 timestamp on or after which the deprecation state of this resource will be changed toOBSOLETE.
       */
      public Optional<String> getObsolete() {
         return obsolete;
      }

      /**
       * @return An optional RFC3339 timestamp on or after which the deprecation state of this resource will be changed to DELETED.
       */
      public Optional<String> getDeleted() {
         return deleted;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         return Objects.hashCode(state, replacement, deprecated, obsolete, deleted);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null || getClass() != obj.getClass()) return false;
         Deprecated that = Deprecated.class.cast(obj);
         return equal(this.state, that.state)
                 && equal(this.replacement, that.replacement)
                 && equal(this.deprecated, that.deprecated)
                 && equal(this.obsolete, that.obsolete)
                 && equal(this.deleted, that.deleted);
      }

      /**
       * {@inheritDoc}
       */
      protected Objects.ToStringHelper string() {
         return toStringHelper(this)
                 .omitNullValues()
                 .add("state", state.orNull())
                 .add("replacement", replacement.orNull())
                 .add("deprecated", deprecated.orNull())
                 .add("obsolete", obsolete.orNull())
                 .add("deleted", deleted.orNull());
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public Builder toBuilder() {
         return builder().fromDeprecated(this);
      }

      public static class Builder {
         private String state;
         private URI replacement;
         private String deprecated;
         private String obsolete;
         private String deleted;

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.Deprecated#getState()
          */
         public Builder state(String state) {
            this.state = state;
            return this;
         }

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.Deprecated#getReplacement()
          */
         public Builder replacement(URI replacement) {
            this.replacement = replacement;
            return this;
         }

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.Deprecated#getDeprecated()
          */
         public Builder deprecated(String deprecated) {
            this.deprecated = deprecated;
            return this;
         }

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.Deprecated#getObsolete()
          */
         public Builder obsolete(String obsolete) {
            this.obsolete = obsolete;
            return this;
         }

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.Deprecated#getDeprecated()
          */
         public Builder deleted(String deleted) {
            this.deleted = deleted;
            return this;
         }

         public Deprecated build() {
            return new Deprecated(state, replacement, deprecated, obsolete, deleted);
         }

         public Builder fromDeprecated(Deprecated in) {
            return new Builder().state(in.getState().orNull())
                    .replacement(in.getReplacement().orNull())
                    .deprecated(in.getDeprecated().orNull())
                    .obsolete(in.getObsolete().orNull())
                    .deleted(in.getDeleted().orNull());
          }
      }
   }

   /**
    * A raw disk image, usually the base for an image.
    *
    * @author David Alves
    * @see <a href="https://developers.google.com/compute/docs/reference/v1beta15/images"/>
    */
   public static class RawDisk {

      private final String source;
      private final String containerType;
      private final Optional<String> sha1Checksum;

      @ConstructorProperties({
              "source", "containerType", "sha1Checksum"
      })
      private RawDisk(String source, String containerType, String sha1Checksum) {
         this.source = checkNotNull(source, "source");
         this.containerType = checkNotNull(containerType, "containerType");
         this.sha1Checksum = fromNullable(sha1Checksum);
      }

      /**
       * @return the full Google Cloud Storage URL where the disk image is stored; provided by the client when the disk
       *         image is created.
       */
      public String getSource() {
         return source;
      }

      /**
       * @return the format used to encode and transmit the block device.
       */
      public String getContainerType() {
         return containerType;
      }

      /**
       * @return an optional SHA1 checksum of the disk image before unpackaging; provided by the client when the disk
       *         image is created.
       */
      public Optional<String> getSha1Checksum() {
         return sha1Checksum;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         return Objects.hashCode(source, containerType, sha1Checksum);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals(Object obj) {
         if (this == obj) return true;
         if (obj == null || getClass() != obj.getClass()) return false;
         RawDisk that = RawDisk.class.cast(obj);
         return equal(this.source, that.source)
                 && equal(this.containerType, that.containerType)
                 && equal(this.sha1Checksum, that.sha1Checksum);
      }

      /**
       * {@inheritDoc}
       */
      protected Objects.ToStringHelper string() {
         return toStringHelper(this)
                 .omitNullValues()
                 .add("source", source)
                 .add("containerType", containerType)
                 .add("sha1Checksum", sha1Checksum.orNull());
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String toString() {
         return string().toString();
      }

      public static Builder builder() {
         return new Builder();
      }

      public Builder toBuilder() {
         return builder().fromImageRawDisk(this);
      }

      public static class Builder {

         private String source;
         private String containerType;
         private String sha1Checksum;

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.RawDisk#getSource()
          */
         public Builder source(String source) {
            this.source = checkNotNull(source);
            return this;
         }

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.RawDisk#getContainerType()
          */
         public Builder containerType(String containerType) {
            this.containerType = checkNotNull(containerType);
            return this;
         }

         /**
          * @see org.jclouds.googlecomputeengine.domain.Image.RawDisk#getSha1Checksum()
          */
         public Builder sha1Checksum(String sha1Checksum) {
            this.sha1Checksum = sha1Checksum;
            return this;
         }

         public RawDisk build() {
            return new RawDisk(source, containerType, sha1Checksum);
         }

         public Builder fromImageRawDisk(RawDisk rawDisk) {
            return new Builder().source(rawDisk.getSource())
                    .containerType(rawDisk.getContainerType())
                    .sha1Checksum(rawDisk.getSha1Checksum().orNull());
         }
      }
   }
}
