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

package com.zql.android.clippings.view.home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.sdk.provider.ClippingContract;
import com.zqlite.android.logly.Logly;

/**
 * @author qinglian.zhang, created on 2017/2/23.
 */
public class HomePresenter implements HomeContract.Presenter{

    private HomeContract.View mView ;

    public HomePresenter(HomeContract.View view){
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void showContent() {
        mView.showContent();
    }

    @Override
    public void loadClippings() {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == HomeContract.QUERY_CLIPPINGS_ID){
            ClippingsLoader loader = new ClippingsLoader(
                    ClippingsApplication.own(),
                    ClippingContract.CLIPPINGS_URI,
                    ClippingContract.PROJECTION_CLIPPINGS_ALL,
                    null,
                    null,
                    null);
            return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!= null){
            try {
                while (data.moveToNext()){
                    Clipping clipping = Clipping.getInstance(data);
                    Logly.d(clipping.toString());
                }
            }catch (Exception e){

            }finally {
                data.close();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
