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

package com.zql.android.clippings.device.view.details;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zql.android.clippings.R;
import com.zql.android.clippings.device.db.Clipping;
import com.zql.android.clippings.device.view.BaseActivity;
import com.zql.android.clippings.device.view.custom.DecorViewProxy;

/**
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class DetailActivity extends BaseActivity implements DetailFragment.DetailFragmentCallback{

    private DetailContract.Presenter mPresenter;

    private DetailFragment mView;

    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DecorViewProxy decorViewProxy = new DecorViewProxy().bind(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {

        toolbar = findViewById(R.id.toolbar);
        int id = getIntent().getIntExtra(DetailContract.PICK_CLIPPING_ID,-1);
        if(id == -1){
            throw new RuntimeException("id could not be -1");
        }
        Bundle bundle = new Bundle();
        bundle.putInt(DetailContract.PICK_CLIPPING_ID,id);
        mView = DetailFragment.getInstance(bundle);
        mPresenter = new DetailPresenter(mView);
        addFragment(R.id.detail_container,mView);
        toolbar.inflateMenu(R.menu.detail_menus);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_favourite:
                        mPresenter.onMenuClick(R.id.menu_favourite);
                        break;
                    case R.id.menu_hide:
                        mPresenter.onMenuClick(R.id.menu_hide);
                        break;
                }
                return false;
            }
        });
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_toolbar_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void clippingUpdate(Clipping clipping) {
        toolbar.setTitle(clipping.title);
    }

    @Override
    public void updateFavourite(int favourite) {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_favourite);
        if(favourite == Clipping.K_CLIPPINGS_FAVOURITE){
            menuItem.setIcon(getResources().getDrawable(R.drawable.menu_favourite_1));
        }else {
            menuItem.setIcon(getResources().getDrawable(R.drawable.menu_favourite_0));
        }

    }

    @Override
    public void updateStatus(int status) {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_hide);
        if(status == Clipping.K_CLIPPING_STATUS_NORMAL){
            menuItem.setTitle(R.string.menu_hide);
        }else {
            menuItem.setTitle(R.string.menu_show);
        }
    }
}
