package com.playground.redux.common.binding

import android.databinding.BindingAdapter
import android.text.TextWatcher
import android.widget.EditText

@BindingAdapter("addTextWatcher")
fun setupTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}