package com.itkacher.data.generation

class ObjectClassModel(var name: String) {
    var parentClass: ObjectClassModel? = null
    var fields: ArrayList<FieldModel> = ArrayList()
    var innerClasses: ArrayList<ObjectClassModel> = ArrayList()
}



