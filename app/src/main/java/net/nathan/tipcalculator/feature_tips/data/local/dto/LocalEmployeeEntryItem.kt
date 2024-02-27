package net.nathan.tipcalculator.feature_tips.data.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class LocalEmployeeEntryItem(
    val name: String,
    val hours: Int?,
    val minutes: Int?,
    val isSelected: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int?
)