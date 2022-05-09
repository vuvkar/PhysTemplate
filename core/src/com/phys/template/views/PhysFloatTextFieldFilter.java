package com.phys.template.views;

import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
import com.kotcrab.vis.ui.widget.VisTextField;

public class PhysFloatTextFieldFilter extends IntDigitsOnlyFilter {
    public PhysFloatTextFieldFilter() {
        super(false);
    }

    @Override
    public boolean acceptChar (VisTextField field, char c) {
        int selectionStart = field.getSelectionStart();
        int cursorPos = field.getCursorPosition();
        String text;
        if (field.isTextSelected()) { //issue #131
            String beforeSelection = field.getText().substring(0, Math.min(selectionStart, cursorPos));
            String afterSelection = field.getText().substring(Math.max(selectionStart, cursorPos));
            text = beforeSelection + afterSelection;
        } else {
            text = field.getText();
        }
        if (c == '․') {
            c = '.';
        }

        if (c == '.' && !text.contains(".")) return true;
        return super.acceptChar(field, c);
    }
}
