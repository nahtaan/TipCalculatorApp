package net.nathan.tipcalculator.feature_tips.data.mapper

import net.nathan.tipcalculator.feature_tips.data.local.dto.LocalEmployeeEntryItem
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

fun EmployeeEntryItem.toLocalEntry(): LocalEmployeeEntryItem{
    return LocalEmployeeEntryItem(
        name = name,
        hours = hours,
        minutes = minutes,
        isSelected = isSelected,
        id = id
    )
}

fun LocalEmployeeEntryItem.toEntryItem(): EmployeeEntryItem{
    return EmployeeEntryItem(
        name = name,
        hours = hours,
        minutes = minutes,
        isSelected = isSelected,
        id = id
    )
}

fun List<EmployeeEntryItem>.toLocalEntryList(): List<LocalEmployeeEntryItem>{
    return this.map {item -> item.toLocalEntry()}
}

fun List<LocalEmployeeEntryItem>.toEntryItemList(): List<EmployeeEntryItem>{
    return this.map {item -> item.toEntryItem()}
}