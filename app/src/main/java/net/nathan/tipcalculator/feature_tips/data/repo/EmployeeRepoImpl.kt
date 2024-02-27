package net.nathan.tipcalculator.feature_tips.data.repo

import kotlinx.coroutines.CoroutineDispatcher
import net.nathan.tipcalculator.feature_tips.data.di.IoDispatcher
import net.nathan.tipcalculator.feature_tips.data.local.EmployeeEntryDao
import net.nathan.tipcalculator.feature_tips.data.mapper.toEntryItem
import net.nathan.tipcalculator.feature_tips.data.mapper.toEntryItemList
import net.nathan.tipcalculator.feature_tips.data.mapper.toLocalEntry
import net.nathan.tipcalculator.feature_tips.data.mapper.toLocalEntryList
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem
import net.nathan.tipcalculator.feature_tips.domain.repo.EmployeeRepo

class EmployeeRepoImpl(
    private val dao: EmployeeEntryDao,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
):EmployeeRepo {
    override suspend fun getAllEmployeeEntries(): List<EmployeeEntryItem> {
        return dao.getAllEmployeeEntryItems().toEntryItemList();
    }

    override suspend fun getSingleEmployeeEntryById(id: Int): EmployeeEntryItem? {
        return dao.getSingleEmployeeEntryById(id)?.toEntryItem()
    }

    override suspend fun getAllSelectedEmployeeEntries(): List<EmployeeEntryItem> {
        return dao.getAllSelectedEmployeeEntries().toEntryItemList()
    }

    override suspend fun updateAllEmployeeEntries(employees: List<EmployeeEntryItem>) {
        dao.updateAllEmployeeEntries(employees.toLocalEntryList())
    }

    override suspend fun addEmployeeEntry(employee: EmployeeEntryItem) {
        dao.addEmployeeEntry(employee.toLocalEntry())
    }

    override suspend fun updateEmployeeEntry(employee: EmployeeEntryItem) {
        dao.addEmployeeEntry(employee.toLocalEntry())
    }

    override suspend fun deleteEmployeeEntry(employee: EmployeeEntryItem) {
        dao.deleteEmployeeEntry(employee.toLocalEntry())
    }

    override suspend fun deleteAllEmployeeEntries() {
        dao.deleteAllEmployeeEntries()
    }
}