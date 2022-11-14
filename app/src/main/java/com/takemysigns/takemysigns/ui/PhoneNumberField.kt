package com.takemysigns.takemysigns.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
            label = { label },
            visualTransformation = { mobileNumberFilter(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(onAny = { onImeAction() }),
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        error?.let { InputErrorMessage(it) }
    }
}

//@Preview
//@Composable
//fun PhoneNumberFieldPreview() {
//    PhoneNumberField()
//}