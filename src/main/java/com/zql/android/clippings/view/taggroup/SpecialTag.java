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
import android.util.AttributeSet;
import android.view.View;

import com.zql.android.clippings.R;

/**
 * @author qinglian.zhang, created on 2017/3/3.
 */
public class SpecialTag extends Tag {

    private SpecialTagCallback mCallback;

    public interface SpecialTagCallback{
        void onSpecialTagClick(int currentStatus);
    }

    public SpecialTag(Context context,String text){
        this(context,text,TagsGroup.DEFAULT_SPECAIL_TAG_COLORS);
    }

    public SpecialTag(Context context, String text, int[] colors) {
        super(context, text, colors);
    }

    public SpecialTag(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecialTag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSpecialCallback(SpecialTagCallback callback){
        mCallback = callback;
    }

    @Override
    protected void onStatusChange(int status) {
        if(mStatus == STATUS_EDIT){
            setText(getResources().getString(R.string.complete));
        }else {
            setText(getResources().getString(R.string.add_label));
        }
    }

    @Override
    protected void _listener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null){
                    mCallback.onSpecialTagClick(mStatus);
                }
            }
        });
    }
}
