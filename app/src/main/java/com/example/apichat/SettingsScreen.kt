package com.example.apichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.apichat.ui.theme.APIChatTheme
import com.example.apichat.ui.theme.colorInactive

@Composable
fun SettingsScreen(settings: Settings, controller: SettingsController) {
    controller.scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface, shape = RectangleShape)
                .padding(horizontal = 5.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick=controller::onNavigateBackClick
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "Settings",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Row {
                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick = controller::onApplySettingsClick,
                    enabled = settings.valid.value && settings.changed.value
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Check,
                        contentDescription = "Apply",
                        tint =
                            if(settings.valid.value && settings.changed.value) MaterialTheme.colorScheme.onSurface
                            else colorInactive
                    )
                }

                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick = controller::onDiscardSettingsClick,
                    enabled = settings.changed.value
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Discard",
                        tint =
                            if(settings.changed.value) MaterialTheme.colorScheme.onSurface
                            else colorInactive
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(controller.scrollState!!),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SettingsGroup(title = "API and LLM settings") {
                SettingsItem(
                    title = "API Endpoint",
                    value = settings.values[Setting.apiEndpoint] ?: "",
                    onValueChange = controller::onApiEndpointType,
                    placeholder = "http://127.0.0.1:5000",
                )

                SettingsItem(
                    title = "Maximum tokens",
                    value = settings.values[Setting.maxTokens] ?: "" ,
                    onValueChange = controller::onMaxTokensType,
                    placeholder = (512).toString(),
                )

                SettingsItem(
                    title = "Repetition penalty",
                    value = settings.values[Setting.repetitionPenalty] ?: "",
                    onValueChange = controller::onRepetitionPenaltyType,
                    placeholder = (1.15f).toString(),
                )

                SettingsItem(
                    title = "Temperature",
                    value = settings.values[Setting.temperature] ?: "",
                    onValueChange = controller::onTemperatureType,
                    placeholder = (0.7f).toString(),
                )

                SettingsItem(
                    title = "Top P",
                    value = settings.values[Setting.topP] ?: "",
                    onValueChange = controller::onTopPType,
                    placeholder = (0.9f).toString(),
                )
            }

            SettingsGroup(title = "Character settings") {
                SettingsItem(
                    title = "User name",
                    value = settings.values[Setting.userName] ?: "",
                    onValueChange = controller::onUserNameType,
                    placeholder = "User",
                )

                SettingsItem(
                    title = "Bot name",
                    value = settings.values[Setting.botName] ?: "",
                    onValueChange = controller::onBotNameType,
                    placeholder = "Bot",
                )

                SettingsItem(
                    title = "Context",
                    value = settings.values[Setting.context] ?: "",
                    onValueChange = controller::onContextType,
                    placeholder = "This is a conversation between the User and LLM-powered AI Assistant named Bot. Bot is...",
                    singleLine = false,
                    minLines = 5,
                    maxLines = 5
                )
            }
        }
    }
}

@Composable
fun SettingsGroup(
    topPadding: Dp = 5.dp,
    title: String,
    titleStyle: TextStyle = MaterialTheme.typography.displayLarge,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    content: @Composable () -> Unit
) {
    Text(
        modifier = Modifier
            .padding(top = topPadding),
        text = title,
        style = titleStyle,
        color = titleColor
    )

    SettingsContainer() {
        content()
    }
}

@Composable
fun SettingsContainer(
    horizontalInnerPadding: Dp = 10.dp,
    verticalInnerPadding: Dp = 10.dp,
    spacing: Dp = 5.dp,
    borderRadius: Dp = 5.dp,
    columnColor: Color = MaterialTheme.colorScheme.surface,
    columnShape: Shape = RoundedCornerShape(borderRadius),
    columnModifier: Modifier =
        Modifier
            .fillMaxWidth()
            .background(color = columnColor, shape = columnShape)
            .padding(horizontal = horizontalInnerPadding, vertical = verticalInnerPadding),
    content: @Composable () -> Unit
) {
    Column(
        modifier = columnModifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        content()
    }
}

@Composable
fun SettingsItem(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
    textBorderRadius: Dp = 5.dp,
    textHorizontalPadding: Dp = 5.dp,
    textVerticalPadding: Dp = 5.dp,
    textShape: Shape = RoundedCornerShape(textBorderRadius),
    textModifier: Modifier =
        Modifier
            .fillMaxWidth()
) {
    Text(
        text = title,
    )

    CustomizableTextField(
        modifier = textModifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        singleLine = singleLine,
        minLines = maxLines,
        maxLines = minLines,
        shape = textShape,
        horizontalPadding = textHorizontalPadding,
        verticalPadding = textVerticalPadding,
    )
}

@Composable
@Preview
fun SettingsScreenPreview() {
    APIChatTheme() {
        val settings = remember { Settings() }
        val controller = SettingsController(settings, LocalContext.current, rememberCoroutineScope(), {})

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsScreen(settings = settings, controller = controller)
        }
    }
}