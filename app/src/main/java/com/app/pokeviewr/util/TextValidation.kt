package com.app.pokeviewr.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun validateEmail(email: String): Boolean {
    return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validateName(name: String): Boolean {
    return name.isNotEmpty()
}

fun validatePassword(password: String): Boolean {
    return password.isNotEmpty() && password.length >= 6
}

fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
    return password == confirmPassword
}

fun TextInputEditText.validateText(validationFunction: (String) -> Boolean, errorMessage: String, inputLayout: TextInputLayout) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            val isValid = validationFunction(text)
            inputLayout.error = if (!isValid) errorMessage else null
        }
    })
}