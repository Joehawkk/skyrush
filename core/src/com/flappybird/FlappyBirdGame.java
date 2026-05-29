package com.flappybird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class FlappyBirdGame extends Game {
    public static final float WORLD_WIDTH  = 480f;
    public static final float WORLD_HEIGHT = 800f;

    private static final String PREFS_NAME = "FlappyBirdPrefs";
    private static final String KEY_HIGH   = "highScore";

    public static int highScore = 0;

    @Override
    public void create() {
        Assets.load();
        highScore = Gdx.app.getPreferences(PREFS_NAME).getInteger(KEY_HIGH, 0);
        setScreen(new MenuScreen(this));
    }

    // сохраняет рекорд, если побит
    public static void updateHighScore(int score) {
        if (score > highScore) {
            highScore = score;
            Gdx.app.getPreferences(PREFS_NAME).putInteger(KEY_HIGH, score).flush();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
    }
}
