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

package com.zql.android.clippings.view;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zql.android.clippings.R;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.sdk.parser.ClippingsParser;
import com.zql.android.clippings.sdk.provider.ClippingContract;
import com.zql.android.clippings.view.home.HomeContract;
import com.zql.android.clippings.view.home.HomeFragment;
import com.zql.android.clippings.view.home.HomePresenter;
import com.zqlite.android.logly.Logly;

import java.io.IOException;

public class MainActivity extends BaseActivity {

    private BottomBar mBottomBar;

    private HomeFragment mHomeFragment;

    private HomeContract.Presenter mHomePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logly.setGlobalTag(new Logly.Tag(Logly.FLAG_THREAD_NAME, "Clippings", Logly.DEBUG));

    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mBottomBar = (BottomBar) findViewById(R.id.main_bottom_bar);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
            }
        });

        mHomeFragment = HomeFragment.getInstance(null);
        mHomePresenter = new HomePresenter(mHomeFragment);
        showFragment(R.id.main_container,mHomeFragment);
    }

    @Override
    protected void initData() {

    }


    private void testQuery() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ClippingContract.CLIPPINGS_URI, ClippingContract.PROJECTION_CLIPPINGS_ALL, null, null, null);
        if (cursor != null) {
            try {

                while (cursor.moveToNext()) {
                    Clipping clipping = Clipping.getInstance(cursor);
                    Logly.d(clipping.toString());
                }

            } catch (Exception e) {

            } finally {
                cursor.close();
            }
        }

    }

    private void initDatabase() {
        try {
            ClippingsParser.own().parse(getAssets().open("My Clippings.txt"), this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
