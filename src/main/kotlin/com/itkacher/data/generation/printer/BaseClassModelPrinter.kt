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
package com.itkacher.data.generation.printer

import com.itkacher.data.generation.FieldModel
import com.itkacher.data.generation.FieldType

abstract class BaseClassModelPrinter {
    protected val builder = StringBuilder()

    protected open fun addImport() {
        builder.append(IMPORT_GSON)
        builder.append(LINE_BREAK)
    }

    protected fun addListDeclaration(field: FieldModel) {
        val nesting = field.nestingLevel?.get() ?: 1
        for (i in 0 until nesting) {
            builder.append(FieldType.LIST.java)
            builder.append(GENERIC_START)
        }
        if (field.typeObjectName != null) {
            builder.append(field.typeObjectName)
        } else {
            builder.append(getListType(field))
        }
        for (i in 0 until nesting) {
            builder.append(GENERIC_END)
        }
    }

    abstract fun getListType(field: FieldModel): String

    protected open fun addSerializationAnnotation(name: String) {
        builder.append(
                TABULATION,
                SERIALIZED_TAG_START, // @SerializedName('
                name,
                SERIALIZED_TAG_END, // )
                LINE_BREAK
        )
    }

    abstract fun build(): StringBuilder
    protected abstract fun addField(field: FieldModel)

    companion object {
        const val IMPORT_GSON = "import com.google.gson.annotations.SerializedName;\r\n"
        const val IMPORT_LIST = "import java.util.List;\r\n"
        const val TODO_NULLABLE = "\t//TODO: CHECK THIS CLASS. IT WAS NULL\r\n"
        const val TODO = "\t//TODO: "
        const val CLASS_NAME = "class "
        const val START_OF_CLASS = " {"
        const val END_OF_CLASS = "}"
        const val LINE_BREAK = "\r\n"
        const val TABULATION = "\t"
        const val LINE_END = ";"
        const val OBJECT_NAME_DEFAULT = "object"
        const val SERIALIZED_TAG_START = "@SerializedName(\""
        const val SERIALIZED_TAG_END = "\")"
        const val UNDERLINE_CHAR = '_'
        const val GENERIC_START = '<'
        const val GENERIC_END = '>'
    }
}