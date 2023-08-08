package com.example.todo;

import android.app.Application;
import android.content.Context;

import com.example.todo.di.AppContainer;

public class TodoApplication extends Application {
    private AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = new AppContainer(this);
    }

    public AppContainer getAppContainer() {
        return appContainer;
    }
}
