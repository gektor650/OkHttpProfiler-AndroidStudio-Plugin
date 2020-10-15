package com.itkacher.views.list

enum class TableColumn(val text: String, val widthPercent: Int) {
    STATUS("Status", 5),
    METHOD("Method", 5),
    REQUEST("Request", 75),
    DURATION("Duration", 5),
    TIME("Time", 10),
}
