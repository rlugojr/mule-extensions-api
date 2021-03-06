/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.runtime.operation;

import static java.util.Optional.ofNullable;
import org.mule.runtime.api.message.Attributes;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.metadata.MediaType;

import java.util.Optional;

/**
 * Represents the result of an operation execution. Extensions can use this class
 * for cases in which the operation not only needs to return a value to be set
 * on the message payload but also wants to specify message attributes and/or
 * {@link MediaType}.
 * <p>
 * The {@link #getOutput()} value is always taken at face value, meaning that
 * if it's {@code null}, then the value that the operation returns to the runtime
 * will in fact be {@code null}. However, if the {@link #getAttributes()} or
 * {@link #getMediaType()} are {@link Optional#empty}, then the runtime will interpret
 * that as the operation not interested in setting those values, keeping the input message's
 * attributes and/or media type untouched.
 *
 * @param <T> the generic type of the output value
 * @param <A> the generic type of the message attributes
 * @since 1.0
 */
public class Result<T, A extends Attributes> {

  /**
   * Builds instances of {@link Result}
   *
   * @param <T> the generic type of the output value
   * @param <A> the generic type of the message attributes
   */
  public static final class Builder<T, A extends Attributes> {

    private final Result<T, A> product = new Result<>();

    private Builder() {}

    /**
     * Sets the output value
     *
     * @param output the new output value
     * @return {@code this} builder
     */
    public Builder<T, A> output(T output) {
      product.output = output;
      return this;
    }

    /**
     * Sets the output attributes value
     *
     * @param attributes the new attributes value
     * @return {@code this} builder
     */
    public Builder<T, A> attributes(A attributes) {
      product.attributes = attributes;
      return this;
    }

    /**
     * Sets the output {@link MediaType}
     *
     * @param mediaType the new {@link MediaType}
     * @return {@code this} builder
     */
    public Builder<T, A> mediaType(MediaType mediaType) {
      product.mediaType = mediaType;
      return this;
    }

    /**
     * @return the build {@link Result}
     */
    public Result<T, A> build() {
      return product;
    }
  }

  /**
   * Creates a new {@link Builder}
   *
   * @param <T> the generic type of the output value
   * @param <A> the generic type of the message attributes
   * @return a new {@link Builder}
   */
  public static <T, A extends Attributes> Builder<T, A> builder() {
    return new Builder<>();
  }

  /**
   * Creates a new {@link Builder} initialises with a state that matched
   * the one of the given {@code muleMessage}
   *
   * @param muleMessage a reference {@link Message}
   * @param <T>         the generic type of the output value
   * @param <A>         the generic type of the message attributes
   * @return a new {@link Builder}
   */
  public static <T, A extends Attributes> Builder<T, A> builder(Message muleMessage) {
    return new Builder<T, A>()
        .output((T) muleMessage.getPayload().getValue())
        .attributes((A) muleMessage.getAttributes())
        .mediaType(muleMessage.getPayload().getDataType().getMediaType());
  }

  /**
   * Creates a new {@link Builder} initialises with a state that matched
   * the one of the given {@code prototypeResult}
   *
   * @param prototypeResult a prototype {@link Result}
   * @param <T>             the generic type of the output value
   * @param <A>             the generic type of the message attributes
   * @return a new {@link Builder}
   */
  public static <T, A extends Attributes> Builder<T, A> builder(Result<T, A> prototypeResult) {
    return new Builder<T, A>()
        .output(prototypeResult.getOutput())
        .attributes(prototypeResult.getAttributes().orElse(null))
        .mediaType(prototypeResult.getMediaType().orElse(null));
  }

  private T output;
  private A attributes = null;
  private MediaType mediaType = null;

  private Result() {}

  /**
   * @return The operation's output
   */
  public T getOutput() {
    return output;
  }

  /**
   * The new value that the operation wants to set on {@link Message#getAttributes()}.
   * <p>
   * The operation might not be interested in changing that value, in which case
   * this method would return {@link Optional#empty()}
   *
   * @return an {@link Optional} {@code Attributes} value
   */
  public Optional<A> getAttributes() {
    return ofNullable(attributes);
  }

  /**
   * The new {@link MediaType} that the operation wants to set on {@link Message}.
   * <p>
   * The operation might not be interested in changing that value, in which case
   * this method would return {@link Optional#empty()}
   *
   * @return an {@link Optional} {@link MediaType} value
   */
  public Optional<MediaType> getMediaType() {
    return ofNullable(mediaType);
  }
}
