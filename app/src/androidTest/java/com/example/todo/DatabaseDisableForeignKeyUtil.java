package com.example.todo;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class DatabaseDisableForeignKeyUtil extends RoomDatabase.Callback {
    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=OFF");
    }
}
