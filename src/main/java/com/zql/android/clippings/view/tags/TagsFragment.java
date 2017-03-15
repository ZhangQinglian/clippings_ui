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

package com.zql.android.clippings.view.tags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vlonjatg.progressactivity.ProgressLinearLayout;
import com.zql.android.clippings.R;
import com.zql.android.clippings.sdk.provider.Label;
import com.zql.android.clippings.view.BaseFragment;
import com.zql.android.clippings.view.tagfilter.TagFilterActivity;
import com.zql.android.clippings.view.taggroup.TagsGroup;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class TagsFragment extends BaseFragment implements TagsContract.View{

    private TagsContract.Presenter mPresenter;

    private TagsGroup mTagsGroup;

    private ProgressLinearLayout mProgressLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tags;
    }

    @Override
    protected void initView() {
        mTagsGroup = (TagsGroup) getView().findViewById(R.id.tags_group);
        mProgressLayout = (ProgressLinearLayout) getView().findViewById(R.id.progress_linear_layout);
        mTagsGroup.addCallback(new TagsGroup.TagsGroupCallback() {
            @Override
            public void onTagChanged(String tagText, int status) {

            }

            @Override
            public void onTagClick(int index, String label) {
                Logly.d("   touch label : " + label);
                mPresenter.loadMd5(label);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logly.d("   TagsFragment : onAttach" );

    }

    @Override
    public void onResume() {
        super.onResume();
        Logly.d("   TagsFragment : onResume" );
        mPresenter.getAllLabels();
    }

    public static TagsFragment getInstance(Bundle bundle){
        TagsFragment fragment = new TagsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setPresenter(TagsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLabels(List<Label> labels) {
        if(labels.size() < 1){
            mProgressLayout.showEmpty(R.drawable.ic_empty,"暂无内容","请添加");
        }else {
            mTagsGroup.showLables(labels);
            mProgressLayout.showContent();
        }
    }

    @Override
    public void loadMd5Finish(List<String> md5s,String label) {
        Intent intent = new Intent(getActivity(),TagFilterActivity.class);
        intent.putStringArrayListExtra(TagFilterActivity.FILTER_MD5S, (ArrayList<String>) md5s);
        intent.putExtra(TagFilterActivity.FILTER_TAG,label);
        startActivity(intent);
    }
}
