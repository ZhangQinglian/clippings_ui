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

import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.db.Clipping;

/**
 * @author qinglian.zhang, created on 2017/3/15.
 */
public class UpdateFavourite extends UseCase<UpdateFavourite.RequestValues,UpdateFavourite.ResponseValue> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Clipping clipping = requestValues.clipping;
        int favourite = requestValues.favourite;
        clipping.favourite = favourite;
        int c = ClippingsApplication.own().getClippingsDB().clippingDao().updateClipping(clipping);
        if( c == 1){
            getUseCaseCallback().onSuccess(new ResponseValue(favourite));
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{

        private Clipping clipping;
        private int favourite;

        public RequestValues(Clipping clipping,int favourite) {
            this.clipping = clipping;
            this.favourite = favourite;
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
