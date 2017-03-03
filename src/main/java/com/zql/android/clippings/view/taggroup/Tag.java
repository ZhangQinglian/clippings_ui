/*******************************************************************************
 *    Copyright 2017-present, Clippings Contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package com.zql.android.clippings.view.taggroup;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;


import com.zql.android.clippings.R;
import com.zqlite.android.logly.Logly;

import java.util.Random;

/**
 * @author qinglian.zhang, created on 2017/3/2.
 */
public class Tag extends TextView {

    private int[] mColors = new int[]{Color.WHITE,Color.BLACK};

    /**
     * 在{@link TagsGroup}中的位置
     */
    private int mIndex = -1;

    private TagCallback mCallback;

    public interface TagCallback{
        void onTagClick(int index);
        void onTagLongClick(int index);
    }
    public Tag(Context context) {
        super(context);
        initDefault();
    }

    public Tag(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefault();
    }

    public Tag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();

    }

    public void setIndex(int index){
        mIndex = index;
    }

    public void setCallback(TagCallback callback){
        mCallback = callback;
    }
    private void initDefault(){
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                _text();
                _bg();
                _layoutParams();
                _listener();
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });


    }


    private String[] strs = {"呐喊","car","二傻系列","a","钢铁是怎样炼成的","励志"};
    private void _text(){
        setText(strs[new Random().nextInt(6)]);
        setTextColor(mColors[1]);
        setTextSize(16);
        int[][] state = {
                {android.R.attr.state_pressed},
                {}
        };
        int[] color = {mColors[0],mColors[1]};
        setTextColor(new ColorStateList(state,color));
        setClickable(true);
    }
    private void _bg(){
        setClickable(true);
        StateListDrawable bg = new StateListDrawable();

        GradientDrawable normalBg = new GradientDrawable();
        normalBg.setShape(GradientDrawable.RECTANGLE);
        normalBg.setStroke(getHeight()/20,mColors[1]);
        normalBg.setColor(mColors[0]);
        normalBg.setCornerRadius(getHeight());

        GradientDrawable pressBg = new GradientDrawable();
        pressBg.setShape(GradientDrawable.RECTANGLE);
        pressBg.setStroke(getHeight()/20,mColors[0]);
        pressBg.setColor(mColors[1]);
        pressBg.setCornerRadius(getHeight());
        bg.addState(new int[]{android.R.attr.state_pressed},pressBg);
        bg.addState(new int[]{},normalBg);

        setBackground(bg);
    }
    private void _layoutParams(){
        int h = getHeight();
        setPadding(h*2/3,h/6,h*2/3,h/6);
        setGravity(Gravity.CENTER);
    }
    private void _listener(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null){
                    Logly.d("  onclick : " + mIndex);
                    mCallback.onTagClick(mIndex);
                }
            }
        });
    }

}
