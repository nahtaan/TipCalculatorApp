package net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HintPositiveNumberField(
    modifier: Modifier = Modifier,
    value: Int?,
    label: @Composable () -> Unit,
    fontSize: TextUnit = 32.sp,
    textColour: Color,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    colours: TextFieldColors
){
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier.padding(5.dp)
        ) {
            OutlinedTextField(
                value = value?.toString() ?: "",
                onValueChange = {
                    onValueChange(it)
                },
                label = label,
                singleLine = singleLine,
                textStyle = TextStyle(
                    fontSize = fontSize,
                    color = textColour
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        onFocusChange(it)
                    },
                colors = colours
            )
        }
    }
}