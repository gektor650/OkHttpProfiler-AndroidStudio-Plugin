package com.itkacher.extension

fun String?.safe(): String {
    return if(this?.isNotEmpty() == true) {
        this
    } else {
        ""
    }
}