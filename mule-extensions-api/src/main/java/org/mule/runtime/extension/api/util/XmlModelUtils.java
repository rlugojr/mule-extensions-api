/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.isMap;
import static org.mule.runtime.extension.api.util.NameUtils.defaultNamespace;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.api.model.ObjectType;
import org.mule.runtime.api.config.PoolingProfile;
import org.mule.runtime.api.meta.model.ExtensionModel;
import org.mule.runtime.api.meta.model.XmlDslModel;
import org.mule.runtime.extension.api.declaration.type.annotation.XmlHintsAnnotation;

import javax.xml.namespace.QName;
import java.util.Optional;

/**
 * Utils class for parsing and generation of Xml related values of an {@link ExtensionModel extension}.
 *
 * This is an internal utils class, not to be considered part of the API. Backwards compatibility not guaranteed
 *
 * @since 1.0
 */
public final class XmlModelUtils {

  private static final String XSD_EXTENSION = ".xsd";
  private static final String CURRENT_VERSION = "current";

  /**
   * Format mask for the default location of a schema
   */
  public static final String DEFAULT_SCHEMA_LOCATION_MASK = "http://www.mulesoft.org/schema/mule/%s";

  /**
   * Location of Mule's core schema
   */
  public static final String MULE_NAMESPACE_SCHEMA_LOCATION = String.format(DEFAULT_SCHEMA_LOCATION_MASK, "core");

  /**
   * Prefix for mule's core schema
   */
  public static final String MULE_PREFIX = "mule";

  /**
   * {@link QName} for an abstract redelivery policy
   */
  public static final QName MULE_ABSTRACT_REDELIVERY_POLICY_QNAME =
      new QName(MULE_NAMESPACE_SCHEMA_LOCATION, "abstract-redelivery-policy", MULE_PREFIX);

  /**
   * {@link QName} for a {@link PoolingProfile}
   */
  public static final QName MULE_POOLING_PROFILE_TYPE_QNAME =
      new QName(MULE_NAMESPACE_SCHEMA_LOCATION, "pooling-profile", MULE_PREFIX);

  /**
   * {@link QName} for a reconnection strategy
   */
  public static final QName MULE_ABSTRACT_RECONNECTION_STRATEGY_QNAME =
      new QName(MULE_NAMESPACE_SCHEMA_LOCATION, "abstract-reconnection-strategy", MULE_PREFIX);

  /**
   * {@link QName} for the streaming strategy
   */
  public static final QName MULE_ABSTRACT_STREAMING_STRATEGY_QNAME =
      new QName(MULE_NAMESPACE_SCHEMA_LOCATION, "abstract-streaming-strategy", MULE_PREFIX);

  /**
   * Takes a set of parameters extracted from the extension and generates a {@link XmlDslModel}.
   *
   * @param prefix prefix of the extension. If {@link Optional#empty()} or empty string, then it will default using the {@code extensionName}.
   * @param namespace namespace location of the extension. If {@link Optional#empty()} or empty string, then it will default using a generated namespace.
   * @param extensionName name of the extension, cannot be null.
   * @param extensionVersion version of the extension, cannot be null.
   * @return a wellformed {@link XmlDslModel}
   */
  public static XmlDslModel createXmlLanguageModel(Optional<String> prefix,
                                                   Optional<String> namespace, String extensionName,
                                                   String extensionVersion) {
    final String stringPrefix =
        isPresentAndNotBlank(prefix) ? prefix.get() : defaultNamespace(extensionName);
    final String stringNamespace =
        isPresentAndNotBlank(namespace) ? namespace.get() : buildDefaultLocation(stringPrefix);
    return getXmlDslModel(extensionVersion, stringPrefix, stringNamespace);
  }

  private static boolean isPresentAndNotBlank(Optional<String> element) {
    return element.isPresent() && isNotBlank(element.get());
  }

  private static XmlDslModel getXmlDslModel(String extensionVersion, String prefix, String namespace) {
    String xsdFileName = buildDefaultXsdFileName(prefix);
    String schemaLocation = buildDefaultSchemaLocation(namespace, xsdFileName);

    return XmlDslModel.builder()
        .setSchemaVersion(extensionVersion)
        .setPrefix(prefix)
        .setNamespace(namespace)
        .setSchemaLocation(schemaLocation)
        .setXsdFileName(xsdFileName)
        .build();
  }

  /**
   * @param metadataType a type
   * @return Whether the given {@code metadataType} can be defined as a top level element
   */
  public static boolean supportsTopLevelDeclaration(MetadataType metadataType) {
    if (metadataType instanceof ObjectType && !isMap(metadataType)) {
      return metadataType.getAnnotation(XmlHintsAnnotation.class)
          .map(XmlHintsAnnotation::allowsTopLevelDefinition)
          .orElse(false);
    }

    return false;
  }

  private static String buildDefaultLocation(String namespace) {
    return String.format(DEFAULT_SCHEMA_LOCATION_MASK, namespace);
  }

  private static String buildDefaultXsdFileName(String namespace) {
    return String.format("mule-%s%s", namespace, XSD_EXTENSION);
  }

  private static String buildDefaultSchemaLocation(String namespaceLocation, String xsdFileName) {
    return String.format("%s/%s/%s", namespaceLocation, CURRENT_VERSION, xsdFileName);
  }
}
