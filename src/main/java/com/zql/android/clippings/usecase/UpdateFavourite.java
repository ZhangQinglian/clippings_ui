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

package com.zql.android.clippings.usecase;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.mvpc.UseCase;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.sdk.provider.ClippingContract;

/**
 * @author qinglian.zhang, created on 2017/3/15.
 */
public class UpdateFavourite extends UseCase<UpdateFavourite.RequestValues,UpdateFavourite.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        int id = requestValues.getId();
        int favourite = requestValues.getFavourite();
        ContentResolver resolver = ClippingsApplication.own().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ClippingContract.TABLE_CLIPPINGS_FAVOURITE,favourite);
        int index = resolver.update(ClippingContract.CLIPPINGS_URI,values,ClippingContract.CLIPPING_ID_SELECTION,new String[]{String.valueOf(id)});
        if(index == 1){
            getUseCaseCallback().onSuccess(new ResponseValue(favourite));
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{

        private int id;
        private int favourite;

        public RequestValues(int id, int favourite) {
            this.id = id;
            this.favourite = favourite;
        }

        public int getId() {
            return id;
        }

        public int getFavourite() {
            return favourite;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        private int favourite;

        public ResponseValue(int favourite) {
            this.favourite = favourite;
        }

        public int getFavourite() {
            return favourite;
        }
    }
}
