/*******************************************************************************
 * Copyright 2017-present, Clippings Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.zql.android.clippings.view;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.os.Bundle;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zql.android.clippings.R;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.sdk.parser.ClippingsParser;
import com.zql.android.clippings.sdk.provider.ClippingContract;
import com.zql.android.clippings.view.details.DetailFragment;
import com.zql.android.clippings.view.home.HomeContract;
import com.zql.android.clippings.view.home.HomeFragment;
import com.zql.android.clippings.view.home.HomePresenter;
import com.zql.android.clippings.view.tags.TagsContract;
import com.zql.android.clippings.view.tags.TagsFragment;
import com.zql.android.clippings.view.tags.TagsPresenter;
import com.zqlite.android.logly.Logly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends BaseActivity {

    private BottomBar mBottomBar;

    private HomeFragment mHomeFragment;

    private HomeContract.Presenter mHomePresenter;

    private TagsFragment mTagsFragment;

    private TagsContract.Presenter mTagsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logly.setGlobalTag(new Logly.Tag(Logly.FLAG_THREAD_NAME, "Clippings", Logly.DEBUG));
        Intent intent = getIntent();
        if (intent != null) {
            parseClippings(getIntent());
        }

        initPermission();
    }

    private void initPermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }

    private void parseClippings(Intent intent) {
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            ClipData clipData = intent.getClipData();
            ClipData.Item item = clipData.getItemAt(0);
            if (item.getUri() != null) {
                String path = item.getUri().getPath();
                try {
                    File file = new File(path);
                    if(ClippingsParser.CLIPPINGS_NAME.equals(file.getName())){
                        InputStream inputStream = new FileInputStream(file);
                        initDatabase(inputStream);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Logly.d("   " + path);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mHomeFragment = HomeFragment.getInstance(null);
        mHomePresenter = new HomePresenter(mHomeFragment);
        mTagsFragment = TagsFragment.getInstance(null);
        mTagsPresenter = new TagsPresenter(mTagsFragment);


        mBottomBar = (BottomBar) findViewById(R.id.main_bottom_bar);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_all:
                        showHome();
                        break;
                    case R.id.tab_label:
                        showLabel();
                        break;
                    case R.id.tab_like:
                        break;
                }
            }
        });
        mBottomBar.setDefaultTab(R.id.tab_all);

    }

    @Override
    protected void initData() {

    }

    private void initDatabase(InputStream inputStream) {
        ClippingsParser.own().parse(inputStream, this);
    }

    private void showHome() {
        Logly.d("     tab all");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,mHomeFragment).commit();
    }

    private void showLabel() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,mTagsFragment).commit();
    }
}
