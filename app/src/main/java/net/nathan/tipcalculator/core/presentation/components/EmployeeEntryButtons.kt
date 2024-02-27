package net.nathan.tipcalculator.core.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
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
fun EmptyCircle(colour: Color, strokeWidth: Float = 9f){
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            val radius = 41f
            drawCircle(
                center = center,
                radius = radius,
                style = Stroke(width = strokeWidth),
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