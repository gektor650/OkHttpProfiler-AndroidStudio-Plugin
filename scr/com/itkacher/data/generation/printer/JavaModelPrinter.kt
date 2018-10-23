package com.itkacher.data.generation.printer

import com.itkacher.data.generation.ClassModel
import com.itkacher.data.generation.FieldModel

class JavaModelPrinter(private val classModels: List<ClassModel>) : BaseClassModelPrinter() {

    override fun addImport() {
        builder.append(IMPORT_NULLABLE)
        super.addImport()
    }

    override fun addField(field: FieldModel) {
        if(! field.fieldType.isJavaPrimitive) {
            addNullableAnnotation()
        }
        addSerializationAnnotation(field.fieldOriginName)
        builder.append(
                TABULATION,
                CONST_VISIBILITY
        )
        if(field.fieldTypeObjectName != null) {
            builder.append(field.fieldTypeObjectName)
        } else {
            builder.append(field.fieldType.java)
        }
        builder.append(SPACE)
        builder.append(field.fieldName)
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

    override fun buildString(): StringBuilder {
        addImport()
        classModels.forEachIndexed { index, classModel ->

            builder.append(CLASS_NAME)
            builder.append(classModel.name)
            builder.append(START_OF_CLASS)
            builder.append(LINE_BREAK)
            builder.append(LINE_BREAK)

            if(classModel.fields.isEmpty()) {
                builder.append(TODO_NULLABLE)
            } else {
                classModel.fields.forEach { field ->
                    addField(field)
                }
            }
            builder.append(LINE_BREAK)
            if (classModels.size > 1 && index == 0) {

            } else if(classModels.size > 1 && index == classModels.size - 1) {
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