@file:Suppress("UNUSED_EXPRESSION")

package com.takemysigns.takemysigns.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.takemysigns.takemysigns.ui.helpers.mobileNumberFilter

@Composable
fun PhoneNumberField(
    label: String = "Phone number",
    value: String,
    error: String?,
    imeAction: ImeAction,
    onTextChanged: (String) -> Unit,
    onImeAction: () -> Unit
){
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { value -> onTextChanged(value) },
            label = { Text(label) },
            visualTransformation = { mobileNumberFilter(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(onAny = { onImeAction() }),
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            )
        )
        error?.let { InputErrorMessage(it) }
    }
}

@Preview
@Composable
fun PhoneNumberFieldPreview() {
    PhoneNumberField(
        label = "Phone number",
        value = "",
        error = "",
        imeAction = ImeAction.Next,
        onTextChanged = { Log.d("PhoneNumberField", "onTextChanged fired...") },
        onImeAction = { Log.d("PhoneNumberField", "onImeAction fired...") }
    )
}