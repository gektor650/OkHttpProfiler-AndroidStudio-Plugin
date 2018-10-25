package com.itkacher.data.generation.printer

import com.itkacher.data.generation.ClassModel
import com.itkacher.data.generation.FieldModel
import com.itkacher.data.generation.FieldType

class KotlinModelPrinter(private val classModels: List<ClassModel>) : BaseClassModelPrinter() {

    override fun addField(field: FieldModel) {
        addSerializationAnnotation(field.originName)
        builder.append(
                TABULATION,
                VAL_CONST
        )
        builder.append(
                field.name,
                VAL_DELIMITER
        )
        when {
            field.type == FieldType.LIST -> {
                builder.append(FieldType.LIST.kotlin)
                builder.append(GENERIC_START)
                builder.append(field.typeObjectName)
                builder.append(GENERIC_END)
            }
            field.typeObjectName != null -> builder.append(field.typeObjectName)
            else -> builder.append(field.type.kotlin)
        }
    }

    override fun build(): StringBuilder {
        addImport()
        classModels.forEach { classModel ->
            if (classModel.fields.isEmpty()) {
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
        const val VAL_CONST = "val "
        const val VAL_DELIMITER = ": "
        const val ARG_START = '('
        const val ARG_END = ')'
        const val ARG_DELIMITER = ','
    }

}