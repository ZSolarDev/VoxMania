package com.zsd.voxmania.game;

import com.zsd.voxmania.display.screen.sprite.NestableSpriteRenderer;

import java.util.ArrayList;

public class GameUI extends NestableSpriteRenderer {
    public ArrayList<Note> notes = new ArrayList<Note>();

    public GameUI(ArrayList<Note> notes)
    {
        super();
        this.notes = notes;
        renderers.addAll(notes);
    }

    public void addNote(Note note)
    {
        notes.add(note);
        addRenderer(note);
    }

    public void addNote(Note note, int idx)
    {
        notes.add(idx, note);
        addRenderer(note, idx);
    }

    public void removeNote(Note note)
    {
        notes.remove(note);
        removeRenderer(note);
    }

    public void removeNote(int idx)
    {
        notes.remove(idx);
        removeRenderer(idx);
    }

    public GameUI()
    {
        this(new ArrayList<Note>());
    }
}
