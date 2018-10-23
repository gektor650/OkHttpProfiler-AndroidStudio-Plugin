package com.itkacher.data.generation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.itkacher.data.generation.printer.BaseClassModelPrinter
import com.itkacher.views.json.JsonMutableTreeNode

class NodeToClassesConverter {

    private val nodesNameMap = HashMap<ClassModel, HashMap<String, Int>>()
    private val classNameMap = HashMap<String, Int>()

    private val classModels = ArrayList<ClassModel>()

    private fun getUniqueNodeName(classModels: ClassModel, name: String): String {
        var map = nodesNameMap[classModels]
        if(map == null) {
            map = HashMap()
            nodesNameMap[classModels] = map
        }
        return getUniqueNameFromMap(map, name)
    }

    private fun getUniqueClassName(name: String): String {
        return getUniqueNameFromMap(classNameMap, name)
    }

    private fun getField(classModel: ClassModel, key: String, node: JsonNode): FieldModel {
        val type = getFieldType(node)
        val ifObjectName = if(type == FieldType.OBJECT || type == FieldType.LIST) {
            val innerClassName = getUniqueClassName(key).capitalize()
            createAndFillClass(innerClassName, node, classModel)
            innerClassName
        } else {
            null
        }
        return FieldModel(getUniqueNodeName(classModel, key), key, type, ifObjectName)
    }

    private fun getFieldType(node: JsonNode): FieldType {
        return when {
            node.isTextual -> FieldType.STRING
            node.isBoolean -> FieldType.BOOLEAN
            node.isNull -> FieldType.OBJECT
            node.isLong  -> FieldType.LONG
            node.isInt -> FieldType.INTEGER
            node.isDouble  -> FieldType.DOUBLE
            node.isFloat -> FieldType.FLOAT
            node.isArray -> FieldType.LIST
            node.isObject -> FieldType.OBJECT
            else -> FieldType.UNDEFINED
        }
    }

    private fun createAndFillClass(name: String, node: JsonNode?, parentClass: ClassModel? = null) {
        val classModel = ClassModel(name.capitalize(), parentClass)
        classModels.add(classModel)
        when {
            node?.isObject == true -> {
                val fields = node.fields()
                fields.forEach {
                    classModel.fields.add(getField(classModel, it.key, it.value))
                }
            }
            node?.isArray == true -> {
                if(node.size() == 0) {
//                    createAndFillClass()
                } else {
                    val firstNodeType : JsonNodeType? = node.get(0).nodeType
                    val fields = HashMap<String, FieldModel>()
                    for (jsonNode in node) {
                        if(firstNodeType != jsonNode.nodeType) {
                            classModel.markAsUnSupported()
                            return
                        }
                        if(jsonNode.isObject) {

                        } else if(jsonNode.isArray) {

                        }
                    }
                }
            }
//            node != null -> generateSingleVar(node.name, node.value)
        }
    }

    fun buildClasses(node: JsonMutableTreeNode): NodeToClassesConverter {
        createAndFillClass(getUniqueClassName(node.name), node.value)
        return this
    }

    fun getClasses(): ArrayList<ClassModel> {
        return classModels
    }

    private fun getUniqueNameFromMap(map: HashMap<String, Int>, name: String): String {
        val newName = when {
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
                for (part in namePart) {
                }
                nameBuilder.toString()
            }
            else -> name
        }
        val count = map[newName]
        return if (count == null) {
            map[newName] = 1
            newName
        } else {
            map[newName] = count.inc()
            "$newName$count"
        }
    }
}