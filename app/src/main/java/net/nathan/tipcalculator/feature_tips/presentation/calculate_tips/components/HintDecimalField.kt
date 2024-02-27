package net.nathan.tipcalculator.feature_tips.presentation.calculate_tips.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun HintDecimalField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    fontSize: TextUnit = 32.sp,
    textColour: Color,
    isHintVisible: Boolean = true,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit
){
    Box(
        modifier = modifier
    ){
        BasicTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            value = if(isHintVisible) "" else value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = fontSize,
                color = textColour,
            ),
            singleLine = singleLine,
            modifier = Modifier
                .onFocusChanged { onFocusChange(it) }
        )
        if(isHintVisible){
            Text(
                text = hint,
                color = textColour,
                fontSize = fontSize
            )
        }
    }
}