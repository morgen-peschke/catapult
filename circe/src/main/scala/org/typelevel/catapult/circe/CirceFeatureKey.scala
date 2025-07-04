/*
 * Copyright 2022 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.typelevel.catapult.circe

import io.circe.{Decoder, Encoder, Json}
import org.typelevel.catapult.FeatureKey
import org.typelevel.catapult.circe.JsonLDCodec.circeLDCodecForJSON
import org.typelevel.catapult.codec.LDCodec
import org.typelevel.catapult.codec.LDCodec.LDCodecResult

object CirceFeatureKey {

  /** Define a feature key that is expected to return a JSON value.
    *
    * This uses `circe` encoding for JSON and will fail if the default value
    * cannot be represented by LaunchDarkly's `LDValue`
    *
    * @param key
    *   the key of the flag
    * @param default
    *   a value to return if the retrieval fails or the type is not expected
    */
  def featureKey(
      key: String,
      default: Json,
  ): LDCodecResult[FeatureKey.Aux[Json]] =
    FeatureKey.instanceOrFailure(key, default)

  /** Define a feature key that is expected to return a JSON value.
    *
    * This uses `circe` encoding for JSON and will fail if the default value
    * cannot be represented by LaunchDarkly's `LDValue`
    *
    * @param key
    *   the key of the flag
    * @param default
    *   a value to return if the retrieval fails or the type is not expected
    */
  def featureKeyEncoded[A: Encoder: Decoder](
      key: String,
      default: A,
  ): LDCodecResult[FeatureKey.Aux[A]] = {
    implicit val ldCodec: LDCodec[A] = JsonLDCodec.ldCodecFromCirceCodec[A]
    FeatureKey.instanceOrFailure(key, default)
  }
}
