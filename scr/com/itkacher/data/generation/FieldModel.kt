package com.itkacher.data.generation

data class FieldModel(
        val name: String,
        val originName: String,
        val type: FieldType,
        val typeObjectName: String? = null,
        val genericType: FieldType? = null
)