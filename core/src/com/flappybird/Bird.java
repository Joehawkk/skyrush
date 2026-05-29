package com.flappybird;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bird {
    public static final float WIDTH  = 48f;
    public static final float HEIGHT = 34f;

    private static final float GRAVITY       = -1200f;
    private static final float JUMP_VELOCITY = 450f;
    private static final float HITBOX_INSET  = 0.1f;

    private float x, y;
    private float velocityY;
    private float stateTime;
    private final Rectangle bounds;

    public Bird(float x, float y) {
        this.x        = x;
        this.y        = y;
        this.velocityY = 0f;
        this.stateTime = 0f;
        this.bounds    = new Rectangle();
        updateBounds();
    }

    public void update(float delta, boolean jumpPressed) {
        if (jumpPressed) {
            velocityY = JUMP_VELOCITY;
        }
        velocityY += GRAVITY * delta;
        y         += velocityY * delta;
        stateTime += delta;
        updateBounds();
    }

    private void updateBounds() {
        float insetX = WIDTH  * HITBOX_INSET;
        float insetY = HEIGHT * HITBOX_INSET;
        bounds.set(x + insetX, y + insetY, WIDTH - insetX * 2, HEIGHT - insetY * 2);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frame = Assets.birdAnimation.getKeyFrame(stateTime);
        // наклон: +25° при прыжке, -90° при падении
        float angle = Math.max(-90f, Math.min(25f, (velocityY / JUMP_VELOCITY) * 25f));
        batch.draw(frame, x, y, WIDTH / 2f, HEIGHT / 2f, WIDTH, HEIGHT, 1f, 1f, angle);
    }

    public boolean isOutOfBounds() {
        return y + HEIGHT < 0 || y > FlappyBirdGame.WORLD_HEIGHT;
    }

    public Rectangle getBounds() { return bounds; }
    public float getX()          { return x; }
    public float getY()          { return y; }
}
