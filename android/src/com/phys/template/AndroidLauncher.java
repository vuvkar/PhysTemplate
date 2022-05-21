package com.phys.template;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.phys.template.input.KeyboardHandlerImpl;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		KeyboardHandlerImpl platformSpecificTools = new KeyboardHandlerImpl();
		platformSpecificTools.inject(this);
		initialize(new PhysTemplate(platformSpecificTools), config);
	}
}
