package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.zsd.voxmania.display.DisplayObject;
import com.zsd.voxmania.display.screen.drawables.DrawableSprite;
import com.zsd.voxmania.game.events.EventData;
import com.zsd.voxmania.game.events.scontainers.DivaScriptESC;
import com.zsd.voxmania.game.events.ESCRunner;
import com.zsd.voxmania.game.events.types.TargetHitEvent;
import com.zsd.voxmania.states.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;


public class Game extends State {
    GameUI ui;
    ESCRunner runner;
    DivaInputState diva;
    ArrayList<DivaInputState.TargetInputType> canBeHeld = new ArrayList<>();
    HashMap<String, Sound> hitSounds = new HashMap<>();

    @SuppressWarnings("DiscouragedApi")
    @Override
    public void create() {
        super.create();
        backgroundColor = Color.GRAY;
        ui = new GameUI();
        defaultRenderer.addRenderer(ui);

        hitSounds.put("target_hit", Gdx.audio.newSound(Gdx.files.internal("game/sounds/target_hit.wav")));
        hitSounds.put("chain_hit", Gdx.audio.newSound(Gdx.files.internal("game/sounds/chain_hit.wav")));
        hitSounds.put("slide_hit", Gdx.audio.newSound(Gdx.files.internal("game/sounds/slide_hit.wav")));

        runner = new ESCRunner(new DivaScriptESC(Gdx.files.internal("mods/TestMod/World Is Mine ExEx.dsc")), Gdx.audio.newMusic(Gdx.files.internal("mods/TestMod/World Is Mine.ogg")));
        runner.onTargetEvent = (args, event) -> {
            int noteType = Math.round(args.get(0));
            float x = args.get(1);
            float y = args.get(2);
            float angle = args.get(3);
            float distance = args.get(4);
            float amplitude = args.get(5);
            float frequency = args.get(6);
            float flyingTime = args.get(7);
            float eventTime = args.get(8);
            boolean special = args.get(9) == 1 && (normalizeNoteType(noteType) != 12 && normalizeNoteType(noteType) != 13 && normalizeNoteType(noteType) != 15 && normalizeNoteType(noteType) != 16);

            Target target = new Target(normalizeNoteType(noteType), true, special, (noteType == 4 || noteType == 5 || noteType == 6 || noteType == 7), x, y, ui, angle, distance, amplitude, frequency, x, y, event, null);
            ui.addNote(target);
            target.getMusTime = () -> {return runner.music.getPosition();};
            target.flyingTime = flyingTime;
            target.eventTime = eventTime;
            Target note = new Target(normalizeNoteType(noteType), false, false, (noteType == 4 || noteType == 5 || noteType == 6 || noteType == 7), -1000, -1000, ui, angle, distance, amplitude, frequency, x, y, event, target);
            ui.addNote(note);
            note.getMusTime = () -> {return runner.music.getPosition();};
            note.flyingTime = flyingTime;
            note.eventTime = eventTime;

            //System.out.println("New target of type " + args.get(0) + " With flying time " + args.get(args.size() - 1) + "!");
        };
        runner.onBasicEvent = (event) -> {
            //System.out.println("New basic event of type " + event.type + " with params: " + event.params.toString() + "!");
        };
        runner.run();
        addObject(runner);

        diva = new DivaInputState(
            (pressed) -> {
                if (pressed.contains(DivaInputState.TargetInputType.TRIANGLE))
                    ui.overlay.onPressableGhostHit(0);
                if (pressed.contains(DivaInputState.TargetInputType.CIRCLE))
                    ui.overlay.onPressableGhostHit(1);
                if (pressed.contains(DivaInputState.TargetInputType.CROSS))
                    ui.overlay.onPressableGhostHit(2);
                if (pressed.contains(DivaInputState.TargetInputType.SQUARE))
                    ui.overlay.onPressableGhostHit(3);
                onTargetHit((target) -> (
                    ((target.type == 0 || target.type == 4 || target.type == 18) && pressed.contains(DivaInputState.TargetInputType.TRIANGLE)) ||
                    ((target.type == 1 || target.type == 5 || target.type == 19) && pressed.contains(DivaInputState.TargetInputType.CIRCLE)) ||
                    ((target.type == 2 || target.type == 6 || target.type == 20) && pressed.contains(DivaInputState.TargetInputType.CROSS)) ||
                    ((target.type == 3 || target.type == 7 || target.type == 21) && pressed.contains(DivaInputState.TargetInputType.SQUARE))
                ),
                (target) -> (target.progress < 0.07 && target.progress > -0.07),
                    (_) -> "target_hit");
            },
            (held) -> {
                boolean pressable = false;
                if (held.contains(DivaInputState.TargetInputType.TRIANGLE) && canBeHeld.contains(DivaInputState.TargetInputType.TRIANGLE)) {
                    ui.overlay.onPressableGhostHit(0);
                    pressable = true;
                }
                if (held.contains(DivaInputState.TargetInputType.CIRCLE) && canBeHeld.contains(DivaInputState.TargetInputType.CIRCLE)) {
                    ui.overlay.onPressableGhostHit(1);
                    pressable = true;
                }
                if (held.contains(DivaInputState.TargetInputType.CROSS) && canBeHeld.contains(DivaInputState.TargetInputType.CROSS)) {
                    ui.overlay.onPressableGhostHit(2);
                    pressable = true;
                }
                if (held.contains(DivaInputState.TargetInputType.SQUARE) && canBeHeld.contains(DivaInputState.TargetInputType.SQUARE)) {
                    ui.overlay.onPressableGhostHit(3);
                    pressable = true;
                }
                if (pressable)
                    updateHolds(held);
            },
            (released) -> {
                updateHolds(new ArrayList<DivaInputState.TargetInputType>());
                canBeHeld.clear();
            },
            (sliders) -> {
                onTargetHit((target) -> (
                (target.type == 12 && sliders.contains(DivaInputState.SliderInputType.LEFT)) ||
                (target.type == 13 && sliders.contains(DivaInputState.SliderInputType.RIGHT)) ||
                (target.type == 15 && sliders.contains(DivaInputState.SliderInputType.LEFT)) ||
                (target.type == 16 && sliders.contains(DivaInputState.SliderInputType.RIGHT)) ||
                (target.type == 23 && sliders.contains(DivaInputState.SliderInputType.LEFT)) ||
                (target.type == 24 && sliders.contains(DivaInputState.SliderInputType.RIGHT))),
                (target) -> (target.progress < (target.type == 15 || target.type == 16 ? 0.02 : 0.07) && target.progress > -(target.type == 15 || target.type == 16 ? 0.02 : 0.07)),
                (target) -> (target.type == 15 || target.type == 16 ? "chain_hit" : "slide_hit"));
            }
        );
        Gdx.input.setInputProcessor(diva);
    }

