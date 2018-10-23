package com.itkacher.data.generation

enum class FieldType(val java: String,val kotlin: String,val isJavaPrimitive: Boolean = false) {
    INTEGER("int", "Int?", true),
    LONG("long", "Long?", true),
    BOOLEAN("boolean", "Boolean?", true),
    FLOAT("float", "Float?", true),
    DOUBLE("double", "Double?", true),
    OBJECT("Object", "Object?"),
    LIST("List", "List"),
    STRING("String", "String?"),
    UNDEFINED("?", "?"),
}