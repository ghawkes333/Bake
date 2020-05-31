package com.example.bake.IdlingResource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    volatile private ResourceCallback mCallback;
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);


    @Override
    public String getName() {
        return SimpleIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(Boolean isIdleNow){
        if(isIdleNow && mCallback != null){
            mCallback.onTransitionToIdle();
        }
    }
}
