package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.zsd.voxmania.display.screen.drawables.DrawableSprite;
import com.zsd.voxmania.game.events.types.TargetEvent;

import java.util.concurrent.Callable;

public class Target extends DrawableSprite {
    public GameUI parent;
    public DrawableSprite holdSpr;
    public DrawableSprite clockHand;
    public boolean target = false;
    public boolean hold = false;

    public int type;
    public float angle;
    public float distance;
    public float amplitude;
    public float frequency;
    public float progress = 1;
    public float targetX, targetY;
    public float startX;
    public float startY;
    public float flyingTime;
    public float eventTime;
    public Target parentTarget;
    public TargetEvent event;
    public Callable<Float> getMusTime;

    public Target(int type, boolean isTarget, boolean isSpecial, boolean isHold, float x, float y, GameUI parent, float angle, float distance, float amplitude, float frequency, float targetX, float targetY, TargetEvent event, Target target)
    {
        super(new Texture(Gdx.files.internal("game/notes/" + (isTarget ? "targets/" : "normal/") + type + (isSpecial ? "_special" : "") + ".png")));
        this.target = isTarget;
        this.type = type;
        this.parent = parent;
        this.angle = angle;
        this.distance = distance;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.targetX = targetX;
        this.targetY = targetY;
        this.parentTarget = target;
        this.hold = isHold;

        if (!isTarget)
            parent.targetRenderer.addSprite(this);
        else
            parent.addSprite(this);
        setX(x);
        setY(y);
        scale(-0.35f);

        boolean isChain = type == 15 || type == 16;
        if (isHold && isTarget && !isChain)
        {
            holdSpr = new DrawableSprite(new Texture(Gdx.files.internal("game/notes/hold" + (isSpecial ? "_special" : "_basic") + ".png")), parent);
            holdSpr.setX(x);
            holdSpr.setY(y - 15);
            holdSpr.scale(-0.35f);
            parent.addSprite(holdSpr);
        }
        if (isTarget && !isChain)
        {
            clockHand = new DrawableSprite(new Texture(Gdx.files.internal("game/notes/clock_hand.png")), parent);
            clockHand.setX(x - 25);
            clockHand.setY(y - 25);
            clockHand.setOriginCenter();
            clockHand.scale(-0.35f);
            parent.addSprite(clockHand);
        }

        startX = (float) (targetX - Math.cos(angle) * distance);
        startY = (float) (targetY - Math.sin(angle) * distance);
        if (Math.round(frequency) % 2 != 0)
            this.frequency *= -1;
        this.event = event;
    }

    @Override
    public void update(float delta) {
        try {
            progress = (((float) eventTime + flyingTime) - getMusTime.call()) / flyingTime;
            if (!target) {
                Vector2 point = new Vector2(
                    progress * distance,
                    (float) (Math.sin(progress * Math.PI * frequency) / 12.0 * amplitude)
                );

                Vector2 trig = new Vector2(
                    (float) Math.sin(MathUtils.degRad * (angle - 90.0)),
                    (float) Math.cos(MathUtils.degRad * (angle - 90.0))
                );

                Vector2 rotate = new Vector2(
                    point.x * trig.y - point.y * trig.x,
                    -(point.x * trig.x + point.y * trig.y)
                );

                setX(rotate.x + targetX);
                setY(rotate.y + targetY);
            } else if (clockHand != null) {
                clockHand.setRotation(progress * 360);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose() {
        try {
            if (parent != null) {
                if (!target)
                    parent.targetRenderer.queueDispose(this);
                else {
                    if (holdSpr != null)
                        parent.queueDispose(holdSpr);
                    if (clockHand != null)
                        parent.queueDispose(clockHand);
                    parent.queueDispose(this);
                }
                parent = null;
            }
        } catch (Exception e)
        {
            System.out.println("Failed to dispose target!");
        }
    }
}
