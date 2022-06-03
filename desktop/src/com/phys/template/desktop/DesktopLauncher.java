package com.phys.template.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.phys.template.PhysTemplate;
import com.phys.template.input.PlatformSpecificManagerImpl;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration.disableAudio = true;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.samples = 3;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.width = 1280;
		config.height = 720;
		config.x = (screenSize.width - config.width) / 2;
		config.y = (screenSize.height - config.height) / 2;
		config.resizable = false;
		config.addIcon("icon-16.png", Files.FileType.Internal);
		config.addIcon("icon-32.png", Files.FileType.Internal);
		new LwjglApplication(new PhysTemplate(new PlatformSpecificManagerImpl()), config);
	}
}
