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
import android.content.Intent;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zql.android.clippings.R;
import com.zql.android.clippings.sdk.parser.ClippingsParser;
import com.zql.android.clippings.sdk.provider.ClippingContract;
import com.zql.android.clippings.view.home.HomeContract;
import com.zql.android.clippings.view.home.ClippingsFragment;
import com.zql.android.clippings.view.home.HomePresenter;
import com.zql.android.clippings.view.tags.TagsContract;
import com.zql.android.clippings.view.tags.TagsFragment;
import com.zql.android.clippings.view.tags.TagsPresenter;
import com.zqlite.android.logly.Logly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends BaseActivity {

    private BottomBar mBottomBar;

    private ClippingsFragment mHomeFragment;

    private HomeContract.Presenter mHomePresenter;

    private TagsFragment mTagsFragment;

    private TagsContract.Presenter mTagsPresenter;

    private ClippingsFragment mFavouriteFragment;

    private HomeContract.Presenter mFavouritePresenter;

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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        mHomeFragment = ClippingsFragment.getInstance(null);
        mHomePresenter = new HomePresenter(mHomeFragment);
        mTagsFragment = TagsFragment.getInstance(null);
        mTagsPresenter = new TagsPresenter(mTagsFragment);
        Bundle bundle = new Bundle();
        bundle.putString(ClippingsFragment.ARG_TAG_FAVOURITE,ClippingsFragment.ARG_TAG_FAVOURITE);
        mFavouriteFragment = ClippingsFragment.getInstance(bundle);
        mFavouritePresenter = new HomePresenter(mFavouriteFragment);

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
                        showFavourite();
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
        if(mHomeFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().show(mHomeFragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, mHomeFragment).commit();
        }
        if(mTagsFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(mTagsFragment).commit();
        }
        if(mFavouriteFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(mFavouriteFragment).commit();
        }
    }

    private void showLabel() {
        if(mTagsFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().show(mTagsFragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.main_container,mTagsFragment).commit();
        }
        if(mHomeFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(mHomeFragment).commit();
        }
        if(mFavouriteFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(mFavouriteFragment).commit();
        }
    }

    private void showFavourite(){
        if(mFavouriteFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().show(mFavouriteFragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.main_container,mFavouriteFragment).commit();
        }
        if(mTagsFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(mTagsFragment).commit();
        }
        if(mHomeFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(mHomeFragment).commit();
        }
    }

}
