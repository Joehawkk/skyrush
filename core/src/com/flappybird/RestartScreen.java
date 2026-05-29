package com.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class RestartScreen implements Screen {

    private static final float BTN_W = 200f;
    private static final float BTN_H = 60f;

    private static final Color GOLD = new Color(1f, 0.85f, 0f,   1f);
    private static final Color GREY = new Color(0.75f, 0.75f, 0.75f, 1f);

    private final FlappyBirdGame     game;
    private final int                score;
    private final boolean            newBest;
    private final SpriteBatch        batch;
    private final FitViewport        viewport;
    private final OrthographicCamera camera;
    private final BitmapFont         font;
    private final GlyphLayout        layout;
    private final Rectangle          playAgainRect;
    private final Rectangle          menuRect;
    private final String             scoreText;
    private final String             bestText;
    private final Vector2            touchVec;

    public RestartScreen(FlappyBirdGame game, int score) {
        this.game  = game;
        this.score = score;

        int prevBest = FlappyBirdGame.highScore;
        FlappyBirdGame.updateHighScore(score);
        this.newBest = (score > prevBest && score > 0);

        batch    = new SpriteBatch();
        camera   = new OrthographicCamera();
        viewport = new FitViewport(FlappyBirdGame.WORLD_WIDTH, FlappyBirdGame.WORLD_HEIGHT, camera);
        font     = new BitmapFont();
        layout   = new GlyphLayout();
        font.getData().setScale(2f);

        float btnX    = (FlappyBirdGame.WORLD_WIDTH - BTN_W) / 2f;
        playAgainRect = new Rectangle(btnX, 310f, BTN_W, BTN_H);
        menuRect      = new Rectangle(btnX, 220f, BTN_W, BTN_H);

        scoreText = "Score: " + score;
        bestText  = "Best:  " + FlappyBirdGame.highScore;
        touchVec  = new Vector2();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(Assets.restartBg, 0f, 0f, FlappyBirdGame.WORLD_WIDTH, FlappyBirdGame.WORLD_HEIGHT);

        font.getData().setScale(3f);
        layout.setText(font, scoreText);
        font.draw(batch, scoreText,
            (FlappyBirdGame.WORLD_WIDTH - layout.width) / 2f, 595f);

        font.getData().setScale(2.2f);
        if (newBest) {
            font.setColor(GOLD);
            String banner = "** NEW BEST! **";
            layout.setText(font, banner);
            font.draw(batch, banner,
                (FlappyBirdGame.WORLD_WIDTH - layout.width) / 2f, 525f);
            font.setColor(Color.WHITE);
        }

        font.getData().setScale(2f);
        font.setColor(GREY);
        layout.setText(font, bestText);
        font.draw(batch, bestText,
            (FlappyBirdGame.WORLD_WIDTH - layout.width) / 2f, 465f);
        font.setColor(Color.WHITE);

        batch.draw(Assets.buttonBg, playAgainRect.x, playAgainRect.y, playAgainRect.width, playAgainRect.height);
        drawCenteredText("Play Again", playAgainRect);

        batch.draw(Assets.buttonBg, menuRect.x, menuRect.y, menuRect.width, menuRect.height);
        drawCenteredText("Menu", menuRect);

        batch.end();

        if (Gdx.input.justTouched()) {
            touchVec.set(Gdx.input.getX(), Gdx.input.getY());
            Vector2 touch = viewport.unproject(touchVec);
            if (playAgainRect.contains(touch)) {
                game.setScreen(new GameScreen(game));
            } else if (menuRect.contains(touch)) {
                game.setScreen(new MenuScreen(game));
            }
        }
    }

    private void drawCenteredText(String text, Rectangle rect) {
        font.getData().setScale(2f);
        layout.setText(font, text);
        font.draw(batch, text,
            rect.x + (rect.width  - layout.width)  / 2f,
            rect.y + (rect.height + layout.height) / 2f);
    }

    @Override
    public void resize(int width, int height) { viewport.update(width, height, true); }

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
