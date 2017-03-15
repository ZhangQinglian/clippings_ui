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

package com.zql.android.clippings.view.details;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;

import com.zql.android.clippings.R;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.view.BaseActivity;

/**
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class DetailActivity extends BaseActivity implements DetailFragment.DetailFragmentCallback{

    private DetailContract.Presenter mPresenter;

    private DetailFragment mView;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        int id = getIntent().getIntExtra(DetailContract.PICK_CLIPPING_ID,-1);
        if(id == -1){
            throw new RuntimeException("id could not be -1");
        }
        Bundle bundle = new Bundle();
        bundle.putInt(DetailContract.PICK_CLIPPING_ID,id);
        mView = DetailFragment.getInstance(bundle);
        mPresenter = new DetailPresenter(mView);
        addFragment(R.id.detail_container,mView);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void clippingUpdate(Clipping clipping) {
        //getSupportActionBar().setTitle(clipping.title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
