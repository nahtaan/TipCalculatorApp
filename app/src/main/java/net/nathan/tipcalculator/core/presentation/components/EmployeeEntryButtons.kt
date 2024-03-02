package net.nathan.tipcalculator.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import net.nathan.tipcalculator.core.util.ContentDescriptions


@Composable
fun CheckButton(
    onCheckClick: () -> Unit,
    colour: Color,
    isChecked: Boolean,
    modifier: Modifier = Modifier
){
    IconButton(
        onClick = { onCheckClick() },
        modifier = modifier
    ){
        if(isChecked){
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = ContentDescriptions.CHECK_BUTTON,
                tint = colour,
                modifier = modifier.fillMaxSize()
            )
        }else{
            EmptyCircle(colour = colour)
        }
    }
}

@Composable
fun EmptyCircle(colour: Color){
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawCircle(
                center = center,
                radius = 15.dp.toPx(),
                style = Stroke(width = 3.dp.toPx()),
                color = colour
            )
    })
}


@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
){
    IconButton(
        onClick = { onDeleteClick() },
        modifier = modifier
    ){
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = ContentDescriptions.DELETE_BUTTON,
            tint = Color.Red,
            modifier = modifier.size(128.dp)
        )
    }
}