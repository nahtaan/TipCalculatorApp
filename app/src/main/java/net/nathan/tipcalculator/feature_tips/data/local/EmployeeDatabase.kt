package net.nathan.tipcalculator.feature_tips.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import net.nathan.tipcalculator.feature_tips.data.local.dto.LocalEmployeeEntryItem

@Database(
    entities = [LocalEmployeeEntryItem::class],
    version = 3,
    exportSchema = false
)
abstract class EmployeeDatabase: RoomDatabase() {
    abstract val dao: EmployeeEntryDao

    companion object{
        const val DATABASE_NAME = "employee_db"
    }
}