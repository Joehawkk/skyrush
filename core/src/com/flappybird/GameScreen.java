package com.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private static final float BG_SPEED        = -80f;
    private static final float PIPE_SPAWN_DIST = 280f;

    private static final float BASE_SPEED      = 200f;
    private static final float SPEED_INCREMENT = 25f;
    private static final float MAX_SPEED       = 420f;
    private static final float BASE_GAP        = 160f;
    private static final float GAP_DECREMENT   = 8f;
    private static final float MIN_GAP         = 100f;
    private static final int   MILESTONE       = 5;

    private static final String[] DIFF_LABELS = { "EASY", "NORMAL", "HARD", "EXTREME", "INSANE" };
    private static final Color[]  DIFF_COLORS = {
        new Color(0.30f, 1.00f, 0.30f, 1f),
        new Color(1.00f, 1.00f, 0.30f, 1f),
        new Color(1.00f, 0.55f, 0.10f, 1f),
        new Color(1.00f, 0.20f, 0.20f, 1f),
        new Color(0.75f, 0.00f, 1.00f, 1f),
    };

    private final FlappyBirdGame     game;
    private final SpriteBatch        batch;
    private final FitViewport        viewport;
    private final OrthographicCamera camera;
    private final BitmapFont         font;
    private final GlyphLayout        layout;

    private final Bird        bird;
    private final Array<Pipe> pipes;
    private int   score;
    private int   lastMilestone;

    private float bg1X, bg2X;
    private float pipeSpawnTimer;
    private int   pipeCount;

    private float currentSpeed;
    private float currentGap;

    public GameScreen(FlappyBirdGame game) {
        this.game  = game;
        batch      = new SpriteBatch();
        camera     = new OrthographicCamera();
        viewport   = new FitViewport(FlappyBirdGame.WORLD_WIDTH, FlappyBirdGame.WORLD_HEIGHT, camera);
        font       = new BitmapFont();
        layout     = new GlyphLayout();
        font.getData().setScale(3f);

        bird          = new Bird(100f, FlappyBirdGame.WORLD_HEIGHT / 2f);
        pipes         = new Array<>();
        score         = 0;
        lastMilestone = 0;
        bg1X          = 0f;
        bg2X          = FlappyBirdGame.WORLD_WIDTH;
        pipeSpawnTimer = 0f;
        pipeCount     = 0;
        currentSpeed  = BASE_SPEED;
        currentGap    = BASE_GAP;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private float pipeSpawnTime() { return PIPE_SPAWN_DIST / currentSpeed; }
    private float gapCenterMin()  { return currentGap / 2f + 80f; }
    private float gapCenterMax()  { return FlappyBirdGame.WORLD_HEIGHT - currentGap / 2f - 80f; }
    private int   diffIndex()     { return Math.min(DIFF_LABELS.length - 1, score / MILESTONE); }

    private void update(float delta) {
        bg1X += BG_SPEED * delta;
        bg2X += BG_SPEED * delta;
        if (bg1X + FlappyBirdGame.WORLD_WIDTH <= 0) bg1X = bg2X + FlappyBirdGame.WORLD_WIDTH;
        if (bg2X + FlappyBirdGame.WORLD_WIDTH <= 0) bg2X = bg1X + FlappyBirdGame.WORLD_WIDTH;

        bird.update(delta, Gdx.input.justTouched());

        pipeSpawnTimer += delta;
        if (pipeSpawnTimer >= pipeSpawnTime()) {
            pipeSpawnTimer -= pipeSpawnTime();
            float gapCenter  = MathUtils.random(gapCenterMin(), gapCenterMax());
            boolean oscillates = (pipeCount % 3 == 2); // каждая третья пара качается
            pipes.add(new Pipe(FlappyBirdGame.WORLD_WIDTH, gapCenter, -currentSpeed, currentGap, oscillates));
            pipeCount++;
        }

        for (int i = pipes.size - 1; i >= 0; i--) {
            Pipe pipe = pipes.get(i);
            pipe.update(delta);

            if (!pipe.isScored() && bird.getX() > pipe.getX() + Pipe.WIDTH) {
                pipe.markScored();
                score++;
                checkDifficulty();
            }

            if (pipe.checkCollision(bird.getBounds())) {
                game.setScreen(new RestartScreen(game, score));
                return;
            }

            if (pipe.isOffScreen()) {
                pipes.removeIndex(i);
            }
        }

        if (bird.isOutOfBounds()) {
            game.setScreen(new RestartScreen(game, score));
            return;
        }
    }

    private void checkDifficulty() {
        int milestone = score / MILESTONE;
        if (milestone > lastMilestone) {
            lastMilestone = milestone;
            currentSpeed = Math.min(MAX_SPEED, currentSpeed + SPEED_INCREMENT);
            currentGap   = Math.max(MIN_GAP,   currentGap   - GAP_DECREMENT);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(Assets.gameBg, bg1X, 0f, FlappyBirdGame.WORLD_WIDTH, FlappyBirdGame.WORLD_HEIGHT);
        batch.draw(Assets.gameBg, bg2X, 0f, FlappyBirdGame.WORLD_WIDTH, FlappyBirdGame.WORLD_HEIGHT);

        for (Pipe pipe : pipes) pipe.draw(batch);

        bird.draw(batch);

        String scoreStr = String.valueOf(score);
        layout.setText(font, scoreStr);
        font.draw(batch, scoreStr,
            (FlappyBirdGame.WORLD_WIDTH - layout.width) / 2f,
            FlappyBirdGame.WORLD_HEIGHT - 28f);

        font.getData().setScale(1.5f);
        String diffStr = DIFF_LABELS[diffIndex()];
        font.setColor(DIFF_COLORS[diffIndex()]);
        layout.setText(font, diffStr);
        font.draw(batch, diffStr,
            FlappyBirdGame.WORLD_WIDTH - layout.width - 12f,
            FlappyBirdGame.WORLD_HEIGHT - 32f);
        font.setColor(Color.WHITE);
        font.getData().setScale(3f);

        batch.end();
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
