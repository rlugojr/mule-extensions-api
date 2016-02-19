/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.api.introspection.impl;

import org.mule.extension.api.introspection.ConfigurationModel;
import org.mule.extension.api.introspection.ConnectionProviderModel;
import org.mule.extension.api.introspection.Described;
import org.mule.extension.api.introspection.EnrichableModel;
import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.IDataType;
import org.mule.extension.api.introspection.OperationModel;
import org.mule.extension.api.introspection.ParameterModel;
import org.mule.extension.api.introspection.SourceModel;
import org.mule.extension.api.introspection.property.APIModelProperty;
import org.mule.extension.api.introspection.property.ConnectionHandlingTypeModelProperty;
import org.mule.extension.api.introspection.property.PasswordModelProperty;
import org.mule.extension.api.introspection.property.StudioModelProperty;
import org.mule.extension.api.introspection.property.XmlModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to transform every element of the extension api into a default instance of it.
 *
 * @since 1.0
 */
public class DefaultExtensionModelUtils
{

    public static ExtensionModel toDefaultExtensionModel(ExtensionModel extensionModel)
    {
        DefaultExtensionModel extension = new DefaultExtensionModel();
        mapDescribedProperties(extensionModel, extension);
        mapModelProperties(extensionModel, extension);

        extension.setVendor(extensionModel.getVendor());
        extension.setVersion(extensionModel.getVersion());

        List<ConfigurationModel> configs = new ArrayList<>();
        for (ConfigurationModel config : extensionModel.getConfigurationModels())
        {
            configs.add(toDefaultConfigurationModel(config));
        }
        extension.setConfigurationModels(configs);

        List<OperationModel> operations = new ArrayList<>();
        for (OperationModel operation : extensionModel.getOperationModels())
        {
            operations.add(toDTOOperation(operation));
        }
        extension.setOperationModels(operations);

        List<SourceModel> sources = new ArrayList<>();
        for (SourceModel source : extensionModel.getSourceModels())
        {
            sources.add(toDefaultSourceModel(source));
        }
        extension.setSourceModels(sources);

        List<ConnectionProviderModel> connectionProviders = new ArrayList<>();
        for (ConnectionProviderModel connectionProviderModel : extensionModel.getConnectionProviders())
        {
            connectionProviders.add(toDefaultConnectionProviderModel(connectionProviderModel));
        }
        extension.setConnectionProviders(connectionProviders);

        return extension;
    }


    public static ConfigurationModel toDefaultConfigurationModel(ConfigurationModel configurationModel)
    {
        DefaultConfigurationModel config = new DefaultConfigurationModel();
        mapDescribedProperties(configurationModel, config);
        mapModelProperties(configurationModel, config);
        List<ParameterModel> params = new ArrayList<>();
        for (ParameterModel param : configurationModel.getParameterModels())
        {
            params.add(toDefaultParameterModel(param));
        }
        config.setParameterModels(params);
        return config;
    }

    public static ConnectionProviderModel toDefaultConnectionProviderModel(ConnectionProviderModel connectionProviderModel)
    {
        DefaultConnectionProviderModel connectionProviderModelDTO = new DefaultConnectionProviderModel();
        mapDescribedProperties(connectionProviderModel, connectionProviderModelDTO);
        mapModelProperties(connectionProviderModel, connectionProviderModelDTO);
        List<ParameterModel> params = new ArrayList<>();
        for (ParameterModel param : connectionProviderModel.getParameterModels())
        {
            params.add(toDefaultParameterModel(param));
        }
        connectionProviderModelDTO.setParameterModels(params);
        return connectionProviderModelDTO;
    }

    public static OperationModel toDTOOperation(OperationModel operationModel)
    {
        DefaultOperationModel operation = new DefaultOperationModel();
        mapDescribedProperties(operationModel, operation);
        mapModelProperties(operationModel, operation);
        operation.setReturnType(toDefaultDataType(operationModel.getReturnType()));
        List<ParameterModel> params = new ArrayList<>();
        for (ParameterModel param : operationModel.getParameterModels())
        {
            params.add(toDefaultParameterModel(param));
        }
        operation.setParameterModels(params);
        return operation;
    }

