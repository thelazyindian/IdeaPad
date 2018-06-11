package com.jashasweejena.ideapad.adapters;

import android.content.Context;

import com.jashasweejena.ideapad.model.Idea;

import io.realm.RealmResults;

public class RealmIdeaAdapter extends RealmModelAdapter<Idea> {

    public RealmIdeaAdapter(Context context, RealmResults<Idea> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
