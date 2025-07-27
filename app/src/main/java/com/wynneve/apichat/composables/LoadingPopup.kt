package com.wynneve.apichat.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.ui.theme.colorShadow
import com.wynneve.apichat.R

// Expected to be called outside any container scopes!

@Composable
fun LoadingPopup(loading: Boolean) {
    if(loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorShadow)
                .clickable(enabled = false) {},
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(10.dp),
                text = LocalContext.current.getString(R.string.loading_Loading)
            )
        }
    }
}