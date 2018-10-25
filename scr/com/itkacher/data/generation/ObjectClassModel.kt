package com.itkacher.data.generation

class ObjectClassModel {
    var name: String? = null
    var parentClass: ObjectClassModel? = null
    var fields: ArrayList<FieldModel> = ArrayList()
    var innerClasses: ArrayList<ObjectClassModel> = ArrayList()
}