    public void updateHolds(ArrayList<DivaInputState.TargetInputType> held)
    {
        ui.overlay.curHeldData = (ArrayList<DivaInputState.TargetInputType>) held.clone();
    }

    public int normalizeNoteType(int type)
    {
        switch (type)
        {
            case 4:
                return 0;
            case 18:
                return 0;
            case 5:
                return 1;
            case 19:
                return 1;
            case 6:
                return 2;
            case 20:
                return 2;
            case 7:
                return 3;
            case 21:
                return 3;
            case 23:
                return 12;
            case 24:
                return 13;
            default:
                return type;
        }
    }

    public void onTargetHit(Function<Target, Boolean> condition, Function<Target, Boolean> hitCondition, Function<Target, String> hitNoise)
    {
        ArrayList<Target> pressableTargets = new ArrayList<>();
        for (Target target : ui.targets) {
            if (target.target)
                continue;
            if (condition.apply(target))
            {
                if (hitCondition.apply(target))
                {
                    if (!pressableTargets.isEmpty())
                    {
                        if (pressableTargets.get(pressableTargets.size() - 1).progress <= target.progress)
                            pressableTargets.add(target);
                    }else
                        pressableTargets.add(target);
                }
            }
        }
        for (Target target : pressableTargets) {
            ui.overlay.onPressableHit(normalizeNoteType(target.type));
            runner.processEvent(new EventData("TargetHitEvent", new TargetHitEvent(target), runner));
            ui.removeTarget(target);
            ui.removeTarget(target.parentTarget);
            target.dispose();
            target.parentTarget.dispose();
            Sound sound = hitSounds.get(hitNoise.apply(target));
            if (sound != null) sound.play();
            if (target.hold){
                if (target.type == 0 && !canBeHeld.contains(DivaInputState.TargetInputType.TRIANGLE))
                    canBeHeld.add(DivaInputState.TargetInputType.TRIANGLE);
                if (target.type == 1 && !canBeHeld.contains(DivaInputState.TargetInputType.CIRCLE))
                    canBeHeld.add(DivaInputState.TargetInputType.CIRCLE);
                if (target.type == 2 && !canBeHeld.contains(DivaInputState.TargetInputType.CROSS))
                    canBeHeld.add(DivaInputState.TargetInputType.CROSS);
                if (target.type == 3 && !canBeHeld.contains(DivaInputState.TargetInputType.SQUARE))
                    canBeHeld.add(DivaInputState.TargetInputType.SQUARE);
            }

        }
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        System.out.println(Gdx.graphics.getFramesPerSecond());
        if (diva != null)
            diva.update(delta);
        if (ui != null) {
            for (Target target : ui.targets) {
                if (target.target)
                    continue;
                if (target.progress < -(target.type == 15 || target.type == 16 ? 0.02 : 0.07)) {
                    runner.processEvent(new EventData("TargetHitEvent", new TargetHitEvent(target), runner));
                    ui.queueRemoveTarget(target);
                    ui.queueRemoveTarget(target.parentTarget);
                    target.dispose();
                    target.parentTarget.dispose();
                }
            }
        }
    }
}
