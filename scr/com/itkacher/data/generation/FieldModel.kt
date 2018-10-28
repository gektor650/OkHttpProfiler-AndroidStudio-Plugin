package com.itkacher.data.generation

import java.util.concurrent.atomic.AtomicInteger

data class FieldModel(
        val name: String,
        val originName: String,
        val type: FieldType,
        val typeObjectName: String? = null,
        val genericType: FieldType? = null,
        val nestingLevel: AtomicInteger? = null
)