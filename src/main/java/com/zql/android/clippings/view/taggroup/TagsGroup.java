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

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author qinglian.zhang, created on 2017/3/2.
 */
public class TagsGroup extends ViewGroup implements Tag.TagCallback{


    private int mChildMargin = 0;

    private int mChildHeight = 0;

    private List<List<Tag>> mChilds = new ArrayList<>();

    private Handler mTestHandler = new Handler();

    public TagsGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefault();
    }

    public TagsGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
    }

    private void initDefault(){
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mTestHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addTag(new Tag(getContext()));
                        addTag(new Tag(getContext()));
                        addTag(new Tag(getContext()));
                        addTag(new Tag(getContext()));
                        addTag(new Tag(getContext()));
                        addTag(new Tag(getContext()));
                        addTag(new Tag(getContext()));
                    }
                },1000);
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    /**
     * add a tag
     * @param tag {@link Tag}
     */
    public void addTag(Tag tag){
        addView(tag);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mChilds.clear();
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int childrenCount = getChildCount();

        int childMeasureSpecWidth = MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(),MeasureSpec.UNSPECIFIED);
        int childMeasureSpecHeigth = MeasureSpec.makeMeasureSpec(height - getPaddingBottom() - getPaddingTop(),MeasureSpec.UNSPECIFIED);

        if(getChildCount() > 0){
            Tag tag = (Tag) getChildAt(0);
            tag.measure(childMeasureSpecWidth,childMeasureSpecHeigth);
            mChildMargin = tag.getMeasuredHeight()/10;
            mChildHeight = tag.getMeasuredHeight();
        }

        for(int i = 0;i<childrenCount;i++){
            Tag tag = (Tag) getChildAt(i);
            tag.setIndex(i);
            tag.setCallback(this);
            tag.measure(childMeasureSpecWidth,childMeasureSpecHeigth);
            putChildren(tag,width - getPaddingRight() - getPaddingLeft());
        }

        setMeasuredDimension(width,(mChildHeight + mChildMargin*2) * mChilds.size()+ getPaddingBottom() + getPaddingTop());
    }

    /**
     * 在measure阶段将子view放进一个二维列表，然后再layout阶段取出并确定其位置
     * @param tag
     * @param width
     */
    private void putChildren(Tag tag,int width){
        List<Tag> tags;
        if(mChilds.size() == 0){
            tags = new ArrayList<>();
            mChilds.add(tags);
        }else {
            tags = mChilds.get(mChilds.size()-1);
        }

        if(!checkWidth(tags,tag,width)){
            tags = new ArrayList<>();
            mChilds.add(tags);
        }
        tags.add(tag);

    }

    private boolean checkWidth(List<Tag> tags,Tag tag,int width){
        int ruler = 0;
        for(Tag t : tags){
            ruler += (t.getMeasuredWidth() + mChildMargin*2);
        }
        if(ruler + (tag.getMeasuredWidth() + mChildMargin*2) > width){
            return false;
        }else {
            return true;
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for(int i = 0;i<mChilds.size();i++){
            List<Tag> tags = mChilds.get(i);
            int turlyPadding = getChildTurlyPadding(getChildsRowWidth(tags));
            int widthRuler = turlyPadding;
            for(int j = 0;j<tags.size();j++){
                Tag tag = tags.get(j);
                int left = widthRuler + mChildMargin;
                int top = getPaddingTop() + i*(tag.getMeasuredHeight() + mChildMargin*2) + mChildMargin;
                int right = left + tag.getMeasuredWidth();
                int bottom = top + tag.getMeasuredHeight();
                tag.layout(left,top,right,bottom);
                widthRuler = right + mChildMargin;
            }
        }
    }

    private int getChildsRowWidth(List<Tag> tags){
        int w = 0;
        for(int i = 0;i<tags.size();i++) w += (tags.get(i).getMeasuredWidth() + mChildMargin*2);
        return w;
    }

    private int getChildTurlyPadding(int width){
        return (getMeasuredWidth() - width)/2;
    }

    @Override
    public void onTagClick(int index) {
        performExitAnimation((Tag) getChildAt(index));
    }

    @Override
    public void onTagLongClick(int index) {

    }

    private void performExitAnimation(final Tag tag){
        tag.setClickable(false);
        tag.animate().alpha(0).setDuration(800).scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(tag);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).withLayer().start();
    }
}
