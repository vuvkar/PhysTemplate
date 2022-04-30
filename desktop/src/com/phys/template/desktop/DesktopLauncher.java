package com.phys.template.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.phys.template.PhysTemplate;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration.disableAudio = true;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.width = 1366;
		config.height = 768;
		config.x = (screenSize.width - 1366) / 2;
		config.y = (screenSize.height - 768) / 2;
		config.resizable = false;
		new LwjglApplication(new PhysTemplate(), config);
	}
}
