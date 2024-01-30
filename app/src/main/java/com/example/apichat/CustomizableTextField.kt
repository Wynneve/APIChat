package com.example.apichat

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.apichat.ui.theme.colorPlaceholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.displayMedium,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    cursorColor: Color = MaterialTheme.colorScheme.onBackground,
    placeholder: String = "",
    placeholderColor: Color = colorPlaceholder,
    icon: ImageVector? = null,
    iconDescription: String = "",
    iconModifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    horizontalPadding: Dp = 0.dp,
    verticalPadding: Dp = 0.dp,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle.copy(
            color = textColor,
        ),
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        cursorBrush = SolidColor(cursorColor),
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = enabled,
            singleLine = singleLine,
            visualTransformation = VisualTransformation.None,
            interactionSource = MutableInteractionSource(),
            placeholder = @Composable {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle.copy(
                            color = placeholderColor
                        )
                    )
                }
            },
            leadingIcon =
                if(icon != null) {
                    @Composable {
                        Icon(
                            imageVector = icon,
                            contentDescription = iconDescription,
                            modifier = iconModifier,
                        )
                    }
                } else null,
            shape = shape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = placeholderColor,
                unfocusedPlaceholderColor = placeholderColor,
            ),
            contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding),
        )
    }
}

@Preview
@Composable
fun CustomizableTextFieldPreview() {
    CustomizableTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "This is a text",
        onValueChange = { },
    )
}