    public static IDataType toDefaultDataType(IDataType dataType)
    {
        DefaultDataType type = new DefaultDataType();
        type.setQualifier(dataType.getQualifier());
        type.setRawType(dataType.getRawType());
        type.setName(dataType.getName());
        if (dataType.getGenericTypes() != null)
        {
            IDataType[] generics = new DefaultDataType[dataType.getGenericTypes().length];
            for (int i = 0; i < dataType.getGenericTypes().length; i++)
            {
                generics[i] = toDefaultDataType(dataType.getGenericTypes()[i]);
            }
            type.setGenericTypes(generics);
        }
        return type;
    }

    public static SourceModel toDefaultSourceModel(SourceModel sourceModel)
    {
        DefaultSourceModel operation = new DefaultSourceModel();
        mapDescribedProperties(sourceModel, operation);
        mapModelProperties(sourceModel, operation);
        operation.setReturnType(sourceModel.getReturnType());
        List<ParameterModel> params = new ArrayList<>();
        for (ParameterModel param : sourceModel.getParameterModels())
        {
            params.add(toDefaultParameterModel(param));
        }
        operation.setParameterModels(params);
        operation.setAttributesType(toDefaultDataType(sourceModel.getAttributesType()));
        operation.setReturnType(toDefaultDataType(sourceModel.getReturnType()));
        return operation;
    }

    public static ParameterModel toDefaultParameterModel(ParameterModel param)
    {
        DefaultParameterModel parameter = new DefaultParameterModel();
        mapDescribedProperties(param, parameter);
        mapModelProperties(param, parameter);
        parameter.setDefaultValue(param.getDefaultValue());
        parameter.setExpressionSupport(param.getExpressionSupport());
        parameter.setRequired(param.isRequired());
        parameter.setType(toDefaultDataType(param.getType()));
        return parameter;
    }

    private static void mapDescribedProperties(Described origin, AbstractBaseDefaultExtensionModel destiny)
    {
        destiny.setName(origin.getName());
        destiny.setDescription(origin.getDescription());
    }

    private static void mapModelProperties(EnrichableModel origin, AbstractBaseDefaultExtensionModel destiny)
    {
        Map<String, Object> modelProperties = new HashMap<>();
        for (Map.Entry<String, Object> entry : origin.getModelProperties().entrySet())
        {
            if (entry.getValue() instanceof APIModelProperty)
            {
                switch (entry.getKey())
                {
                    case XmlModelProperty.KEY:
                    {
                        DefaultXmlModelProperty prop = new DefaultXmlModelProperty();
                        XmlModelProperty original = (XmlModelProperty) entry.getValue();
                        prop.setKey(original.getKey());
                        prop.setNamespace(original.getNamespace());
                        prop.setSchemaLocation(original.getSchemaLocation());
                        prop.setSchemaVersion(original.getSchemaVersion());
                        modelProperties.put(entry.getKey(), prop);
                    }
                    break;
                    case PasswordModelProperty.KEY:
                    {
                        DefaultPasswordModelProperty prop = new DefaultPasswordModelProperty();
                        PasswordModelProperty original = (PasswordModelProperty) entry.getValue();
                        prop.setKey(original.getKey());
                        modelProperties.put(entry.getKey(), prop);
                    }
                    break;
                    case StudioModelProperty.KEY:
                    {
                        DefaultStudioModelProperty prop = new DefaultStudioModelProperty();
                        StudioModelProperty original = (StudioModelProperty) entry.getValue();
                        prop.setKey(original.getKey());
                        prop.setDerived(original.isDerived());
                        prop.setEditorFileName(original.getEditorFileName());
                        modelProperties.put(entry.getKey(), prop);
                    }
                    break;
                    case ConnectionHandlingTypeModelProperty.KEY:
                    {
                        DefaultConnectionHandlingTypeModelProperty prop = new DefaultConnectionHandlingTypeModelProperty();
                        ConnectionHandlingTypeModelProperty original = (ConnectionHandlingTypeModelProperty) entry.getValue();
                        prop.setCached(original.isCached());
                        prop.setNone(original.isNone());
                        prop.setPooled(original.isPooled());
                        prop.setPoolingSupport(original.getPoolingSupport());
                        modelProperties.put(entry.getKey(), prop);
                    }
                    break;
                    default:
                        System.out.println(entry.getKey());
                }

            }
        }
        destiny.setModelProperties(modelProperties);
    }
}
