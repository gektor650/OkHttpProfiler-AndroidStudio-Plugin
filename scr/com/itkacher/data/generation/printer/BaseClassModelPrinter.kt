package com.itkacher.data.generation.printer

import com.itkacher.data.generation.FieldModel

abstract class BaseClassModelPrinter {
    protected val builder = StringBuilder()

    protected open fun addImport() {
        builder.append(LINE_BREAK)
        builder.append(IMPORT_GSON)
        builder.append(IMPORT_LIST)
        builder.append(LINE_BREAK)
        builder.append(LINE_BREAK)
    }

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
        const val TODO_NULLABLE = "\t//TODO: CHECK THIS CLASS. IT WAS NULL"
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