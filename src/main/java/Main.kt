/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package moshitest

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import okio.ByteString
import okio.ByteString.Companion.decodeBase64
import org.jetbrains.annotations.Nullable

class ByteStrings {
    @Throws(Exception::class)
    fun run() {
        //val json = "\"TW9zaGksIE9saXZlLCBXaGl0ZSBDaGluPw\""
        val json = "\"RWF0IFBpY2tsZXMsIFN1bW1lciwgU3ByaW5nLCBDb2xkIEZvb2Q=\""

        val moshi = Moshi.Builder()
            .add(ByteString::class.java, Base64ByteStringAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter(ByteString::class.java)

        val byteString = jsonAdapter.fromJson(json)
        println(byteString)
    }

    /**
     * Formats byte strings using [Base64](http://www.ietf.org/rfc/rfc2045.txt). No line
     * breaks or whitespace is included in the encoded form.
     */
    inner class Base64ByteStringAdapter : JsonAdapter<ByteString>() {


        @Throws(IOException::class)
        override fun fromJson(reader: JsonReader): ByteString? {
            val base64 = reader.nextString()
            return base64.decodeBase64()
        }

        @Throws(IOException::class)
        override fun toJson(writer: JsonWriter, value: ByteString?) {
            val string = value?.base64()
            writer.value(string)
        }


    }

    companion object {

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            ByteStrings().run()
        }
    }
}