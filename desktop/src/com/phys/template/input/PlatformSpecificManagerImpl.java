package com.phys.template.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.phys.template.PhysTemplate;
import com.phys.template.desktop.DesktopLauncher;
import com.phys.template.models.Metadata;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlatformSpecificManagerImpl implements PlatformSpecificManager<DesktopLauncher> {
    FileChooser fileChooser;
    Stage stage;

    private boolean isInit = false;

    @Override
    public void showKeyboard (KeyboardFeedbackInterface textField) {

    }

    @Override
    public void hideKeyboard () {

    }

    @Override
    public void onPause () {

    }

    @Override
    public void onResume () {

    }

    @Override
    public boolean isKeyboardShown () {
        return false;
    }

    private void checkInit() {
        if (!isInit) {
            stage = PhysTemplate.Instance().UIStage().getStage();
            fileChooser = new FileChooser(FileChooser.Mode.SAVE);
            fileChooser.setSize(1600, 900);
            isInit = true;
        }
    }

    @Override
    public void openProject() {
        checkInit();
        fileChooser.setMode(FileChooser.Mode.OPEN);
        fileChooser.setMultiSelectionEnabled(false);
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        fileChooser.setDirectory(desktopPath);

        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getAbsolutePath().endsWith("json");
            }
        });
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                String path = file.first().file().getAbsolutePath();
                PhysTemplate.Instance().ProjectController().loadProject(Gdx.files.absolute(path));
            }
        });

        stage.addActor(fileChooser.fadeIn());
    }

    @Override
    public void saveWord() {
        checkInit();
        final String ext = "docx";
        fileChooser.setMode(FileChooser.Mode.SAVE);
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        fileChooser.setDirectory(desktopPath);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getAbsolutePath().endsWith(ext);
            }
        });
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                File selectedFile = file.first().file();
                boolean isFileUnlocked;

                String path = selectedFile.getAbsolutePath();
                if (!path.endsWith(ext)) {
                    if (path.indexOf(".") > 0) {
                        path = path.substring(0, path.indexOf("."));
                    }
                    path += ext;
                }

                File toBeSaved = new File(path);
                FileOutputStream fos = null;
                try {
                    // Make sure that the output stream is in Append mode. Otherwise you will
                    // truncate your file, which probably isn't what you want to do :-)
                    fos = new FileOutputStream(toBeSaved, true);
                    // -> file was closed
                    isFileUnlocked = true;
                } catch(IOException e) {
                    // -> file still open
                    isFileUnlocked = false;
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (isFileUnlocked) {
                    FileHandle handle = Gdx.files.absolute(path);
                    try {
                        XWPFDocument documentForProject = PhysTemplate.Instance().DocumentController().createDocumentForProject(PhysTemplate.Instance().ProjectController().getCurrentProject());

                        //Write the Document in file system
                        FileOutputStream out = new FileOutputStream(handle.path());

                        documentForProject.write(out);
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Dialogs.showErrorDialog(stage, "Փակեք word Ֆայլը և կրկին փորձեք");
                }
            }
        });

        Metadata metadata = PhysTemplate.Instance().ProjectController().getCurrentProject().getMetadata();
        String squadName = metadata.getSquadName();
        squadName = squadName.replace(" ", "_");
        fileChooser.setDefaultFileName(squadName + "_ֆիզոյի_ամփոփագիր");

        stage.addActor(fileChooser.fadeIn());
    }

    @Override
    public void saveProject() {
        checkInit();
        final String ext = ".json";
        fileChooser.setMode(FileChooser.Mode.SAVE);
        fileChooser.setMultiSelectionEnabled(false);
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        fileChooser.setDirectory(desktopPath);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getAbsolutePath().endsWith(ext);
            }
        });
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);

        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                String path = file.first().file().getAbsolutePath();
                if (!path.endsWith(ext)) {
                    if (path.indexOf(".") > 0) {
                        path = path.substring(0, path.indexOf("."));
                    }
                    path += ext;
                }
                FileHandle handle = Gdx.files.absolute(path);
                // TODO: 1/6/2022 save on a file
                PhysTemplate.Instance().ProjectController().saveProject(handle);
            }
        });

        Metadata metadata = PhysTemplate.Instance().ProjectController().getCurrentProject().getMetadata();
        String squadName = metadata.getSquadName();
        squadName = squadName.replace(" ", "_");
        fileChooser.setDefaultFileName(squadName + "_ամփոփագիր");

        stage.addActor(fileChooser.fadeIn());
    }

    @Override
    public void inject (DesktopLauncher launcher) {

    }

    @Override
    public void dispose () {

    }
}
