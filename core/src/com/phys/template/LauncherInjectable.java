package com.phys.template;

public interface LauncherInjectable<T> {

	void inject(T launcher);

	void dispose();

}
