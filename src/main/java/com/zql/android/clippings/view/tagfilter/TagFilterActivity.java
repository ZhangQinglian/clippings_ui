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

package com.zql.android.clippings.view.tagfilter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zql.android.clippings.R;
import com.zql.android.clippings.view.BaseActivity;
import com.zql.android.clippings.view.home.HomeContract;
import com.zql.android.clippings.view.home.ClippingsFragment;
import com.zql.android.clippings.view.home.HomePresenter;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class TagFilterActivity extends BaseActivity {

    private ClippingsFragment mClippingsFragment;

    private HomeContract.Presenter mPresenter;

    public static final String FILTER_MD5S = "filter_md5s";

    public static final String FILTER_TAG = "filter_tag";

    private List<String> mMd5s ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String tag = getIntent().getExtras().getString(FILTER_TAG);
        if(tag == null || tag.trim().length() == 0){
            throw new RuntimeException("invalid tag");
        }
        getSupportActionBar().setTitle(tag);
        mMd5s = intent.getExtras().getStringArrayList(FILTER_MD5S);
        Logly.d("" + mMd5s.size());
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(FILTER_MD5S, (ArrayList<String>) mMd5s);
        mClippingsFragment = ClippingsFragment.getInstance(bundle);
        mPresenter = new HomePresenter(mClippingsFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.tag_filter_container, mClippingsFragment).commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tag_filter;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
