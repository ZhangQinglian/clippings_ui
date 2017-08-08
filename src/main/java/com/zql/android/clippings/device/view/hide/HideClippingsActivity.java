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

package com.zql.android.clippings.device.view.hide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zql.android.clippings.R;
import com.zql.android.clippings.device.view.BaseActivity;
import com.zql.android.clippings.device.view.custom.DecorViewProxy;
import com.zql.android.clippings.device.view.home.ClippingsFragment;
import com.zql.android.clippings.device.view.home.HomeContract;
import com.zql.android.clippings.device.view.home.HomePresenter;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class HideClippingsActivity extends BaseActivity {

    private ClippingsFragment mClippingsFragment;

    private HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DecorViewProxy().bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.setting_hide);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_toolbar_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString(ClippingsFragment.ARG_TAG_HIDE,ClippingsFragment.ARG_TAG_HIDE);
        mClippingsFragment = ClippingsFragment.getInstance(bundle);
        mPresenter = new HomePresenter(mClippingsFragment);
        getSupportFragmentManager().beginTransaction().add(R.id.tag_filter_container, mClippingsFragment).commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hide_clippings;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
