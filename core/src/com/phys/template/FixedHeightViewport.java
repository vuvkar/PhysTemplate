package com.phys.template;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FixedHeightViewport extends Viewport {

    public static int notchSize;
    public static boolean isDebug = true;
    private final float worldHeight;

    public static float debugScalingFactor = 1f;

    public static Integer fakeDPI;

    public FixedHeightViewport(float worldHeight, OrthographicCamera camera) {
        this.worldHeight = worldHeight;
        setCamera(camera);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        float aspect = (float) screenWidth / screenHeight;

        float scalingHeight = getScalingHeight();

        float widthForScreen = scalingHeight * aspect;
        setWorldSize(widthForScreen, scalingHeight);

        setScreenBounds(0, 0, screenWidth, screenHeight);

        apply(centerCamera);
    }

    private static float getDeviceHeight() {
        float height = Gdx.graphics.getHeight();

        if (isDebug) {
            height *= debugScalingFactor;
        }

        return height;
    }


    private static float getScalingHeight() {
        float diagonal = getDeviceScreenSizeInchesDiagonal();
        if (diagonal >= 7f && getDeviceHeight() > 1080) {
            int delta = (int) (getDeviceHeight() - 1080);

            // 0.555 for 7.0 inch
            // 0.600 for 7.9 inch
            // 0.850 for 12.9 inch
            float scalingPercent = 0.05f * diagonal + 0.205f;
            //Magic scaling factor <- So we dont scale 1:1 with the resolution increase. So if we double the resolution in height, the stage
            //Only gets 85% larger, not 100%;

            delta *= scalingPercent;

            return 1080 + delta;
        } else {
            return 1080;
        }
    }

    public static float getDeviceScreenSizeInchesDiagonal() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        if (isDebug) {
            width *= debugScalingFactor;
        }

        float inchesw = width / Gdx.graphics.getPpiX();
        float inchesh = height / Gdx.graphics.getPpiY();

        if (isDebug) {
            float scalingHeight = getDeviceHeight();
            float aspect = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
            float widthForScreen = scalingHeight * aspect;

            if (fakeDPI != null) {
                inchesw = widthForScreen / fakeDPI;
                inchesh = scalingHeight / fakeDPI;
            }
        }

        return (float) Math.sqrt(inchesh * inchesh + inchesw * inchesw);
    }
}


