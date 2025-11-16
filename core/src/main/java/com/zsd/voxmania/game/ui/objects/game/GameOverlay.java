package com.zsd.voxmania.game.ui.objects.game;

import com.zsd.voxmania.display.screen.sprite.NestableDrawableRenderer;
import com.zsd.voxmania.game.ui.UILayer;
import com.zsd.voxmania.game.ui.UIManager;

public class GameOverlay extends UILayer
{
    public MobileOverlay mobileOverlay;
    public TargetHoldOverlay holdOverlay;

    public GameOverlay()
    {
        super();
        mobileOverlay = (MobileOverlay) UIManager.createUILayer("game.MobileOverlay", MobileOverlay.class);
        addRenderer(mobileOverlay);
        holdOverlay = (TargetHoldOverlay) UIManager.createUILayer("game.TargetHoldOverlay", TargetHoldOverlay.class);
        addRenderer(holdOverlay);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        mobileOverlay.position.x = position.x;
        mobileOverlay.position.y = position.y;
        holdOverlay.position.x = position.x;
        holdOverlay.position.y = position.y;
    }

    @Override
    public void dispose()
    {
        super.dispose();
        mobileOverlay.dispose();
        holdOverlay.dispose();
    }
}
