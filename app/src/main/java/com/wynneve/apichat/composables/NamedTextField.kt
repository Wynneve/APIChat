package com.wynneve.apichat.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.ui.theme.APIChatTheme

// Must be called in Row/Column scope in order to properly space composables.
@Composable
fun NamedTextField(
    textModifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    title: String,
    placeholder: String,
    value: () -> String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Text(
        modifier = textModifier,
        text = title,
        style = MaterialTheme.typography.displayMedium
    )

    CustomTextField(
        fieldModifier = fieldModifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        singleLine = singleLine,
        minLines = maxLines,
        maxLines = minLines,
        shape = RoundedCornerShape(5.dp),
        horizontalPadding = 5.dp,
        verticalPadding = 5.dp,
        visualTransformation = visualTransformation,
    )
}

@Preview
@Composable
fun NamedTextFieldPreview() {
    APIChatTheme {
        NamedTextField(
            title = "This is a named text field",
            placeholder = "Placeholder",
            value = { "Value" },
            onValueChange = {}
        )
    }
}