package com.itkacher.data.generation

data class FieldModel(
        val fieldName: String,
        val fieldOriginName: String,
        val fieldType: FieldType,
        val fieldTypeObjectName: String? = null
)