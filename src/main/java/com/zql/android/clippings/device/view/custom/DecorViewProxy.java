package com.zql.android.clippings.device.view.custom;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by scott on 2017/8/4.
 */

public class DecorViewProxy {
    private ViewGroup mDecorView;

    private Activity activity;

    private SlideView slideView;

    public DecorViewProxy bind(Activity original) {
        activity = original;
        mDecorView = (ViewGroup) activity.getWindow().getDecorView();
        slideView = new SlideView(original);

        View child = mDecorView.getChildAt(0);
        ViewGroup.LayoutParams vglp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        slideView.setLayoutParams(child.getLayoutParams());
        child.setLayoutParams(vglp);
        mDecorView.removeViewAt(0);
        slideView.addView(child);
        mDecorView.addView(slideView);

        slideView.init(activity);
        return this;
    }

    public void enableSlideActivity(){
        slideView.enable();
    }

    public void disableSlideActivity(){
        slideView.disable();
    }
}
