package com.wynneve.apichat

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wynneve.apichat.composables.CustomTextField
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorInactive

@Composable
fun SettingsScreen(settings: SettingsViewModel) {
    settings.scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        HeaderRow {
            Row {
                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick=settings::onNavigateBackClick
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
                    onClick = settings::onApplySettingsClick,
                    enabled = settings.getValid() && settings.getChanged()
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Check,
                        contentDescription = "Apply",
                        tint =
                            if(settings.getValid() && settings.getChanged()) MaterialTheme.colorScheme.onSurface
                            else colorInactive
                    )
                }

                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick = settings::onDiscardSettingsClick,
                    enabled = settings.getChanged()
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Discard",
                        tint =
                            if(settings.getChanged()) MaterialTheme.colorScheme.onSurface
                            else colorInactive
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(settings.scrollState!!),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SettingsGroup(title = "API and LLM settings") {
                SettingsItem(
                    title = "API Endpoint",
                    value = { settings.get(Setting.apiEndpoint) },
                    onValueChange = settings::onApiEndpointType,
                    placeholder = settings.getDefault(Setting.apiEndpoint),
                )

                SettingsItem(
                    title = "Maximum tokens",
                    value = { settings.get(Setting.maxTokens) },
                    onValueChange = settings::onMaxTokensType,
                    placeholder = settings.getDefault(Setting.maxTokens),
                )

                SettingsItem(
                    title = "Repetition penalty",
                    value = { settings.get(Setting.repetitionPenalty) },
                    onValueChange = settings::onRepetitionPenaltyType,
                    placeholder = settings.getDefault(Setting.repetitionPenalty),
                )

                SettingsItem(
                    title = "Temperature",
                    value = { settings.get(Setting.temperature) },
                    onValueChange = settings::onTemperatureType,
                    placeholder = settings.getDefault(Setting.temperature),
                )

                SettingsItem(
                    title = "Top P",
                    value = { settings.get(Setting.topP) },
                    onValueChange = settings::onTopPType,
                    placeholder = settings.getDefault(Setting.topP),
                )
            }

            SettingsGroup(title = "Dialogue settings") {
                SettingsItem(
                    title = "User name",
                    value = { settings.get(Setting.userName) },
                    onValueChange = settings::onUserNameType,
                    placeholder = settings.getDefault(Setting.userName),
                )

                SettingsItem(
                    title = "Bot name",
                    value = { settings.get(Setting.botName) },
                    onValueChange = settings::onBotNameType,
                    placeholder = settings.getDefault(Setting.botName),
                )

                SettingsItem(
                    title = "Context",
                    value = { settings.get(Setting.context) },
                    onValueChange = settings::onContextType,
                    placeholder = settings.getDefault(Setting.context),
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
    placeholder: String,
    value: () -> String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 1,
) {
    NamedTextField(
        title = title,
        placeholder = placeholder,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        textModifier = Modifier
            .padding(start = 10.dp),
        fieldModifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
    )
}

@Composable
@Preview
fun SettingsScreenPreview() {
    APIChatTheme {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val settings = viewModel { SettingsViewModel(context, scope, {}) }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SettingsScreen(settings = settings)
        }
    }
}