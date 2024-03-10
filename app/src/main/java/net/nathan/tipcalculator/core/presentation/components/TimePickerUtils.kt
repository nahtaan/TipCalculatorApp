package net.nathan.tipcalculator.core.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.nathan.tipcalculator.core.util.TipStrings

@Composable
fun OutlinedTimePickerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    hour: Int,
    minute: Int,
    titleStyle: TextStyle,
    titleSize: TextUnit,
    titleColour: Color,
    timeSize: TextUnit,
    timeColour: Color,
    timeStyle: TextStyle,
    shape: Shape,
    borderWidth: Dp,
    borderColour: Color
){
    Box(
        modifier = modifier
            .border(
                shape = shape,
                width = borderWidth,
                color = borderColour
            ).clickable { onClick() }
    ){
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = titleStyle,
                fontSize = titleSize,
                color = titleColour
            )
            var time = if(hour < 10){
                "0${hour}:"
            }else{
                "${hour}:"
            }
            if(minute < 10){
                time += "0${minute}"
            }else{
                time += minute
            }
            Text(
                text = time,
                color = timeColour,
                fontSize = timeSize,
                style = timeStyle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    timePickerState: TimePickerState,
    header: String,
    cardColors: CardColors,
    timePickerColours: TimePickerColors,
    headerColour: Color
){
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ){
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .height(IntrinsicSize.Min),
            shape = RoundedCornerShape(10.dp),
            colors = cardColors
        ) {
            Column(
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = header,
                        style = MaterialTheme.typography.headlineSmall,
                        color = headerColour
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                        .weight(0.8f)
                ){
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier,
                        colors = timePickerColours,
                        layoutType = TimePickerLayoutType.Vertical
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                        .weight(0.1f)
                ){
                    Button(
                        onClick = {
                            onSave()
                        }
                    ){
                        Text(
                            text = TipStrings.CONFIRM
                        )
                    }
                }
            }
        }
    }
}