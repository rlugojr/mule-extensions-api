/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mule.runtime.api.metadata.MetadataKeyBuilder.newKey;
import static org.mule.runtime.api.metadata.resolving.FailureCode.CONNECTION_FAILURE;
import static org.mule.runtime.api.metadata.resolving.FailureCode.NOT_AUTHORIZED;
import static org.mule.runtime.api.metadata.resolving.MetadataFailure.Builder.newFailure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.failure;
import static org.mule.runtime.api.metadata.resolving.MetadataResult.success;
import org.mule.runtime.api.metadata.MetadataKey;
import org.mule.runtime.api.metadata.MetadataKeyBuilder;
import org.mule.runtime.api.metadata.MetadataKeysContainer;
import org.mule.runtime.api.metadata.MetadataKeysContainerBuilder;
import org.mule.runtime.api.metadata.descriptor.TypeMetadataDescriptor;
import org.mule.runtime.api.metadata.resolving.MetadataFailure;
import org.mule.runtime.api.metadata.resolving.MetadataResult;
import org.mule.runtime.api.metadata.resolving.NamedTypeResolver;
import org.mule.runtime.extension.api.persistence.metadata.EntityMetadataResultJsonSerializer;
import org.mule.runtime.extension.api.persistence.metadata.MetadataKeysResultJsonSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MetadataKeyResultPersistenceTestCase extends AbstractMetadataPersistenceTestCase {

  private static final String METADATA_KEYS_RESULT_JSON = "metadata/success-result-keys.json";
  private static final String METADATA_MULTILEVEL_KEYS_RESULT_JSON = "metadata/success-result-multilevel-keys.json";
  private static final String METADATA_KEYS_RESULT_FAILURE_JSON = "metadata/failure-keys-result.json";
  private static final String METADATA_ENTITY_RESULT_FAILURE_JSON = "metadata/failure-entity-result.json";

  private static final String FIRST_KEY_ID = "firstKey";
  private static final String SECOND_KEY_ID = "secondKey";
  private static final String METADATA_RESULT_ERROR_MESSAGE = "Metadata Failure Error";
  private static final String FIRST_CHILD = "firstChild";
  private static final String SECOND_CHILD = "secondChild";

  private MetadataKeysResultJsonSerializer keysResultSerializer = new MetadataKeysResultJsonSerializer(true);
  private final EntityMetadataResultJsonSerializer typeDescriptorResultJsonSerializer =
      new EntityMetadataResultJsonSerializer(true);
  private MetadataKeysContainerBuilder builder = MetadataKeysContainerBuilder.getInstance();
  protected final MetadataKey key = MetadataKeyBuilder.newKey("Key ID").withDisplayName("Key Name").build();
  protected final NamedTypeResolver resolver = mock(NamedTypeResolver.class);

  @Before
  public void setup() {
    super.setUp();
    builder = MetadataKeysContainerBuilder.getInstance();
  }

  @Test
  public void serializeSuccessMetadataKeysResult() throws IOException {
    Set<MetadataKey> keys = new LinkedHashSet<>();
    keys.add(newKey(FIRST_KEY_ID).build());
    keys.add(newKey(SECOND_KEY_ID).build());
    MetadataResult<MetadataKeysContainer> successResult = success(builder.add(CATEGORY_NAME, keys).build());
    String serialized = keysResultSerializer.serialize(successResult);
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_JSON);
  }

  @Test
  public void serializeNullPayloadMetadataKeysResult() throws IOException {
    MetadataResult<Object> failure = failure(newFailure()
        .withMessage(METADATA_RESULT_ERROR_MESSAGE)
        .withFailureCode(NOT_AUTHORIZED)
        .withReason(METADATA_RESULT_ERROR_MESSAGE)
        .onKeys());
    String serialized = keysResultSerializer.serialize(failure);
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeSuccessMultilevelMetadataKeyResult() throws IOException {
    Set<MetadataKey> keys = new LinkedHashSet<>();
    keys.add(newKey(FIRST_KEY_ID).withChild(newKey(FIRST_CHILD)).withChild(newKey(SECOND_CHILD)).build());
    keys.add(newKey(SECOND_KEY_ID).build());
    String serialized = keysResultSerializer.serialize(success(builder.add(CATEGORY_NAME, keys).build()));
    assertSerializedJson(serialized, METADATA_MULTILEVEL_KEYS_RESULT_JSON);
  }

  @Test
  public void serializeFailureMultilevelMetadataKeyResult() throws IOException {
    MetadataResult<MetadataKeysContainer> failureResult = failure(builder.build(), newFailure()
        .withMessage(METADATA_RESULT_ERROR_MESSAGE)
        .withFailureCode(NOT_AUTHORIZED)
        .withReason(METADATA_RESULT_ERROR_MESSAGE)
        .onKeys());
    String serialized = keysResultSerializer.serialize(failureResult);
    assertSerializedJson(serialized, METADATA_KEYS_RESULT_FAILURE_JSON);
  }

  @Test
  public void serializeFailureEntityMetadataResult() throws IOException {
    MetadataResult<TypeMetadataDescriptor> failureResult = failure(newFailure()
        .withMessage(METADATA_RESULT_ERROR_MESSAGE)
        .withFailureCode(CONNECTION_FAILURE)
        .withReason(METADATA_RESULT_ERROR_MESSAGE)
        .onComponent());
    String serialized = typeDescriptorResultJsonSerializer.serialize(failureResult);
    assertSerializedJson(serialized, METADATA_ENTITY_RESULT_FAILURE_JSON);
  }

  @Test
  public void deserializeMetadataKeysResult() throws IOException {
    String resource = getResourceAsString(METADATA_KEYS_RESULT_JSON);
    MetadataResult<MetadataKeysContainer> metadataResult = keysResultSerializer.deserialize(resource);

    assertThat(metadataResult.isSuccess(), is(true));
    MetadataKeysContainer container = metadataResult.get();
    assertThat(container.getKeys(CATEGORY_NAME).isPresent(), is(true));
    Iterator<MetadataKey> iterator = container.getKeys(CATEGORY_NAME).get().iterator();
    assertThat(iterator.next().getDisplayName(), is(FIRST_KEY_ID));
    assertThat(iterator.next().getDisplayName(), is(SECOND_KEY_ID));
  }

  @Test
  public void deserializeFailureKeysResult() throws IOException {
    String resource = getResourceAsString(METADATA_KEYS_RESULT_FAILURE_JSON);
    MetadataResult<MetadataKeysContainer> metadataResult = keysResultSerializer.deserialize(resource);
    assertThat(metadataResult.isSuccess(), is(false));
    assertThat(metadataResult.getFailures().isEmpty(), is(false));

    MetadataFailure metadataFailure = metadataResult.getFailures().get(0);
    assertThat(metadataFailure.getReason(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailure.getMessage(), is(METADATA_RESULT_ERROR_MESSAGE));
    assertThat(metadataFailure.getFailureCode().getName(), is(NOT_AUTHORIZED.getName()));
  }
}
