package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.zsd.voxmania.game.events.EventData;
import com.zsd.voxmania.game.events.scontainers.DivaScriptESC;
import com.zsd.voxmania.game.events.ESCRunner;
import com.zsd.voxmania.game.events.types.TargetHitEvent;
import com.zsd.voxmania.game.ui.objects.game.TargetUI;
import com.zsd.voxmania.states.State;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;


public class Game extends State {
    GameUI ui;
    ESCRunner runner;
    DivaInputState diva;
    ArrayList<DivaInputState.TargetInputType> canBeHeld = new ArrayList<>();
    HashMap<String, Sound> hitSounds = new HashMap<>();
    public float score = 0;
    public float curHoldScore = 0;
    public float heldSecs = 0;
    public int curCombo = 0;

    public TargetUI getTargetUI()
    {
        return ui.targetRenderer;
    }

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
            getTargetUI().addNote(target);
            target.getMusTime = () -> {return runner.music.getPosition();};
            target.flyingTime = flyingTime;
            target.eventTime = eventTime;
            Target note = new Target(normalizeNoteType(noteType), false, false, (noteType == 4 || noteType == 5 || noteType == 6 || noteType == 7), -1000, -1000, ui, angle, distance, amplitude, frequency, x, y, event, target);
            getTargetUI().addNote(note);
            note.getMusTime = () -> {return runner.music.getPosition();};
            note.flyingTime = flyingTime;
            note.eventTime = eventTime;
        };
        runner.onBasicEvent = (event) -> {
            //System.out.println("New basic event of type " + event.type + " with params: " + event.params.toString() + "!");
        };
        runner.run();
        addObject(runner);

        diva = new DivaInputState(
            (pressed) -> {
                if (pressed.contains(DivaInputState.TargetInputType.TRIANGLE))
                    ui.overlay.mobileOverlay.onPressableGhostHit(0);
                if (pressed.contains(DivaInputState.TargetInputType.CIRCLE))
                    ui.overlay.mobileOverlay.onPressableGhostHit(1);
                if (pressed.contains(DivaInputState.TargetInputType.CROSS))
                    ui.overlay.mobileOverlay.onPressableGhostHit(2);
                if (pressed.contains(DivaInputState.TargetInputType.SQUARE))
                    ui.overlay.mobileOverlay.onPressableGhostHit(3);
                onTargetHit((target) -> (
                    ((target.type == 0 || target.type == 4 || target.type == 18) && pressed.contains(DivaInputState.TargetInputType.TRIANGLE)) ||
                    ((target.type == 1 || target.type == 5 || target.type == 19) && pressed.contains(DivaInputState.TargetInputType.CIRCLE)) ||
                    ((target.type == 2 || target.type == 6 || target.type == 20) && pressed.contains(DivaInputState.TargetInputType.CROSS)) ||
                    ((target.type == 3 || target.type == 7 || target.type == 21) && pressed.contains(DivaInputState.TargetInputType.SQUARE))
                ),
                    (t) -> "target_hit");
            },
            (held) -> {
                boolean pressable = false;
                if (held.contains(DivaInputState.TargetInputType.TRIANGLE) && canBeHeld.contains(DivaInputState.TargetInputType.TRIANGLE)) {
                    ui.overlay.mobileOverlay.onPressableGhostHit(0);
                    pressable = true;
                }
                if (held.contains(DivaInputState.TargetInputType.CIRCLE) && canBeHeld.contains(DivaInputState.TargetInputType.CIRCLE)) {
                    ui.overlay.mobileOverlay.onPressableGhostHit(1);
                    pressable = true;
                }
                if (held.contains(DivaInputState.TargetInputType.CROSS) && canBeHeld.contains(DivaInputState.TargetInputType.CROSS)) {
                    ui.overlay.mobileOverlay.onPressableGhostHit(2);
                    pressable = true;
                }
                if (held.contains(DivaInputState.TargetInputType.SQUARE) && canBeHeld.contains(DivaInputState.TargetInputType.SQUARE)) {
                    ui.overlay.mobileOverlay.onPressableGhostHit(3);
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
                    (target) -> (target.type == 15 || target.type == 16 ? "chain_hit" : "slide_hit"));
            }
        );
        Gdx.input.setInputProcessor(diva);


        // LUA TESTING
        Globals globals = JsePlatform.standardGlobals();
        LuaValue script = globals.load(Gdx.files.internal("theme/test.lua").readString(), "test.lua");
        script.call(); // prints to console
        LuaValue func = globals.get("greet");
        float ret = func.call(LuaValue.valueOf("ZSolarDev")).tofloat();
        System.out.println("Lua function 'greet' with arg 'ZSolarDev' returned " + ret + "!");
    }



    boolean addedHoldBonus = false;
    float lastHeldLength = 0;
    public void updateHolds(ArrayList<DivaInputState.TargetInputType> held)
    {
        if (!held.isEmpty()) {
            heldSecs += Gdx.graphics.getDeltaTime();

            int heldSecsInt = (int) heldSecs;

            if (heldSecsInt < 5) {
                curHoldScore += (600 * Gdx.graphics.getDeltaTime()) * held.size();
            }else if (!addedHoldBonus) {
                addedHoldBonus = true;
            }else {
                curHoldScore = 3000 * held.size();
                ui.overlay.holdOverlay.showBonusText = true;
            }

            ui.overlay.holdOverlay.showText = true;
            ui.overlay.holdOverlay.holdScoreText.text = "+" + (int) curHoldScore;
            ui.overlay.holdOverlay.bonusHoldScoreText.text =  "+" + 1500 * held.size();
            lastHeldLength = held.size();
        } else {
            score += curHoldScore;
            score += 1500 * lastHeldLength;
            lastHeldLength = 0;
            heldSecs = 0;
            curHoldScore = 0;
            addedHoldBonus = false;
            ui.overlay.holdOverlay.showText = false;
            ui.overlay.holdOverlay.showBonusText = false;
        }
        ui.overlay.holdOverlay.curHeldData = (ArrayList<DivaInputState.TargetInputType>) held.clone();
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

    public void onTargetHit(Function<Target, Boolean> condition, Function<Target, String> hitNoise)
    {
        ArrayList<TargetHit> pressableTargets = new ArrayList<>();
        for (Target target : getTargetUI().targets) {
            if (target.target)
                continue;
            if (condition.apply(target))
            {
                int rating = 0;
                boolean canHit = false;
                float hitTime = runner.music.getPosition() - (target.eventTime + target.flyingTime);
                float absHitTime = Math.abs(hitTime);
                ArrayList<Float> times = new ArrayList<Float>(List.of(0.13f, 0.10f, 0.07f, 0.03f, 1.0f/120.0f));
                for (int timeIdx = 0; timeIdx < times.size(); timeIdx++) {
                    float time = times.get(timeIdx);
                    ArrayList<Integer> sliderIDs = new ArrayList<Integer>(List.of(12, 13, 15, 16));
                    if (time < 0.07f && sliderIDs.contains(normalizeNoteType(target.type)))
                        continue;

                    if (absHitTime <= time) {
                        int ratingStart = timeIdx * 3;
                        rating = (hitTime < 0 ? ratingStart : hitTime == time ? ratingStart + 1 : ratingStart + 2);
                        canHit = true;
                    }
                }
                if (canHit)
                {
                    TargetHit tHit = new TargetHit();
                    tHit.target = target;
                    tHit.rating = rating;
                    if (!pressableTargets.isEmpty())
                    {
                        if (pressableTargets.get(pressableTargets.size() - 1).target.progress == target.progress)
                            pressableTargets.add(tHit);
                    }else
                        pressableTargets.add(tHit);
                }
            }
        }
        for (TargetHit tHit : pressableTargets) {
            Target target = tHit.target;
            ui.overlay.mobileOverlay.onPressableHit(normalizeNoteType(target.type));
            runner.processEvent(new EventData("TargetHitEvent", new TargetHitEvent(target), runner));
            getTargetUI().removeTarget(target);
            getTargetUI().removeTarget(target.parentTarget);
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
            int rating = tHit.rating;
            int ratingScore = 0;
            if (rating >= 0)
                ratingScore = 50;
            if (rating >= 3)
                ratingScore = 100;
            if (rating >= 6) {
                ratingScore = 300;
                curCombo++;
            }
            if (rating >= 9)
                ratingScore = 500;
            score += ratingScore;
        }
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (diva != null)
            diva.update(delta);
        if (getTargetUI() != null) {
            for (Target target : getTargetUI().targets) {
                if (target.target)
                    continue;
                if (target.progress < -(target.type == 15 || target.type == 16 ? 0.02 : 0.07)) {
                    runner.processEvent(new EventData("TargetHitEvent", new TargetHitEvent(target), runner));
                    getTargetUI().queueRemoveTarget(target);
                    getTargetUI().queueRemoveTarget(target.parentTarget);
                    target.dispose();
                    target.parentTarget.dispose();
                }
            }
        }
    }
}

class TargetHit {
    public Target target;
    /*
    0-2: sad
    3-5: safe
    6-8: fine
    9-11: cool
    12-14: cool (without delay)
     */
    public int rating;
    public TargetHit() {}
}
