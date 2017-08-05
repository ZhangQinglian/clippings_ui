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

package com.zql.android.clippings.device.view.taggroup;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.R;
import com.zql.android.clippings.device.db.Label;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author qinglian.zhang, created on 2017/3/2.
 */
public class TagsGroup extends ViewGroup implements Tag.TagCallback ,SpecialTag.SpecialTagCallback{


    public static final int[] DEFAULT_TAG_COLORS = new int[]{Color.WHITE,Color.BLACK};
    public static final int[] DEFAULT_SPECAIL_TAG_COLORS = new int[]{Color.WHITE,Color.parseColor("#607D8B")};

    /**
     * 编辑模式，可添加可删除
     */
    public static final int MODE_EDIT =1;
    /**
     * 正常模式，只可点击
     */
    public static final int MODE_NORMAL =2;

    /**
     * 从TagsGroup移除，并从数据库移除
     */
    public static final int TAG_CHANGE_REMOVE = 1;
    /**
     * 添加进TagsGroup，并添加进数据库
     */
    public static final int TAG_CHANGE_ADD = 2;
    /**
     * 在TagsGroup上显示，无需添加数据库，该数据应该是从数据库中取出
     */
    public static final int TAG_CHAGE_SHOW = 3;

    private int mChildMargin = 0;

    private int mChildHeight = 0;

    private List<List<Tag>> mChilds = new ArrayList<>();

    private Handler mTestHandler = new Handler();

    private SpecialTag mSpecialTag;

    private Set<String> mTagSet = new HashSet<>();

    private TagsGroupCallback mCallback;

    private int mMode = MODE_NORMAL;
    public interface TagsGroupCallback{
        /**
         *
         * @param tagText
         * @param status
         */
        void onTagChanged(String tagText,int status);

        /**
         *
         * @param index
         * @param label
         */
        void onTagClick(int index,String label);
    }
    public TagsGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefault(attrs);
    }

    public TagsGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault(attrs);
    }

    private void initDefault( AttributeSet attrs){
        if(attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,new int[]{R.attr.tag_mode});
            int mode = typedArray.getInt(0,-1);
            if(mode == MODE_EDIT){
                mMode = MODE_EDIT;
            }else {
                mMode = MODE_NORMAL;
            }
            typedArray.recycle();
        }
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mTestHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mMode == MODE_EDIT){
                            addSpecalTag(new SpecialTag(getContext(),getResources().getString(R.string.add_label)));
                        }
                    }
                });
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
        if(mTagSet.contains(tag.getTagText())){
            Toast.makeText(ClippingsApplication.own(),getResources().getString(R.string.label_already_exist),Toast.LENGTH_SHORT).show();
        }else {
            addTagText(tag.getTagText(),TAG_CHANGE_ADD);
            addView(tag);
        }
    }

    public void showLables(List<Label> labels){
        if(labels != null){
            if(mMode == MODE_NORMAL){
                mTagSet.clear();
                removeAllViews();
            }
            for(int i = 0;i<labels.size();i++){
                Label label = labels.get(i);
                if(mTagSet.contains(label.label)) continue;
                addTagText(label.label,TAG_CHAGE_SHOW);
                Tag tag = new Tag(getContext(),label.label);
                tag.setTag(label.id);
                addView(tag);
            }
        }
    }
    public void addSpecalTag(SpecialTag tag){
        addView(tag,0);
    }
    public void addCallback(TagsGroupCallback callback){
        mCallback = callback;
    }
    private void addTagText(String text,int status){
        mTagSet.add(text);
        if(mCallback != null){
            mCallback.onTagChanged(text,status);
        }
    }

    private void removeTagText(String text,int status){
        mTagSet.remove(text);
        if(mCallback != null){
            mCallback.onTagChanged(text,status);
        }
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
            if(i>0){
                Tag tag = (Tag) getChildAt(i);
                tag.setIndex(i);
                tag.setCallback(this);
                tag.measure(childMeasureSpecWidth,childMeasureSpecHeigth);
                putChildren(tag,width - getPaddingRight() - getPaddingLeft());
            }else {
                if(getChildAt(i) instanceof SpecialTag){
                    SpecialTag tag = (SpecialTag) getChildAt(i);
                    tag.setIndex(i);
                    tag.setSpecialCallback(this);
                    tag.measure(childMeasureSpecWidth,childMeasureSpecHeigth);
                    putChildren(tag,width - getPaddingRight() - getPaddingLeft());
                }else {
                    Tag tag = (Tag) getChildAt(i);
                    tag.setIndex(i);
                    tag.setCallback(this);
                    tag.measure(childMeasureSpecWidth,childMeasureSpecHeigth);
                    putChildren(tag,width - getPaddingRight() - getPaddingLeft());
                }
            }
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
        return ruler + (tag.getMeasuredWidth() + mChildMargin * 2) <= width;
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
    public void onTagClick(int index, int status,String label) {
        if(mMode == MODE_EDIT){
            if(status == Tag.STATUS_EDIT){
                performExitAnimation((Tag) getChildAt(index));
            }
        }else {
            if(status == Tag.STATUS_NORMAL){
                if(mCallback != null){
                    mCallback.onTagClick(index,label);
                }
            }
        }
    }

    @Override
    public void onTagLongClick(int index, int status) {
        if(mMode == MODE_EDIT){
            if(status == Tag.STATUS_NORMAL){
                changeTagsStatus(Tag.STATUS_EDIT);
            }else {
                changeTagsStatus(Tag.STATUS_NORMAL);
            }
        }else {

        }
    }

    private void changeTagsStatus(int status){
        for(int i = 0;i<getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof Tag){
                Tag tag = (Tag) getChildAt(i);
                tag.changeStatus(status);
            }
        }
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
                removeTagText(tag.getTagText(),TAG_CHANGE_REMOVE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).withLayer().start();
    }

    @Override
    public void onSpecialTagClick(int status) {
        if(status == Tag.STATUS_EDIT){
            changeTagsStatus(Tag.STATUS_NORMAL);
        }else {
            final EditText editText = new EditText(ClippingsApplication.own());
            editText.setSingleLine();
            editText.setGravity(Gravity.CENTER);
            editText.setTextColor(getResources().getColor(R.color.colorPrimary));
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.add_label)
                   .setView(editText).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String tag = editText.getText().toString();
                    if(tag == null || tag.trim().length() == 0){
                        //do nothing
                    }else {
                        addTag(new Tag(getContext(),tag));
                    }
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
                    .setCancelable(false)
                    .show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    showKeyboard(editText);
                }
            },200);
        }
    }

    private void showKeyboard(EditText editText){
        if(editText!=null){
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }
}
