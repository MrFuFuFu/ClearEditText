package com.example.barryirvine.clearableedittext.textwatchers;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public abstract class ErrorTextWatcher implements TextWatcher {

    private TextInputLayout mTextInputLayout;
    private String errorMessage;

    protected ErrorTextWatcher(@NonNull final TextInputLayout textInputLayout, @NonNull final String errorMessage) {
        this.mTextInputLayout = textInputLayout;
        this.errorMessage = errorMessage;
    }

    public final boolean hasError() {
        return mTextInputLayout.getError() != null;
    }

    public abstract boolean validate();

    protected String getEditTextValue() {
        return mTextInputLayout.getEditText().getText().toString();
    }

    protected void showError(final boolean error) {
        if (!error) {
            mTextInputLayout.setError(null);
            mTextInputLayout.setErrorEnabled(false);
        } else {
            if (!errorMessage.equals(mTextInputLayout.getError())) {
                // Stop the flickering that happens when setting the same error message multiple times
                mTextInputLayout.setError(errorMessage);
            }
            mTextInputLayout.requestFocus();
        }
    }

    @Override
    public void beforeTextChanged(final CharSequence text, final int start, final int count, final int after) {

    }

    @Override
    public void afterTextChanged(final Editable s) {

    }
}
