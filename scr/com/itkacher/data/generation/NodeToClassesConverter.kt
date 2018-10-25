package com.itkacher.data.generation

import com.fasterxml.jackson.databind.JsonNode
import com.itkacher.Resources
import com.itkacher.data.generation.printer.BaseClassModelPrinter
import com.itkacher.extension.isPrimitive
import com.itkacher.views.json.JsonMutableTreeNode

class NodeToClassesConverter {

    private val nodesNameMap = HashMap<ObjectClassModel, HashMap<String, Int>>()
    private val classNameMap = HashMap<String, Int>()

    private val classModels = ArrayList<ObjectClassModel>()

    private fun createUniqueField(classModel: ObjectClassModel, key: String, node: JsonNode): FieldModel {
        val type = getFieldType(node)
        var listGenericType: FieldType? = null
        var fieldWarnings: String? = null
        val ifObjectName = when (type) {
            FieldType.OBJECT -> {
                val innerClassName = getUniqueClassName(key).capitalize()
                createAndFillClass(innerClassName, node, classModel)
                innerClassName
            }
            FieldType.LIST -> {
                if (node.size() == 0) {
                    listGenericType = FieldType.OBJECT
                    null
                } else {
                    val firstChild: JsonNode? = node.get(0)
                    firstChild?.let {
                        when {
                            it.isObject -> {
                                val innerClassName = getUniqueClassName(key).capitalize()
                                createAndFillClass(innerClassName, node, classModel)
                                innerClassName
                            }
                            it.isArray -> {
                                fieldWarnings = Resources.getString("class_generation_array_of_array")
                                null
                            }
                            it.isPrimitive() ||
                                    it.isNull -> {
                                listGenericType = getFieldType(firstChild)
                                null
                            }
                            else -> {
                                null
                            }
                        }
                    }
                }
            }
            else -> null
        }
        return FieldModel(getUniqueNodeName(classModel, key), key, type, ifObjectName, listGenericType, fieldWarnings)
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
        val classModel = ObjectClassModel()
        when {
            node?.isObject == true -> {
                classModel.name = name.capitalize()
                classModel.parentClass = parentClass
                classModels.add(classModel)
                val fields = node.fields()
                fields.forEach {
                    classModel.fields.add(createUniqueField(classModel, it.key, it.value))
                }
            }
            node?.isArray == true -> {
                val child: JsonNode? = node.get(0)
                if(child != null) {
                    val fieldType = getFieldType(child)
                    val newName = "list"
                    if(fieldType == FieldType.OBJECT || fieldType == FieldType.LIST) {
                        val className = getUniqueClassName(newName).capitalize()
                        createAndFillClass(className, child, classModel)
                        classModel.fields.add(FieldModel(newName, name, FieldType.LIST, className))
                    } else {
                        classModel.fields.add(FieldModel(newName, name, fieldType))
                    }
                    classModels.add(classModel)
                }
            }
            else -> {
                classModel.name = name
            }
//            node != null -> generateSingleVar(node.name, node.value)
        }
        classModels.add(classModel)
    }

    fun buildClasses(node: JsonMutableTreeNode): NodeToClassesConverter {
        createAndFillClass(getUniqueClassName(node.name), node.value)
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
}