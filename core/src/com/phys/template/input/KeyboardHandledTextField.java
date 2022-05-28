package com.phys.template.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTextField;

public class KeyboardHandledTextField extends VisTextField {

    private final CustomOnScreenKeyboard keyboard;
    private TextCompleteListener completeListener;
    private boolean shouldShowCurrentText = true;


    public KeyboardHandledTextField (String text, PlatformSpecificManager<?> keyboardHandler, final KeyboardType type) {
        super(text);

        KeyboardFeedbackInterface keyboardFeedbackInterface = new KeyboardFeedbackInterface() {
            @Override
            public void completedMessage (String message) {
                setText(message);

                if (completeListener != null) completeListener.onComplete();
            }

            @Override
            public String getText () {
                return KeyboardHandledTextField.this.getText();
            }

            @Override
            public KeyboardType getType () {
                return type;
            }

            @Override
            public boolean shouldShowCurrentText () {
                return shouldShowCurrentText;
            }
        };

        this.setFocusTraversal(false);
        this.setTextFieldListener(new VisTextField.TextFieldListener() {
            @Override
            public void keyTyped (VisTextField textField, char c) {
                if (c == '․') {
                    int cursorPosition = textField.getCursorPosition();
                    String replace = textField.getText().replace('․', '.');
                    textField.setText(replace);
                    textField.setCursorPosition(cursorPosition);
                }

                boolean isMobile = Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
                if ((c == '\r' || c == '\n') && isMobile) {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                }
            }
        });

        boolean isDesktop = Gdx.app.getType() == Application.ApplicationType.Desktop;
        if (isDesktop) { //no keyboard on desktop, so listen to on change
            addListener(new ChangeListener() {
                @Override
                public void changed (ChangeEvent changeEvent, Actor actor) {
                    if (completeListener != null) completeListener.onComplete();
                }
            });
        }

        keyboard = new CustomOnScreenKeyboard(keyboardHandler, keyboardFeedbackInterface);
        setOnscreenKeyboard(keyboard);
    }
}
