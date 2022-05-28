package com.phys.template.input;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.badlogic.gdx.Gdx;
import com.phys.template.AndroidLauncher;
import com.phys.template.R;

import static android.app.Activity.RESULT_OK;

public class PlatformSpecificManagerImpl implements PlatformSpecificManager<AndroidLauncher> {

    private Activity activity;


    private KeyboardFeedbackInterface keyboardFeedbackTextField;

    private View keyboardView;
    private CustomEditTextView textBox;

    private volatile boolean keyboardShown;

    private KeyboardHeightProvider keyboardHeightProvider;
    private KeyboardHeightProvider.KeyboardHeightObserver observer;

    public void init (Activity activity) {
        this.activity = activity;


        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        keyboardView = inflater.inflate(R.layout.keyboard, null);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        keyboardView.setLayoutParams(params);

        this.textBox = keyboardView.findViewById(R.id.keyboardTextBox);

        textBox.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textBox.setBackPressedListener(new BackPressedListener() {
            @Override
            public void onBackPressed (CustomEditTextView customEditTextView) {
                hideKeyboard();
            }
        });

        textBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        textBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        keyboardHeightProvider = new KeyboardHeightProvider(activity);
        observer = new KeyboardHeightProvider.KeyboardHeightObserver() {
            @Override
            public void onKeyboardHeightChanged (int height, int orientation) {

                if (height > 0) {
                    View view = keyboardView.findViewById(R.id.keyboard);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    params.height = height;
                    view.setLayoutParams(params);
                }
            }
        };
        keyboardHeightProvider.setKeyboardHeightObserver(observer);


        keyboardHeightProvider.start();
    }

    @Override
    public void inject (AndroidLauncher launcher) {
        init(launcher);
    }

    @Override
    public void showKeyboard (KeyboardFeedbackInterface textFieldListener) {
        //allow the TextField to configure itself
        this.keyboardFeedbackTextField = null;
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run () {
                requestKeyboard(textFieldListener, textFieldListener.getText(), textFieldListener.shouldShowCurrentText());
            }
        });
    }

    private void requestKeyboard (final KeyboardFeedbackInterface textFieldListener, final CharSequence currentText, boolean shouldShowCurrentText) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run () {

                if (!keyboardShown) {
                    keyboardShown = true;
                    activity.addContentView(keyboardView, keyboardView.getLayoutParams());
                }

                textBox.setRawInputType(InputType.TYPE_CLASS_TEXT);

                KeyboardType style = textFieldListener.getType();
                int keyboardType = style.equals(KeyboardType.NUMERIC) ? InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL : InputType.TYPE_CLASS_TEXT;

                textBox.setInputType(keyboardType);
                if (shouldShowCurrentText) {
                    textBox.setText(currentText);
                } else {
                    textBox.setText("");
                }
                textBox.setSelection(textBox.getText().length());

                textBox.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);

                textBox.requestFocus();

                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(textBox, InputMethodManager.SHOW_FORCED);
            }
        });
        this.keyboardFeedbackTextField = textFieldListener;
    }

    @Override
    public void hideKeyboard () {

        final String keyboardText = textBox.getText().toString();

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run () {
                keyboardFeedbackTextField.completedMessage(keyboardText);
                keyboardFeedbackTextField = null;
            }
        });

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run () {

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(keyboardView.getWindowToken(), 0);

                ((ViewGroup) keyboardView.getParent()).removeView(keyboardView);

                textBox.setSelection(0);
                textBox.setText("");
                textBox.clearFocus();

                keyboardShown = false;
            }
        });

    }

    @Override
    public void onPause () {
        keyboardHeightProvider.setKeyboardHeightObserver(null);
    }

    @Override
    public void onResume () {
        keyboardHeightProvider.setKeyboardHeightObserver(observer);
    }

    @Override
    public boolean isKeyboardShown () {
        return keyboardShown;
    }

    @Override
    public void openProject() {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/json");
        intent = Intent.createChooser(chooseFile, "Choose a file");

        activity.startActivityForResult(intent, 0);
    }



    @Override
    public void saveWord() {

    }

    @Override
    public void saveProject() {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/json");
        intent = Intent.createChooser(chooseFile, "Save project");

        activity.startActivityForResult(intent, 2);
    }

    @Override
    public void dispose () {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run () {
                keyboardHeightProvider.close();
            }
        });
        keyboardFeedbackTextField = null;
        activity = null;
        textBox = null;
        keyboardShown = false;
    }
}
