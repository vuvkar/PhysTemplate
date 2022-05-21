package com.phys.template.input;

public interface KeyboardFeedbackInterface {

    void completedMessage(String message);
    String getText();
    KeyboardType getType();
    boolean shouldShowCurrentText();
}
