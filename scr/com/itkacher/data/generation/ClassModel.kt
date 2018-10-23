package com.itkacher.data.generation

data class ClassModel(
    val name: String,
    val parentClass: ClassModel? = null,
    val fields: ArrayList<FieldModel> = ArrayList(),
    val innerClasses: ArrayList<ClassModel> = ArrayList()
) {
    var unsupported: Boolean = false
    fun markAsUnSupported() {
        unsupported = true
    }
}



