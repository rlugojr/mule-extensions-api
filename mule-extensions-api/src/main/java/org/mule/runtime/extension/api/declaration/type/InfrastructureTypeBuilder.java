/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.declaration.type;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.builder.BooleanTypeBuilder;
import org.mule.metadata.api.builder.NumberTypeBuilder;
import org.mule.metadata.api.builder.ObjectFieldTypeBuilder;
import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.StringTypeBuilder;
import org.mule.metadata.api.builder.TypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.utils.ParsingContext;

/**
 * Base class for a component capable of creating a {@link MetadataType} to be used
 * in an infrastructure parameter
 *
 * @since 1.0
 */
abstract class InfrastructureTypeBuilder {

  protected ObjectTypeBuilder objectType(BaseTypeBuilder typeBuilder, Class<?> type, ParsingContext context) {
    final ObjectTypeBuilder objectType = typeBuilder.objectType().id(type.getName());
    context.addTypeBuilder(type, objectType);

    return objectType;
  }

  protected ObjectFieldTypeBuilder addEnumField(ObjectTypeBuilder objectType,
                                                BaseTypeBuilder typeBuilder,
                                                String name,
                                                String description,
                                                String defaultValue,
                                                String... values) {
    return addField(objectType, getEnumType(typeBuilder, defaultValue, values), name, description);
  }

  protected StringTypeBuilder getEnumType(BaseTypeBuilder typeBuilder, String defaultValue, String... values) {
    StringTypeBuilder enumType = typeBuilder.stringType().id(String.class.getName()).enumOf(values);
    if (defaultValue != null) {
      enumType.defaultValue(defaultValue);
    }

    return enumType;
  }

  protected ObjectFieldTypeBuilder addBooleanField(ObjectTypeBuilder objectType,
                                                   BaseTypeBuilder typeBuilder,
                                                   String name,
                                                   String description,
                                                   Boolean defaultValue) {
    BooleanTypeBuilder booleanType = typeBuilder.booleanType().id(Boolean.class.getName());
    if (defaultValue != null) {
      booleanType.defaultValue(defaultValue.toString());
    }

    return addField(objectType, booleanType, name, description);
  }

  protected ObjectFieldTypeBuilder addStringField(ObjectTypeBuilder objectType,
                                                  BaseTypeBuilder typeBuilder,
                                                  String name,
                                                  String description,
                                                  String defaultValue) {
    final StringTypeBuilder stringType = typeBuilder.stringType().id(String.class.getName());

    if (defaultValue != null) {
      stringType.defaultValue(String.valueOf(defaultValue));
    }

    return addField(objectType, stringType, name, description);
  }

  protected ObjectFieldTypeBuilder addIntField(ObjectTypeBuilder objectType,
                                               BaseTypeBuilder typeBuilder,
                                               String name,
                                               String description,
                                               Integer defaultValue) {
    final NumberTypeBuilder intType = typeBuilder.numberType()
        .integer()
        .id(Integer.class.getName());

    if (defaultValue != null) {
      intType.defaultValue(String.valueOf(defaultValue));
    }

    return addField(objectType, intType, name, description);
  }

  protected ObjectFieldTypeBuilder addLongField(ObjectTypeBuilder objectType,
                                                BaseTypeBuilder typeBuilder,
                                                String name,
                                                String description,
                                                Long defaultValue) {

    final NumberTypeBuilder longType = typeBuilder.numberType()
        .integer()
        .id(Long.class.getName());

    if (defaultValue != null) {
      longType.defaultValue(String.valueOf(defaultValue));
    }

    return addField(objectType, longType, name, description);
  }

  protected ObjectFieldTypeBuilder addField(ObjectTypeBuilder objectType, TypeBuilder typeBuilder, String name,
                                            String description) {
    return objectType.addField().key(name).description(description).required(false).value(typeBuilder.build());
  }
}
