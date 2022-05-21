package com.phys.template.input;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class CustomOnScreenKeyboard implements TextField.OnscreenKeyboard {

	private IKeyboardHandler keyboardHandler;
	private KeyboardFeedbackInterface keyboardFeedbackInterface;

	public CustomOnScreenKeyboard (IKeyboardHandler keyboardHandler, KeyboardFeedbackInterface keyboardFeedbackInterface) {
		this.keyboardHandler = keyboardHandler;
		this.keyboardFeedbackInterface = keyboardFeedbackInterface;
	}

	@Override
	public void show (boolean visible) {
		if (visible) {
			keyboardHandler.showKeyboard(keyboardFeedbackInterface);
		} else {
			keyboardHandler.hideKeyboard();
		}
	}
}
