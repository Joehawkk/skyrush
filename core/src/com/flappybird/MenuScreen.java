package com.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuScreen implements Screen {

    private static final float BTN_W = 200f;
    private static final float BTN_H = 60f;

    private final FlappyBirdGame     game;
    private final SpriteBatch        batch;
    private final FitViewport        viewport;
    private final OrthographicCamera camera;
    private final BitmapFont         font;
    private final GlyphLayout        layout;
    private final Rectangle          playRect;
    private final Rectangle          exitRect;
    private final Vector2            touchVec;

    public MenuScreen(FlappyBirdGame game) {
        this.game = game;
        batch     = new SpriteBatch();
        camera    = new OrthographicCamera();
        viewport  = new FitViewport(FlappyBirdGame.WORLD_WIDTH, FlappyBirdGame.WORLD_HEIGHT, camera);
        font      = new BitmapFont();
        layout    = new GlyphLayout();
        font.getData().setScale(2f);

        float btnX = (FlappyBirdGame.WORLD_WIDTH - BTN_W) / 2f;
        playRect = new Rectangle(btnX, 370f, BTN_W, BTN_H);
        exitRect = new Rectangle(btnX, 280f, BTN_W, BTN_H);
        touchVec = new Vector2();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(Assets.gameBg, 0f, 0f, FlappyBirdGame.WORLD_WIDTH, FlappyBirdGame.WORLD_HEIGHT);

        batch.draw(Assets.buttonBg, playRect.x, playRect.y, playRect.width, playRect.height);
        drawCenteredText("Play", playRect);

        batch.draw(Assets.buttonBg, exitRect.x, exitRect.y, exitRect.width, exitRect.height);
        drawCenteredText("Exit", exitRect);

        batch.end();

        if (Gdx.input.justTouched()) {
            touchVec.set(Gdx.input.getX(), Gdx.input.getY());
            Vector2 touch = viewport.unproject(touchVec);
            if (playRect.contains(touch)) {
                game.setScreen(new GameScreen(game));
            } else if (exitRect.contains(touch)) {
                Gdx.app.exit();
            }
        }
    }

    private void drawCenteredText(String text, Rectangle rect) {
        layout.setText(font, text);
        font.draw(batch, text,
            rect.x + (rect.width  - layout.width)  / 2f,
            rect.y + (rect.height + layout.height) / 2f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void show()   {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
