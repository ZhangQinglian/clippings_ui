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

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.CursorLoader;

import com.zql.android.clippings.sdk.provider.ClippingContract;
import com.zqlite.android.logly.Logly;

/**
 * @author qinglian.zhang, created on 2017/2/23.
 */
public class ClippingsLoader extends CursorLoader {

    private ContentObserver mContentObserver ;
    public ClippingsLoader(Context context) {
        super(context);
    }

    public ClippingsLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mContentObserver == null){
            mContentObserver = new ContentObserver(new Handler()) {
                @Override
                public boolean deliverSelfNotifications() {
                    return super.deliverSelfNotifications();
                }

                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    onContentChanged();
                }

                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange, uri);
                    onContentChanged();
                }
            };
            getContext().getContentResolver().registerContentObserver(ClippingContract.CLIPPINGS_URI,true,mContentObserver);
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        super.onReset();
        if(mContentObserver != null){
            getContext().getContentResolver().unregisterContentObserver(mContentObserver);
        }
    }

    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    @Override
    public void deliverResult(Cursor cursor) {
        super.deliverResult(cursor);
    }
}
