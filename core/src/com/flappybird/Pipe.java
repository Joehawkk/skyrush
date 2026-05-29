package com.flappybird;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Pipe {
    public static final float WIDTH = 64f;
    public static final float GAP   = 160f;
    public static final float SPEED = -200f;

    private static final float OSCILLATE_AMPLITUDE = 70f;
    private static final float OSCILLATE_SPEED_RAD = 1.8f;

    private float x;
    private final float   baseGapCenterY;
    private final float   speed;
    private final float   gap;
    private final boolean oscillating;

    private float oscillateTimer;
    private float effectiveCenterY;

    private float     gapBottom;
    private float     gapTop;
    private boolean   scored;

    private final Rectangle bottomBounds;
    private final Rectangle topBounds;

    public Pipe(float x, float gapCenterY, float speed, float gap, boolean oscillating) {
        this.x              = x;
        this.baseGapCenterY = gapCenterY;
        this.speed          = speed;
        this.gap            = gap;
        this.oscillating    = oscillating;
        this.scored         = false;
        this.oscillateTimer = 0f;
        this.effectiveCenterY = gapCenterY;
        this.bottomBounds   = new Rectangle();
        this.topBounds      = new Rectangle();
        updateBounds();
    }

    public Pipe(float x, float gapCenterY) {
        this(x, gapCenterY, SPEED, GAP, false);
    }

    public void update(float delta) {
        x += speed * delta;

        if (oscillating) {
            oscillateTimer += delta;
            float offset = MathUtils.sin(oscillateTimer * OSCILLATE_SPEED_RAD) * OSCILLATE_AMPLITUDE;
            effectiveCenterY = MathUtils.clamp(
                baseGapCenterY + offset,
                gap / 2f + 40f,
                FlappyBirdGame.WORLD_HEIGHT - gap / 2f - 40f
            );
        }

        updateBounds();
    }

    private void updateBounds() {
        gapBottom = effectiveCenterY - gap / 2f;
        gapTop    = effectiveCenterY + gap / 2f;
        bottomBounds.set(x, 0f,     WIDTH, gapBottom);
        topBounds.set   (x, gapTop, WIDTH, FlappyBirdGame.WORLD_HEIGHT - gapTop);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(Assets.tubeBottom, x, 0f,     WIDTH, gapBottom);
        batch.draw(Assets.tubeTop,    x, gapTop, WIDTH, FlappyBirdGame.WORLD_HEIGHT - gapTop);
    }

    public boolean checkCollision(Rectangle birdBounds) {
        return Intersector.overlaps(birdBounds, bottomBounds)
            || Intersector.overlaps(birdBounds, topBounds);
    }

    public boolean isOffScreen() { return x + WIDTH < 0; }

    public float   getX()       { return x; }
    public boolean isScored()   { return scored; }
    public void    markScored() { this.scored = true; }
}
