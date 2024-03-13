package com.wynneve.apichat.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.db.entities.DbUser
import com.wynneve.apichat.ui.theme.APIChatTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        HeaderRow(
            title = "New profile",
            navigation = {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight(),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .width(25.dp)
                            .height(25.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
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
                var pass = remember { mutableStateOf("") }
                CredentialField(
                    title = "Login",
                    placeholder = "Login",
                    value = { "" },
                    onValueChange = {},
                )
                CredentialField(
                    title = "Password",
                    placeholder = "Password",
                    value = { pass.value },
                    onValueChange = { pass.value = it },
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier)

                Button(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = {}
                ) {
                    Text(
                        text = "Create",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun NewProfileScreenPreview() {
    APIChatTheme {
        NewProfileScreen()
    }
}