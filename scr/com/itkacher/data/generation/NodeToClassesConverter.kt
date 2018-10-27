package com.itkacher.data.generation

import com.fasterxml.jackson.databind.JsonNode
import com.itkacher.data.generation.printer.BaseClassModelPrinter
import com.itkacher.extension.isPrimitive
import com.itkacher.views.json.JsonMutableTreeNode
import java.util.concurrent.atomic.AtomicInteger

class NodeToClassesConverter {

    private val nodesNameMap = HashMap<ObjectClassModel, HashMap<String, Int>>()
    private val classNameMap = HashMap<String, Int>()

    private val classModels = ArrayList<ObjectClassModel>()

    private fun createUniqueField(classModel: ObjectClassModel, key: String, node: JsonNode): FieldModel {
        val type = getFieldType(node)
        var listGenericType: FieldType? = null
        var nestingLevel: AtomicInteger? = null
        val ifObjectName = when (type) {
            FieldType.OBJECT -> {
                val innerClassName = getUniqueClassName(key).capitalize()
                createAndFillClass(innerClassName, node, classModel)
                innerClassName
            }
            FieldType.LIST -> {
                nestingLevel = AtomicInteger()
                val pair = getNestedFieldType(classModel, key, nestingLevel, node)
                listGenericType = pair.second
                pair.first
            }
            else -> null
        }
        return FieldModel(getUniqueNodeName(classModel, key), key, type, ifObjectName, listGenericType, nestingLevel)
    }

    private fun getNestedFieldType(classModel: ObjectClassModel,
                                   name: String,
                                   nestingLevel: AtomicInteger,
                                   node: JsonNode): Pair<String?, FieldType> {
        nestingLevel.incrementAndGet()
        var type: FieldType = FieldType.OBJECT
        var className: String? = null
        node.firstOrNull()?.let {
            when {
                it.isObject -> {
                    val innerClassName = getUniqueClassName(name).capitalize()
                    createAndFillClass(innerClassName, it, null)
                    type = FieldType.OBJECT
                    className = innerClassName
                }
                it.isArray -> {
                    return getNestedFieldType(classModel, name, nestingLevel, it)
                }
                it.isPrimitive() ||
                        it.isNull -> {
                    type = getFieldType(it)
                    className = null
                }
                else -> {
                    type = getFieldType(it)
                    className = null
                }
            }
        }

        return Pair(className, type)
    }

    private fun getFieldType(node: JsonNode): FieldType {
        return when {
            node.isTextual -> FieldType.STRING
            node.isBoolean -> FieldType.BOOLEAN
            node.isNull -> FieldType.OBJECT
            node.isLong -> FieldType.LONG
            node.isInt -> FieldType.INTEGER
            node.isDouble -> FieldType.DOUBLE
            node.isFloat -> FieldType.FLOAT
            node.isArray -> FieldType.LIST
            node.isObject -> FieldType.OBJECT
            else -> FieldType.UNDEFINED
        }
    }

    private fun createAndFillClass(name: String, node: JsonNode?, parentClass: ObjectClassModel? = null) {
        val classModel = ObjectClassModel(getUniqueClassName(name).capitalize())
        classModels.add(classModel)
        when {
            node?.isObject == true -> {
                classModel.parentClass = parentClass
                val fields = node.fields()
                fields.forEach {
                    classModel.fields.add(createUniqueField(classModel, it.key, it.value))
                }
            }
            node?.isArray == true -> {
                classModel.fields.add(createUniqueField(classModel, classModel.name, node))
            }
            else -> {
                node?.let {
                    val type = getFieldType(node)
                    classModel.fields.add(FieldModel(name, name, type))
                }
            }
        }
    }

    fun buildClasses(node: JsonMutableTreeNode): NodeToClassesConverter {
        createAndFillClass(node.name, node.value)
        return this
    }

    fun getClasses(): ArrayList<ObjectClassModel> {
        return classModels
    }

    private fun reformatName(name: String): String {
        return when {
            name.isEmpty() -> BaseClassModelPrinter.OBJECT_NAME_DEFAULT
            name.contains(BaseClassModelPrinter.UNDERLINE_CHAR) -> {
                val namePart = name.split(BaseClassModelPrinter.UNDERLINE_CHAR)
                val nameBuilder = StringBuilder()
                namePart.forEachIndexed { index, s ->
                    if (index == 0) {
                        nameBuilder.append(s)
                    } else {
                        nameBuilder.append(s.capitalize())
                    }
                }
                nameBuilder.toString()
            }
            else -> name
        }
    }


    private fun getUniqueNodeName(classModels: ObjectClassModel, name: String): String {
        var map = nodesNameMap[classModels]
        if (map == null) {
            map = HashMap()
            nodesNameMap[classModels] = map
        }
        return getUniqueNameFromMap(map, name)
    }

    private fun getUniqueClassName(name: String): String {
        return getUniqueNameFromMap(classNameMap, name)
    }

    private fun getUniqueNameFromMap(map: HashMap<String, Int>, name: String): String {
        var newName = if (name.startsWith("[")) {
            getUniqueClassName("ArrayListOf")
        } else {
            reformatName(name)
        }
        val count = map[newName]
        return if (count == null) {
            map[newName] = 1
            if(RESERVED_WORDS.contains(name)) {
                "${newName}0"
            } else {
                newName
            }
        } else {
            map[newName] = count.inc()
            "$newName$count"
        }
    }

    companion object {
        val RESERVED_WORDS = arrayOf(
                "package", "as", "typealias", "class", "this", "super", "val", "var",
                "fun", "for", "null", "true", "false", "is", "in", "throw", "return", "break", "continue", "object", "if",
                "try", "else", "while", "do", "when", "interface", "yield", "typeof", "abstract", "continue", "for", "new", "switch",
                "assert", "default", "goto", "package", "synchronized",
                "boolean", "do", "if", "private", "this",
                "break", "double", "implements", "protected", "throw",
                "byte", "else", "import", "public", "throws",
                "case", "enum", "instanceof", "return", "transient",
                "catch", "extends", "int", "short", "try",
                "char", "final", "interface", "static", "void",
                "class", "finally", "long", "strictfp", "volatile",
                "const", "float", "native", "super", "while"
        )
    }
}