package com.phys.template.input;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class CustomEditTextView extends EditText {

    private BackPressedListener backPressedListener;

    public CustomEditTextView (Context context) {
        super(context);
    }

    public CustomEditTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditTextView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (backPressedListener != null) {
                backPressedListener.onBackPressed(this);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void setBackPressedListener (BackPressedListener listener) {
        backPressedListener = listener;
    }

}
