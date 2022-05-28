package com.phys.template.views.exerciseWidgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.util.IntDigitsOnlyFilter;
import com.kotcrab.vis.ui.util.NumberDigitsTextFieldFilter;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.phys.template.PhysTemplate;
import com.phys.template.models.Exercise;
import com.phys.template.input.KeyboardHandledTextField;
import com.phys.template.input.KeyboardType;
import com.phys.template.views.PhysFloatTextFieldFilter;

import java.text.NumberFormat;
import java.text.ParseException;

import static com.phys.template.views.peopleWidgets.EditPersonPopup.FIELD_HEIGHT;

public class ExerciseValueFillRow extends Table {

    private VisLabel exerciseName;
    private KeyboardHandledTextField exerciseField;
    private NumberDigitsTextFieldFilter filter;

    public ExerciseValueFillRow(final Exercise exercise) {
        pad(3);
        defaults().pad(3);
        exerciseField = new KeyboardHandledTextField("", PhysTemplate.Instance().UIStage().getPlatformSpecificManager(), KeyboardType.NUMERIC);
        exerciseName = new VisLabel(exercise.name + ", " + exercise.longName);
        add(exerciseName).left();
        row();
        filter = PhysTemplate.Instance().DataController().isFloatExercise(exercise.number) ? new PhysFloatTextFieldFilter() : new IntDigitsOnlyFilter(false);
        exerciseField.setTextFieldFilter(filter);
        add(exerciseField).height(FIELD_HEIGHT).growX();
        add(new VisLabel(exercise.getUnit()));
    }

    public void setValue(Number number) {
        if (number == null) {
            exerciseField.clearText();
            return;
        }
        exerciseField.setText(number.toString());
    }

    public Number getNumber() {
        String text = exerciseField.getText();
        if (text.isEmpty()) {
            return null;
        }
        try {
            return NumberFormat.getInstance().parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAvailable(boolean isAvailable, boolean isAllowed) {
        if (!isAllowed) {
            Color color = Color.PURPLE;
            exerciseName.setColor(color);
            exerciseField.setDisabled(true);
            exerciseField.clearText();
            return;
        }

        if (!isAvailable) {
            Color color = Color.RED;
            exerciseName.setColor(color);
            exerciseField.setDisabled(true);
            exerciseField.clearText();
            return;
        }

        exerciseName.setColor(Color.WHITE);
        exerciseField.setDisabled(false);
    }
}
