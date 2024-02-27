package net.nathan.tipcalculator.feature_tips.presentation.add_edit_employee.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun HintPositiveNumberField(
    modifier: Modifier = Modifier,
    value: Int?,
    hint: String,
    fontSize: TextUnit = 32.sp,
    textColour: Color,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit
){
    Box(
        modifier = modifier
    ){
        Column {
            Text(
                text = hint,
                style = MaterialTheme.typography.headlineSmall.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.fillMaxWidth()
            )
            BasicTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                value = value?.toString() ?: "",
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = fontSize,
                    color = textColour,
                ),
                singleLine = singleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { onFocusChange(it) }
            )
        }
    }
}