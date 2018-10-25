package com.itkacher.data.generation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.itkacher.data.generation.printer.BaseClassModelPrinter
import com.itkacher.extension.isPrimitive
import com.itkacher.views.json.JsonMutableTreeNode

class NodeToClassesConverter {

    private val nodesNameMap = HashMap<ClassModel, HashMap<String, Int>>()
    private val classNameMap = HashMap<String, Int>()

    private val classModels = ArrayList<ClassModel>()

    private fun createUniqueField(classModel: ClassModel?, key: String, node: JsonNode): FieldModel {
        val type = getFieldType(node)
        var listGenericType: FieldType? = null
        val ifObjectName = when (type) {
            FieldType.OBJECT -> {
                val innerClassName = getUniqueClassName(key).capitalize()
                createAndFillClass(innerClassName, node, classModel)
                innerClassName
            }
            FieldType.LIST -> {
                val classOfList = getClassOfArrayNode(key, node)
                listGenericType = classOfList?.second
                classOfList?.first
            }
            else -> null
        }
        return if (classModel != null) {
            FieldModel(getUniqueNodeName(classModel, key), key, type, ifObjectName, listGenericType)
        } else {
            FieldModel(key, key, type, ifObjectName, listGenericType)
        }
    }

    private fun getClassOfArrayNode(arrayName: String, node: JsonNode): Pair<String?, FieldType?>? {
        val firstNodeType: JsonNodeType? = node.get(0)?.nodeType
        val fields = HashMap<String, JsonNode>()

        var fieldTypeIfPrimitive: FieldType? = null

        node.forEachIndexed { index, jsonNode ->
            if (firstNodeType != jsonNode.nodeType) {
                return Pair(String.format(UNSUPPORTED_CLASS, arrayName), FieldType.UNDEFINED);
            }
            when {
                jsonNode.isObject -> {
//                    for (field in jsonNode.fields()) {
//                        fields[field.key] = createUniqueField(null, field.key, field.value)
//                    }
//                    classModel.fields.addAll(fields.values)
                }
                jsonNode.isArray -> {

                }
                jsonNode.isPrimitive() -> {
                    return Pair(null, getFieldType(node))
                }
                else -> {}
            }
        }
        return Pair(null, null)
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

    private fun createAndFillClass(name: String, node: JsonNode?, parentClass: ClassModel? = null) {
        when {
            node?.isObject == true -> {
                val classModel = ClassModel(name.capitalize(), parentClass)
                classModels.add(classModel)
                val fields = node.fields()
                fields.forEach {
                    classModel.fields.add(createUniqueField(classModel, it.key, it.value))
                }
            }
            node?.isArray == true -> { }
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


    private fun getUniqueNodeName(classModels: ClassModel, name: String): String {
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
        val newName = reformatName(name)
        val count = map[newName]
        return if (count == null) {
            map[newName] = 1
            newName
        } else {
            map[newName] = count.inc()
            "$newName$count"
        }
    }

    companion object {
        const val UNSUPPORTED_CLASS = "ARRAY_(%s)_IS_UNSUPPORTED_BECAUSE_OF_DIFFERENT_VAL_TYPES"
    }
}