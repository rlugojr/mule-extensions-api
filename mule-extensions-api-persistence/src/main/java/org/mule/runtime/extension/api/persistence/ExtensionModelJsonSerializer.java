/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.persistence;

import org.mule.runtime.extension.api.introspection.config.ConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.EnrichableModel;
import org.mule.runtime.extension.api.introspection.ExtensionModel;
import org.mule.runtime.extension.api.introspection.config.ImmutableConfigurationModel;
import org.mule.runtime.extension.api.introspection.connection.ImmutableConnectionProviderModel;
import org.mule.runtime.extension.api.introspection.ImmutableExtensionModel;
import org.mule.runtime.extension.api.introspection.operation.ImmutableOperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ImmutableParameterModel;
import org.mule.runtime.extension.api.introspection.ImmutableRuntimeExtensionModel;
import org.mule.runtime.extension.api.introspection.operation.ImmutableRuntimeOperationModel;
import org.mule.runtime.extension.api.introspection.source.ImmutableSourceModel;
import org.mule.runtime.extension.api.introspection.ModelProperty;
import org.mule.runtime.extension.api.introspection.operation.OperationModel;
import org.mule.runtime.extension.api.introspection.parameter.ParameterModel;
import org.mule.runtime.extension.api.introspection.source.SourceModel;
import org.mule.runtime.extension.api.introspection.property.DisplayModelProperty;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.persistence.MetadataTypeGsonTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Serializer that can convert a {@link ExtensionModel} into a readable and processable JSON representation and from a JSON
 * {@link String} to an {@link ExtensionModel} instance
 * <p>
 * <b>Considerations:</b>
 * <ul>
 * <li>All Runtime models from a extension, like: {@link ImmutableRuntimeExtensionModel} or {@link ImmutableRuntimeOperationModel},
 * will be serialized and deserialized into their non runtime version, like: {@link ImmutableExtensionModel}, {@link ImmutableOperationModel}</li>
 * <li>Only {@link ModelProperty}s that are considered as <b>externalizable</b>, the ones that {@link ModelProperty#isExternalizable()}
 * returns {@code true}, will be serialized</li>
 * <li>Due to the nature of {@link ModelProperty}, that can be dynamically attached to any {@link EnrichableModel}, only
 * the already know set of {@link ModelProperty} will be tagged with a friendly name, example: {@link DisplayModelProperty}
 * is going to be identified with the {@code display} name. Otherwise, the {@link ModelProperty} will be serialized
 * tagging it with the full qualifier name of the class.</li>
 * <li>When deserializing {@link ModelProperty}s, their full qualified name will be used, if the class is not found in the
 * ClassLoader the {@link ModelProperty} object will be discarded</li>
 * </ul>
 *
 * @since 1.0
 */
public class ExtensionModelJsonSerializer
{

    private final Gson gson;

    /**
     * Creates a new instance of the {@link ExtensionModelJsonSerializer}.
     * This serializer is capable of serializing and deserializing {@link ExtensionModel} from JSON ({@link #deserialize(String)}
     * and to JSON ( {@link #serialize(ExtensionModel)}
     */
    public ExtensionModelJsonSerializer()
    {
        this(false);
    }

    /**
     * Creates a new instance of the {@link ExtensionModelJsonSerializer}.
     *
     * @param prettyPrint boolean indicating if the serialization of the {@link ExtensionModel} should be printed in
     *                    a human readable or into compact and more performable format
     */
    public ExtensionModelJsonSerializer(boolean prettyPrint)
    {
        final DefaultImplementationTypeAdapterFactory configurationModelTypeAdapterFactory = new DefaultImplementationTypeAdapterFactory<>(ConfigurationModel.class, ImmutableConfigurationModel.class);
        final DefaultImplementationTypeAdapterFactory connectionProviderModelTypeAdapterFactory = new DefaultImplementationTypeAdapterFactory<>(ConnectionProviderModel.class, ImmutableConnectionProviderModel.class);
        final DefaultImplementationTypeAdapterFactory operationModelTypeAdapterFactory = new DefaultImplementationTypeAdapterFactory<>(OperationModel.class, ImmutableOperationModel.class);
        final DefaultImplementationTypeAdapterFactory sourceModelTypeAdapterFactory = new DefaultImplementationTypeAdapterFactory<>(SourceModel.class, ImmutableSourceModel.class);
        final DefaultImplementationTypeAdapterFactory parameterModelTypeAdapterFactory = new DefaultImplementationTypeAdapterFactory<>(ParameterModel.class, ImmutableParameterModel.class);

        final GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(MetadataType.class, new MetadataTypeGsonTypeAdapter())
                .registerTypeAdapterFactory(new ModelPropertyMapTypeAdapterFactory())
                .registerTypeAdapterFactory(sourceModelTypeAdapterFactory)
                .registerTypeAdapterFactory(parameterModelTypeAdapterFactory)
                .registerTypeAdapterFactory(configurationModelTypeAdapterFactory)
                .registerTypeAdapterFactory(connectionProviderModelTypeAdapterFactory)
                .registerTypeAdapterFactory(operationModelTypeAdapterFactory);

        if (prettyPrint)
        {
            gsonBuilder.setPrettyPrinting();
        }

        this.gson = gsonBuilder.create();
    }

    /**
     * Serializes an {@link ExtensionModel} into JSON
     *
     * @param extensionModel {@link ExtensionModel} to be serialized
     * @return {@link String} JSON representation of the {@link ExtensionModel}
     */
    public String serialize(ExtensionModel extensionModel)
    {
        return gson.toJson(extensionModel);
    }

    /**
     * @param extensionModelList List of {@link ExtensionModel} to be serialized
     * @return {@link String} JSON representation of the {@link List} of {@link ExtensionModel}
     */
    public String serializeList(List<ExtensionModel> extensionModelList)
    {
        return gson.toJson(extensionModelList);
    }

    /**
     * Deserializes a JSON representation of an {@link ExtensionModel}, to an actual instance of it.
     *
     * @param extensionModel serialized {@link ExtensionModel}
     * @return an instance of {@link ExtensionModel} based in the JSON
     */
    public ExtensionModel deserialize(String extensionModel)
    {
        return gson.fromJson(extensionModel, ImmutableExtensionModel.class);
    }

    /**
     * Deserializes a JSON representation of a {@link List} of {@link ExtensionModel}, to an actual instance of it.
     *
     * @param extensionModelList serialized {@link List} {@link ExtensionModel}
     * @return an instance of {@link ExtensionModel} based in the JSON
     */
    public List<ExtensionModel> deserializeList(String extensionModelList)
    {
        return gson.fromJson(extensionModelList, new TypeToken<List<ImmutableExtensionModel>>()
        {
        }.getType());
    }
}