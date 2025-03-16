package ru.etu.auto.models

data class Reminder(
    val title: String,
    val dateAdded: String,
    val repairDate: String,
    val description: String,
    val mileage: Int
)
