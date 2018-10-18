package com.itkacher.views.list

enum class TableColumn(val text: String, val widthPercent: Int) {
    STATUS("Status", 5),
    METHOD("Method", 5),
    REQUEST("Request", 50),
    DURATION("Duration", 20),
    TIME("Time", 20),
}
