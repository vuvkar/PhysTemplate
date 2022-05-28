package com.phys.template.input;


import com.phys.template.LauncherInjectable;

public interface PlatformSpecificManager<T> extends LauncherInjectable<T> {

    /**
     * Request the keyboard for the specific TextField/TextArea
     */
    void showKeyboard(KeyboardFeedbackInterface keyboardFeedbackInterface);

    /**
     * Force hide the keyboard, e.g. when the user unfocuses the TextField
     */
    void hideKeyboard();

    void onPause();

    void onResume();

    /**
     * @return whether keyboard is shown
     */
    boolean isKeyboardShown();

    void openProject();

    void saveWord();

    void saveProject();
}
