package com.itkacher.data.generation

enum class FieldType(val java: String,val kotlin: String,val isJavaPrimitive: Boolean = false,val javaWrapper: String) {
    INTEGER("int", "Int?", true, "Integer"),
    LONG("long", "Long?", true, "Long"),
    BOOLEAN("boolean", "Boolean?", true, "Boolean"),
    FLOAT("float", "Float?", true, "Float"),
    DOUBLE("double", "Double?", true, "Double"),
    OBJECT("Object", "Any?", false, "Object"),
    LIST("List", "List", false, "List"),
    STRING("String", "String?", false, "String"),
    UNDEFINED("?", "?", false, "?"),
}