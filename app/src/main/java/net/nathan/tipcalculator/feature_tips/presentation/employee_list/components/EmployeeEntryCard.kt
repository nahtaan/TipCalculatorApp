package net.nathan.tipcalculator.feature_tips.presentation.employee_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.nathan.tipcalculator.core.presentation.components.CheckButton
import net.nathan.tipcalculator.core.presentation.components.DeleteButton
import net.nathan.tipcalculator.feature_tips.domain.model.EmployeeEntryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeEntryCard(
    modifier: Modifier = Modifier,
    employee: EmployeeEntryItem,
    onDeleteClick: () -> Unit,
    onCardClick: () -> Unit,
    onCheckClick: () -> Unit
){
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onCardClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
    ) {
        Row(
            modifier = modifier
                .padding(start = 8.dp)
        ) {
            Column(
                modifier = modifier
                    .padding(start = 4.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = employee.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier
                            .weight(0.7f)
                    )
                    Row(
                        modifier = modifier
                            .weight(0.3f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CheckButton(
                            onCheckClick = onCheckClick,
                            colour = MaterialTheme.colorScheme.onPrimaryContainer,
                            isChecked = employee.isSelected
                        )
                        DeleteButton(
                            modifier = modifier,
                            onDeleteClick = onDeleteClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EmployeeEntryCardPreview(){
    EmployeeEntryCard(
        modifier = Modifier,
        employee = EmployeeEntryItem(
            "Nathan Bateson",
            1,
            30,
            true,
            null
        ),
        onDeleteClick = {},
        onCardClick = {},
        onCheckClick = {}
    )
}