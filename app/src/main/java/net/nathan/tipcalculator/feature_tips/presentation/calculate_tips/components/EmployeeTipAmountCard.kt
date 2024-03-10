package net.nathan.tipcalculator.feature_tips.presentation.calculate_tips.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.nathan.tipcalculator.core.presentation.components.CheckButton

@Composable
fun EmployeeTipAmountCard(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit,
    onCheckClick: () -> Unit,
    checkColour: Color,
    name: String,
    tipAmount: Double,
    hours: Int?,
    minutes: Int?,
    isChecked: Boolean
){
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onCardClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.85f)
                    .padding(start = 16.dp)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 32.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var calcHours = hours
                    var calcMins = minutes
                    if (calcHours == null) {
                        calcHours = 0
                    }
                    if (calcMins == null) {
                        calcMins = 0
                    }
                    var time = ""
                    time += "$calcHours hour"
                    if (calcHours != 1) {
                        time += "s"
                    }
                    time = "$time, "
                    time += "$calcMins minute"
                    if (calcMins != 1) {
                        time += "s"
                    }
                    // cast and add a zero to ensure there is always 2 decimal places
                    var tipString = tipAmount.toString() + "0"
                    val indexOfPoint = tipString.lastIndexOf('.')
                    tipString = tipString.substring(0, indexOfPoint + 3)
                    Text(
                        text = "£${tipString} ($time)",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            Column(
                modifier = Modifier.weight(0.15f)
                    .padding(end = 4.dp, top = 4.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End
            ) {
                CheckButton(
                    modifier = Modifier.height(48.dp),
                    onCheckClick = {onCheckClick()},
                    colour = checkColour,
                    isChecked = isChecked
                )
            }
        }
    }
}

@Preview
@Composable
fun EmployeeTipAmountCardPreview(){
    EmployeeTipAmountCard(
        name = "Nathan",
        onCardClick = {},
        tipAmount = 7.8,
        hours = 4,
        minutes = 34,
        onCheckClick = {},
        isChecked = false,
        checkColour = MaterialTheme.colorScheme.onPrimary
    )
}