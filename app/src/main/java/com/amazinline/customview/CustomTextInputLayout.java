package com.amazinline.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.amazinline.R;
import com.google.android.material.textfield.TextInputLayout;

public class CustomTextInputLayout extends TextInputLayout {

    /**
     * Constructor
     * @param context
     */
    public CustomTextInputLayout(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     */
    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * When state is changed, change background
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        replaceBackground();
    }

    /**
     * When an error is present, change background
     * @param error
     */
    @Override
    public void setError(@Nullable final CharSequence error) {
        super.setError(error);

        replaceBackground();
    }

    /**
     * Replace the background if an error is enabled
     * Or set the normal background when there is none
     */
    private void replaceBackground() {
        EditText editText = getEditText();
        if (editText != null) {

            int backgroundNormal = R.drawable.edittext_background;
            int backgroundError = R.drawable.edittext_background_error;

            editText.setBackgroundResource(isErrorEnabled() ? backgroundError : backgroundNormal);

            Drawable drawable = editText.getBackground();
            if (drawable != null) {
                drawable.clearColorFilter();
            }
        }
    }
}

