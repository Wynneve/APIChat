package com.wynneve.apichat.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NamedGroup(
    topPadding: Dp = 5.dp,
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.displayLarge,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    scrollable: Boolean = true,
    content: @Composable () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(top = topPadding),
        text = title,
        style = titleStyle,
        color = titleColor
    )

    ContentListColumn(scrollable = scrollable) {
        content()
    }
}