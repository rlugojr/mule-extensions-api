/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import static java.util.Arrays.stream;
import static org.mule.metadata.api.builder.BaseTypeBuilder.create;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.extension.api.ExtensionConstants.DYNAMIC_CONFIG_POLICY_PARAMETER_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.EXPIRATION_POLICY_PARAMETER_NAME;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.extension.api.declaration.type.annotation.InfrastructureTypeAnnotation;
import org.mule.runtime.extension.api.declaration.type.annotation.TypeAliasAnnotation;

import java.util.concurrent.TimeUnit;

/**
 * Creates instances of {@link MetadataType} which represent a reconnection strategy
 *
 * @since 1.0
 */
public final class DynamicConfigPolicyTypeBuilder extends InfrastructureTypeBuilder {

  public static final String MAX_IDLE_TIME = "maxIdleTime";
  private static final String TIME_UNIT = "timeUnit";

  /**
   * @return a {@link MetadataType} representation of a dynamic config policy
   */
  public MetadataType buildDynamicConfigPolicyType() {
    BaseTypeBuilder typeBuilder = create(JAVA);
    ObjectTypeBuilder dynamicConfigPolicy = typeBuilder.objectType()
        .id(Object.class.getName())
        .with(new TypeAliasAnnotation(DYNAMIC_CONFIG_POLICY_PARAMETER_NAME))
        .with(new InfrastructureTypeAnnotation());
    addField(dynamicConfigPolicy, getExpirationPolicy(typeBuilder), "expiration-policy", EXPIRATION_POLICY_DESCRIPTION, false);
    return dynamicConfigPolicy.build();
  }

  private TypeBuilder getExpirationPolicy(BaseTypeBuilder typeBuilder) {
    ObjectTypeBuilder expirationPolicy = typeBuilder.objectType()
        .id(Object.class.getName())
        .with(new InfrastructureTypeAnnotation())
        .with(new TypeAliasAnnotation(EXPIRATION_POLICY_PARAMETER_NAME));

    addMaxIdleTime(expirationPolicy, typeBuilder);
    addTimeUnit(expirationPolicy, typeBuilder);
    return expirationPolicy;
  }

  private void addMaxIdleTime(ObjectTypeBuilder expirationPolicyType, BaseTypeBuilder typeBuilder) {
    addLongField(expirationPolicyType, typeBuilder, MAX_IDLE_TIME,
                 "A scalar time value for the maximum amount of time a dynamic configuration instance should be allowed\n" +
                     "to be idle before it's considered eligible for expiration",
                 null, true);
  }

  private void addTimeUnit(ObjectTypeBuilder expirationPolicyType, BaseTypeBuilder typeBuilder) {
    addEnumField(expirationPolicyType, typeBuilder, TIME_UNIT,
                 "A time unit that qualifies the maxIdleTime attribute",
                 null, true,
                 stream(TimeUnit.values()).map(Enum::name).toArray(String[]::new));
  }
}
