package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun getDummyText(): List<String> = listOf(
    stringResource(R.string.dummy1),
    stringResource(R.string.dummy2),
    stringResource(R.string.dummy3)
)