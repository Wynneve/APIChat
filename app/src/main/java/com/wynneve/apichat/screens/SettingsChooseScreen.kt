package com.wynneve.apichat.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.ui.theme.APIChatTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsChooseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        HeaderRow(
            title = "Settings",
            navigation = {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .size(25.dp),
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Back",
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 10.dp,
                    end = 10.dp
                )
        ) {
            ContentListColumn {
                SettingEntry("Profile settings", {})

                SettingEntry("Global chat settings", {})
            }
        }
    }
}

@Composable
fun SettingEntry(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp)
            )
            .height(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            style = MaterialTheme.typography.displayMedium,
            text = text
        )

        Icon(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(25.dp),
            tint = MaterialTheme.colorScheme.onBackground,
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "",
        )
    }
}

@Preview
@Composable
fun SettingsChooseScreenPreview() {
    APIChatTheme {
        SettingsChooseScreen()
    }
}