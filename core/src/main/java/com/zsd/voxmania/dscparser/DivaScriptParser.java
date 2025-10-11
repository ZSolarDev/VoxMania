package com.zsd.voxmania.dscparser;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.LittleEndianInputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DivaScriptParser {
    static final ArrayList<Integer> fmts = new ArrayList<>(Arrays.asList(353510679,285614104,335874337,369295649,352458520,335745816,335618838,319956249,319296802,318845217));
    static final Map<Integer, Integer> opCodes = Map.ofEntries(
        /* AGEAGE_CTRL */ Map.entry(105, 8),
        /* AIM */ Map.entry(46, 3),
        /* AOTO_CAP */ Map.entry(97, 1),
        /* AUTO_BLINK */ Map.entry(56, 2),
        /* BAR_TIME_SET */ Map.entry(28, 2),
        /* BLOOM */ Map.entry(93, 2),
        /* CHANGE_FIELD */ Map.entry(14, 1),
        /* CHARA_ALPHA */ Map.entry(96, 4),
        /* CHARA_COLOR */ Map.entry(72, 2),
        /* CHARA_HEIGHT_ADJUST */ Map.entry(60, 2),
        /* CHARA_LIGHT */ Map.entry(103, 3),
        /* CHARA_POS_ADJUST */ Map.entry(62, 4),
        /* CHARA_SIZE */ Map.entry(59, 2),
        /* CLOTH_WET */ Map.entry(50, 2),
        /* COLOR_COLLE */ Map.entry(94, 3),
        /* DATA_CAMERA */ Map.entry(13, 2),
        /* DATA_CAMERA_START */ Map.entry(66, 2),
        /* DOF */ Map.entry(95, 3),
        /* EDIT_BLUSH */ Map.entry(48, 1),
        /* EDIT_CAMERA */ Map.entry(81, 24),
        /* EDIT_DISP */ Map.entry(44, 1),
        /* EDIT_EFFECT */ Map.entry(43, 2),
        /* EDIT_EXPRESSION */ Map.entry(78, 2),
        /* EDIT_EYE */ Map.entry(41, 2),
        /* EDIT_EYELID */ Map.entry(40, 1),
        /* EDIT_EYELID_ANIM */ Map.entry(75, 3),
        /* EDIT_EYE_ANIM */ Map.entry(79, 3),
        /* EDIT_FACE */ Map.entry(30, 1),
        /* EDIT_HAND_ANIM */ Map.entry(45, 2),
        /* EDIT_INSTRUMENT_ITEM */ Map.entry(76, 2),
        /* EDIT_ITEM */ Map.entry(42, 1),
        /* EDIT_LYRIC */ Map.entry(34, 2),
        /* EDIT_MODE_SELECT */ Map.entry(82, 1),
        /* EDIT_MOTION */ Map.entry(27, 4),
        /* EDIT_MOTION_F */ Map.entry(91, 6),
        /* EDIT_MOTION_LOOP */ Map.entry(77, 4),
        /* EDIT_MOT_SMOOTH_LEN */ Map.entry(64, 2),
        /* EDIT_MOUTH */ Map.entry(36, 1),
        /* EDIT_MOUTH_ANIM */ Map.entry(80, 2),
        /* EDIT_MOVE */ Map.entry(38, 7),
        /* EDIT_MOVE_XYZ */ Map.entry(74, 9),
        /* EDIT_SHADOW */ Map.entry(39, 1),
        /* EDIT_TARGET */ Map.entry(35, 5),
        /* EFFECT_OFF */ Map.entry(11, 1),
        /* EFFECT */ Map.entry(9, 6),
        /* END */ Map.entry(0, 0),
        /* EXPRESSION */ Map.entry(22, 4),
        /* EYE_ANIM */ Map.entry(18, 3),
        /* FACE_TYPE */ Map.entry(89, 1),
        /* FADEIN_FIELD */ Map.entry(10, 2),
        /* FADEOUT_FIELD */ Map.entry(17, 2),
        /* FADE_MODE */ Map.entry(55, 1),
        /* FOG */ Map.entry(92, 3),
        /* HAND_ANIM */ Map.entry(20, 5),
        /* HAND_ITEM */ Map.entry(47, 3),
        /* HAND_SCALE */ Map.entry(87, 3),
        /* HIDE_FIELD */ Map.entry(15, 1),
        /* ITEM_ALPHA */ Map.entry(101, 4),
        /* ITEM_ANIM */ Map.entry(61, 4),
        /* ITEM_ANIM_ATTACH */ Map.entry(85, 3),
        /* LIGHT_POS */ Map.entry(88, 4),
        /* LIGHT_ROT */ Map.entry(51, 3),
        /* LOOK_ANIM */ Map.entry(21, 4),
        /* LOOK_CAMERA */ Map.entry(23, 5),
        /* LYRIC */ Map.entry(24, 2),
        /* MAN_CAP */ Map.entry(98, 1),
        /* MIKU_DISP */ Map.entry(4, 2),
        /* MIKU_MOVE */ Map.entry(2, 4),
        /* MIKU_ROT */ Map.entry(3, 2),
        /* MIKU_SHADOW */ Map.entry(5, 2),
        /* MODE_SELECT */ Map.entry(26, 2),
        /* MOUTH_ANIM */ Map.entry(19, 5),
        /* MOVE_CAMERA */ Map.entry(31, 21),
        /* MOVE_FIELD */ Map.entry(16, 3),
        /* MOVIE_CUT_CHG */ Map.entry(102, 2),
        /* MOVIE_DISP */ Map.entry(68, 1),
        /* MOVIE_PLAY */ Map.entry(67, 1),
        /* MUSIC_PLAY */ Map.entry(25, 0),
        /* NEAR_CLIP */ Map.entry(49, 2),
        /* OSAGE_MV_CCL */ Map.entry(71, 3),
        /* OSAGE_STEP */ Map.entry(70, 3),
        /* PARTS_DISP */ Map.entry(57, 3),
        /* PSE */ Map.entry(106, 2),
        /* PV_BRANCH_MODE */ Map.entry(65, 1),
        /* PV_END */ Map.entry(32, 0),
        /* PV_END_FADEOUT */ Map.entry(83, 2),
        /* SATURATE */ Map.entry(54, 1),
        /* SCENE_FADE */ Map.entry(52, 6),
        /* SCENE_ROT */ Map.entry(63, 1),
        /* SET_CAMERA */ Map.entry(12, 6),
        /* SET_CHARA */ Map.entry(37, 1),
        /* SET_MOTION */ Map.entry(7, 4),
        /* SET_PLAYDATA */ Map.entry(8, 2),
        /* SE_EFFECT */ Map.entry(73, 1),
        /* SHADOWHEIGHT */ Map.entry(29, 2),
        /* SHADOWPOS */ Map.entry(33, 3),
        /* SHADOW_CAST */ Map.entry(90, 2),
        /* SHADOW_RANGE */ Map.entry(86, 1),
        /* SHIMMER */ Map.entry(100, 3),
        /* STAGE_LIGHT */ Map.entry(104, 3),
        /* TARGET */ Map.entry(6, 7),
        /* TARGET_FLAG */ Map.entry(84, 1),
        /* TARGET_FLYING_TIME */ Map.entry(58, 1),
        /* TIME */ Map.entry(1, 1),
        /* TONE_TRANS */ Map.entry(53, 6),
        /* TOON */ Map.entry(99, 3),
        /* WIND */ Map.entry(69, 3)
    );

    public static DivaScript parse(FileHandle handle) {
        return parse(handle.read());
    }

    public static DivaScript parse(String path) {
        try(FileInputStream stream = new FileInputStream(path)){
            return parse(stream);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static DivaScript parse(InputStream stream)
    {
        try(LittleEndianInputStream inStrm = new LittleEndianInputStream(stream)) {
            DivaScript script = new DivaScript();
            int fmt = inStrm.readInt();
            if (!fmts.contains(fmt))
                throw new RuntimeException("DSC Format unrecognized!");
            script.fmt = fmt;
            while(true)
            {
                try{
                    Command cmd = new Command();
                    cmd.type = inStrm.readInt();
                    for (int i = 0; i < opCodes.get(cmd.type); i++)
                    {
                        cmd.args.add(inStrm.readInt());
                    }
                    script.scriptData.add(cmd);
                } catch (Exception e) {
                    break;
                }
            }
            return script;
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
