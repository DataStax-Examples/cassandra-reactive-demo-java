/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.demo.common.controller;

import com.datastax.demo.common.model.Stock;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** A helper class that creates URIs for controllers dealing with {@link Stock} objects. */
@Component
public class StockUriHelper {

  private final Converter<Instant, String> instantStringConverter;

  private final Converter<ByteBuffer, String> byteBufferStringConverter;

  public StockUriHelper(
      Converter<Instant, String> instantStringConverter,
      Converter<ByteBuffer, String> byteBufferStringConverter) {
    this.instantStringConverter = instantStringConverter;
    this.byteBufferStringConverter = byteBufferStringConverter;
  }

  /**
   * Creates an URI pointing to the next page of a result set.
   *
   * @param nextPage The paging state to create an URI for.
   * @return An URI pointing to the next page of a result set.
   */
  @NonNull
  public URI buildNextPageUri(@NonNull ByteBuffer nextPage) {
    String encoded = byteBufferStringConverter.convert(nextPage);
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .replaceQueryParam("page", encoded)
        .build(true)
        .toUri();
  }

  /**
   * Creates an URI pointing to a specific stock value.
   *
   * @param stock The stock value to create an URI for.
   * @return An URI pointing to a specific stock value.
   */
  @NonNull
  public URI buildStockDetailsUri(@NonNull Stock stock) {
    String date = instantStringConverter.convert(stock.getDate());
    return ServletUriComponentsBuilder.fromCurrentRequestUri()
        .replacePath("/api/v1/stocks/{symbol}/{date}")
        .buildAndExpand(stock.getSymbol(), date)
        .toUri();
  }
}