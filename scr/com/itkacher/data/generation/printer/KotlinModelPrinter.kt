package com.itkacher.data.generation.printer

import com.itkacher.data.generation.ClassModel
import com.itkacher.data.generation.FieldModel

class KotlinModelPrinter(private val classModels: List<ClassModel>) : BaseClassModelPrinter() {

    override fun addField(field: FieldModel) {
        addSerializationAnnotation(field.fieldOriginName)
        builder.append(
                TABULATION,
                VAR_TYPE
        )
        builder.append(
                field.fieldName,
                VAR_TYPE_DECLARATION
        )

        if (field.fieldTypeObjectName != null) {
            builder.append(field.fieldTypeObjectName)
        } else {
            builder.append(field.fieldType.kotlin)
        }
    }

    override fun buildString(): StringBuilder {
        addImport()
        classModels.forEach { classModel ->
            if(classModel.fields.isEmpty()) {
                builder.append(CLASS_NAME)
                builder.append(classModel.name)
                builder.append(LINE_BREAK)
                builder.append(TODO_NULLABLE)
                builder.append(LINE_BREAK)
                builder.append(LINE_BREAK)
            } else {
                builder.append(DATA_CLASS)
                builder.append(CLASS_NAME)
                builder.append(classModel.name)
                builder.append(ARG_START)
                builder.append(LINE_BREAK)
                builder.append(LINE_BREAK)
                classModel.fields.forEachIndexed { index, field ->
                    addField(field)
                    if (index != classModel.fields.size - 1) {
                        builder.append(ARG_DELIMITER)
                    }
                    builder.append(LINE_BREAK)
                    builder.append(LINE_BREAK)
                }
                builder.append(LINE_BREAK)
                builder.append(ARG_END)
                builder.append(LINE_BREAK)
                builder.append(LINE_BREAK)
            }
        }
        return builder
    }

    companion object {

        const val DATA_CLASS = "data "
        const val VAR_TYPE = "val "
        const val VAR_TYPE_DECLARATION = ": "
        const val ARG_START = '('
        const val ARG_END = ')'
        const val ARG_DELIMITER = ','
    }

}