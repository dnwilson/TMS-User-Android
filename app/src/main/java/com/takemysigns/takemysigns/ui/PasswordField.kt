package com.takemysigns.takemysigns.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.takemysigns.TakeMySigns.R

@Composable
fun PasswordField(
    label: String = "Password",
    value: String,
    error: String?,
    imeAction: ImeAction,
    onTextChanged: (String) -> Unit,
    onImeAction: () -> Unit
) {
    val showPassword = remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = value,
            onValueChange = { value -> onTextChanged(value) },
            label = { label },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if(showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword.value = !showPassword.value }) {
                    Icon(
                        imageVector = if (showPassword.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = stringResource(id = if (showPassword.value) R.string.hide_password else R.string.show_password)
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(onAny = { onImeAction() }),
            isError = error != null
        )
        error?.let { InputErrorMessage(it) }
    }

}

//@Preview
//@Composable
//fun PasswordFieldPreview() {
//    PasswordField()
//}