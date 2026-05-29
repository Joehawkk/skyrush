package com.flappybird;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
    public static Texture gameBg;
    public static Texture restartBg;

    public static Texture bird0, bird1, bird2;
    public static Animation<TextureRegion> birdAnimation;

    public static Texture tubeBottom;
    public static Texture tubeTop;

    public static Texture buttonBg;

    public static void load() {
        gameBg     = new Texture("background/game_bg.png");
        restartBg  = new Texture("background/restart_bg.png");
        bird0      = new Texture("bird/bird0.png");
        bird1      = new Texture("bird/bird1.png");
        bird2      = new Texture("bird/bird2.png");
        tubeBottom = new Texture("tube/tube.png");
        tubeTop    = new Texture("tube/tube_flipped.png");
        buttonBg   = new Texture("button/button_bg.png");

        TextureRegion[] frames = {
            new TextureRegion(bird0),
            new TextureRegion(bird1),
            new TextureRegion(bird2)
        };
        birdAnimation = new Animation<>(0.1f, frames);
        birdAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public static void dispose() {
        if (gameBg    != null) { gameBg.dispose();    gameBg    = null; }
        if (restartBg != null) { restartBg.dispose(); restartBg = null; }
        if (bird0     != null) { bird0.dispose();     bird0     = null; }
        if (bird1     != null) { bird1.dispose();     bird1     = null; }
        if (bird2     != null) { bird2.dispose();     bird2     = null; }
        if (tubeBottom != null){ tubeBottom.dispose();tubeBottom = null; }
        if (tubeTop   != null) { tubeTop.dispose();   tubeTop   = null; }
        if (buttonBg  != null) { buttonBg.dispose();  buttonBg  = null; }
    }
}
