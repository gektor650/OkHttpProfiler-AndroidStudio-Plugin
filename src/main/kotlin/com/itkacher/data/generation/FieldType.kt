/**
 * Copyright 2018 LocaleBro.com [Ievgenii Tkachenko(gektor650@gmail.com)]
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
package com.itkacher.data.generation

enum class FieldType(val java: String,val kotlin: String,val isJavaPrimitive: Boolean = false,val javaWrapper: String) {
    INTEGER("int", "Int?", true, "Integer"),
    LONG("long", "Long?", true, "Long"),
    BOOLEAN("boolean", "Boolean?", true, "Boolean"),
    FLOAT("float", "Float?", true, "Float"),
    DOUBLE("double", "Double?", true, "Double"),
    OBJECT("Object", "Any?", false, "Object"),
    LIST("List", "List", false, "List"),
    STRING("String", "String?", false, "String"),
    UNDEFINED("?", "?", false, "?"),
}