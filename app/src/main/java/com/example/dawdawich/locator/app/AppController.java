package com.example.dawdawich.locator.app;
//get information from https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/

import android.app.Application;
import android.content.Context;

import com.example.dawdawich.locator.helper.SQLiteHandler;
import com.example.dawdawich.locator.helper.SessionManager;
import com.example.dawdawich.locator.interfaces.UpdateUser;

public class AppController extends Application {

    private static AppController mInstance;
    private UpdateUser update;
    private SessionManager session;
    private SQLiteHandler db;

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        session = new SessionManager(this);
        db = new SQLiteHandler(AppController.getInstance());

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public UpdateUser getUpdate() {
        return update;
    }

    public void setUpdate(UpdateUser update) {
        this.update = update;
    }

    public SessionManager getSession() {
        return session;
    }

    public void setSession(SessionManager session) {
        this.session = session;
    }

    public SQLiteHandler getDb() {
        return db;
    }
}
