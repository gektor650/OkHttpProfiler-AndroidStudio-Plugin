package com.itkacher.extension

import com.fasterxml.jackson.databind.JsonNode

fun JsonNode?.isPrimitive(): Boolean {
    return this != null && !this.isNull && !this.isObject && !this.isArray && !this.isTextual && !this.isPojo
}