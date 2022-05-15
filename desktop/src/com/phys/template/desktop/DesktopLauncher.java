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
		config.width = 1600;
		config.height = 900;
		config.x = (screenSize.width - config.width) / 2;
		config.y = (screenSize.height - config.height) / 2;
		config.resizable = false;
		new LwjglApplication(new PhysTemplate(), config);
	}
}
