/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getLocalPart;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;
import static org.mule.runtime.extension.api.util.NameUtils.getAliasName;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectFieldType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.declaration.type.annotation.FlattenedTypeAnnotation;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Set of utility operations to handle {@link MetadataType}
 *
 * @since 1.0
 */
public final class ExtensionMetadataTypeUtils {

  private ExtensionMetadataTypeUtils() {}

  /**
   * @param fieldType the {@link ObjectFieldType} to inspect to retrieve its type Alias
   * @return the {@code Alias} name of the {@link ObjectFieldType}
   */
  public static String getAlias(ObjectFieldType fieldType) {
    return fieldType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElse(getLocalPart(fieldType));
  }

  /**
   * @param metadataType the {@link MetadataType} to inspect to retrieve its type Alias
   * @return the {@code Alias} name of the {@link MetadataType}
   */
  public static String getAlias(MetadataType metadataType) {
    return metadataType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElse(metadataType.getMetadataFormat().equals(JAVA) ? getAliasName(getType(metadataType)) : "");
  }

  /**
   * @param metadataType the {@link MetadataType} to inspect to retrieve its type Alias
   * @param defaultName default name to use if {@code metadataType} alias is not defined
   * @return the {@code Alias} name of the {@link MetadataType} or the {@code defaultName} if alias was not specified
   */
  public static String getAlias(MetadataType metadataType, String defaultName) {
    return metadataType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElse(metadataType.getMetadataFormat().equals(JAVA)
            ? getAliasName(defaultName, getType(metadataType).getAnnotation(Alias.class))
            : defaultName);
  }

  public static boolean isFinal(MetadataType metadataType) {
    try {
      return metadataType.getAnnotation(ClassInformationAnnotation.class).map(ClassInformationAnnotation::isFinal)
          .orElse(metadataType.getMetadataFormat().equals(JAVA) && Modifier.isFinal(getType(metadataType).getModifiers()));
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * @param metadataType the {@link MetadataType} to inspect
   * @return whether the {@code metadataType} represents a {@link Map} or not
   */
  public static boolean isMap(MetadataType metadataType) {

    if (metadataType.getAnnotation(TypeIdAnnotation.class).isPresent()) {
      if (Map.class.getName().equals(metadataType.getAnnotation(TypeIdAnnotation.class).get().getValue())) {
        return true;
      }
    }

    return metadataType.getAnnotation(ClassInformationAnnotation.class)
        .map(classInformationAnnotation -> classInformationAnnotation.getImplementedInterfaces().contains(Map.class.getName()))
        .orElse(false);
  }

  public static String getId(MetadataType metadataType) {
    try {
      return getTypeId(metadataType)
          .orElse(metadataType.getMetadataFormat().equals(JAVA) ? getType(metadataType).getName() : "");
    } catch (Exception e) {
      return "";
    }
  }

  /**
   * @return {@code true} if the type is marked as a {@link FlattenedTypeAnnotation FlattenedType}
   */
  public static boolean isParameterGroup(MetadataType type) {
    return type.getAnnotation(FlattenedTypeAnnotation.class).isPresent();
  }
}
