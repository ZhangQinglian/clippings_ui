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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;


import com.zql.android.clippings.R;
import com.zqlite.android.logly.Logly;


/**
 * @author qinglian.zhang, created on 2017/3/2.
 */
public class Tag extends AppCompatTextView {

    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_EDIT = 2;

    protected int mStatus = STATUS_NORMAL;

    private int[] mColors ;

    private String mTagText;
    /**
     * 在{@link TagsGroup}中的位置
     */
    private int mIndex = -1;

    private TagCallback mCallback;

    public interface TagCallback{
        void onTagClick(int index,int currentStatus,String label);
        void onTagLongClick(int index,int currentStatus);
    }

    public Tag(Context context,String text){
        this(context,text,TagsGroup.DEFAULT_TAG_COLORS);
    }
    public Tag(Context context,String text,int[] colors) {
        super(context);
        initDefault();
        mColors = colors;
        setTagText(text);
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

    public void changeStatus(int status){
        if(status == mStatus) return;
        mStatus = status;
        onStatusChange(mStatus);
    }

    protected void onStatusChange(int status){
        if(mStatus == STATUS_EDIT){
            Drawable drawable = getResources().getDrawable(R.drawable.ic_tag_delete);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable d = new BitmapDrawable(this.getResources(), Bitmap.createScaledBitmap(bitmap, getMeasuredHeight()/3, getMeasuredHeight()/3, true));
            setCompoundDrawablesWithIntrinsicBounds(null,null, d,null);
            setCompoundDrawablePadding(getMeasuredHeight()/2);
        }else {
            setCompoundDrawablesWithIntrinsicBounds(null,null, null,null);
        }
    }
    public void setCallback(TagCallback callback){
        mCallback = callback;
    }

    public void setTagText(String text){
        mTagText = text;
        setText(mTagText);
    }

    public String getTagText(){
        return mTagText;
    }
    protected void initDefault(){
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                if(isShown()){
                    _text();
                    _layoutParams();
                    _listener();
                    _bg();
                    Logly.d("  tag removeOnPreDrawListener ");
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
                return true;
            }
        });
    }

    private void _text(){
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
    protected void _bg(){
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
    protected void _listener(){
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null){
                    Logly.d("  onclick : " + mIndex);
                    mCallback.onTagClick(mIndex,mStatus,mTagText);
                }
            }
        });

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mCallback != null){
                    mCallback.onTagLongClick(mIndex,mStatus);
                }
                return true;
            }
        });
    }
}
