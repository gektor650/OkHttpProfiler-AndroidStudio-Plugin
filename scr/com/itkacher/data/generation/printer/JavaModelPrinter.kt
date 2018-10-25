package com.itkacher.data.generation.printer

import com.itkacher.data.generation.ObjectClassModel
import com.itkacher.data.generation.FieldModel
import com.itkacher.data.generation.FieldType

class JavaModelPrinter(private val classModels: List<ObjectClassModel>) : BaseClassModelPrinter() {

    override fun addImport() {
        builder.append(IMPORT_NULLABLE)
        super.addImport()
    }

    override fun addField(field: FieldModel) {
        if (!field.type.isJavaPrimitive) {
            addNullableAnnotation()
        }
        addSerializationAnnotation(field.originName)
        builder.append(
                TABULATION,
                CONST_VISIBILITY
        )
        when {
            field.type == FieldType.LIST -> {
                builder.append(FieldType.LIST.java)
                builder.append(GENERIC_START)
                if (field.typeObjectName != null) {
                    builder.append(field.typeObjectName)
                } else {
                    builder.append(field.genericType?.javaWrapper)
                }
                builder.append(GENERIC_END)
            }
            field.typeObjectName != null -> builder.append(field.typeObjectName)
            else -> builder.append(field.type.java)
        }
        builder.append(SPACE)
        builder.append(field.name)
        builder.append(LINE_END)
        builder.append(LINE_BREAK)
        builder.append(LINE_BREAK)
    }

    private fun addNullableAnnotation(): JavaModelPrinter {
        builder.append(
                TABULATION,
                NULLABLE_ANNOTATION,
                LINE_BREAK
        )
        return this
    }

    override fun build(): StringBuilder {
        addImport()
        classModels.forEachIndexed { index, classModel ->

            builder.append(CLASS_NAME)
            builder.append(classModel.name)
            builder.append(START_OF_CLASS)
            builder.append(LINE_BREAK)
            builder.append(LINE_BREAK)

            if (classModel.fields.isEmpty()) {
                builder.append(TODO_NULLABLE)
            } else {
                classModel.fields.forEach { field ->
                    if (field.fieldWarning?.isNotEmpty() == true) {
                        builder.append(TODO, field.fieldWarning)
                    }
                    addField(field)
                }
            }
            builder.append(LINE_BREAK)
            if (classModels.size > 1 && index == 0) {

            } else if (classModels.size > 1 && index == classModels.size - 1) {
                builder.append(END_OF_CLASS)
                builder.append(LINE_BREAK)
                builder.append(END_OF_CLASS)
            } else {
                builder.append(END_OF_CLASS)
            }
            builder.append(LINE_BREAK)
            builder.append(LINE_BREAK)
        }
        return builder
    }

    companion object {
        const val IMPORT_NULLABLE = "import android.support.annotation.Nullable;\r\n"
        const val CONST_VISIBILITY = "private "
        const val NULLABLE_ANNOTATION = "@Nullable"
        const val SPACE = " "
    }
}