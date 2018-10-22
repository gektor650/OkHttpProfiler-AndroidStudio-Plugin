package com.itkacher.data

import com.fasterxml.jackson.databind.JsonNode
import com.itkacher.views.json.JsonMutableTreeNode
import java.lang.StringBuilder

class JavaGenerationModel(private val node: JsonMutableTreeNode) {




    fun generateClass() {
        val builder = StringBuilder()
        builder.append(IMPORT)
        builder.append(NEW_LINE)
        builder.append(NEW_LINE)
        builder.append(CLASS_NAME)
        builder.append(node.name.capitalize())
        builder.append(START_OF_CLASS)
        builder.append(NEW_LINE)
        builder.append(NEW_LINE)

        if (node.isLeaf) {

        } else if (node.value?.isObject == true) {
            val fields = node.value.fields()
            fields.forEach {
                builder.append(genVar(it))
            }
        }
        builder.append(END_OF_CLASS)

        println(builder.toString())
    }

    private fun genVar(entry: Map.Entry<String, JsonNode>): StringBuilder {
        val builder = StringBuilder()
        builder.append(
                TABULATION,
                NULLABLE_ANNOTATION, // "@Nullable"
                NEW_LINE,
                TABULATION,
                SERIALIZED_TAG_START, // @SerializedName('
                entry.key,
                SERIALIZED_TAG_END, // )
                NEW_LINE,
                TABULATION,
                CONST_VISIBILITY
        )
        when {
            entry.value.isTextual -> {
                builder.append(
                        CONST_TYPE_STRING,
                        entry.key.capitalize()
                )
            }
            entry.value.isBoolean -> {
                builder.append(
                        CONST_TYPE_BOOLEAN,
                        entry.key.capitalize()
                )
            }
            entry.value.isNull -> {
                builder.append(
                        CONST_TYPE_OBJECT,
                        entry.key.capitalize()
                )
            }
            entry.value.isLong -> {
                builder.append(
                        CONST_TYPE_LONG,
                        entry.key.capitalize()
                )
            }
            entry.value.isInt -> {
                builder.append(
                        CONST_TYPE_INTEGER,
                        entry.key.capitalize()
                )
            }
            entry.value.isDouble -> {
                builder.append(
                        CONST_TYPE_DOBLE,
                        entry.key.capitalize()
                )
            }
            entry.value.isArray -> {
                builder.append(
                        CONST_TYPE_ARRAY,
                        CONST_TYPE_ARRAY_TYPE_END,
                        entry.key.capitalize()
                )
            }
            else -> {

            }
        }
        builder.append(
                LINE_END,
                NEW_LINE,
                NEW_LINE
        )
        return builder
    }

    companion object {
        const val IMPORT = "import com.google.gson;\r\n"
        const val CLASS_NAME = "class "
        const val START_OF_CLASS = "{"
        const val END_OF_CLASS = "}"
        const val NEW_LINE = "\r\n"
        const val TABULATION = "\t"
        const val CONST_VISIBILITY = "private "
        const val CONST_TYPE_STRING = "String "
        const val CONST_TYPE_BOOLEAN= "bool "
        const val CONST_TYPE_INTEGER = "int "
        const val CONST_TYPE_LONG = "long "
        const val CONST_TYPE_OBJECT = "Object "
        const val CONST_TYPE_DOBLE = "double "
        const val CONST_TYPE_ARRAY = "List<"
        const val CONST_TYPE_ARRAY_TYPE_END = "> "
        const val NULLABLE_ANNOTATION = "@Nullable"
        const val LINE_END = ";"
        const val SERIALIZED_TAG_START = "@SerializedName(\""
        const val SERIALIZED_TAG_END = "\")"
    }
}