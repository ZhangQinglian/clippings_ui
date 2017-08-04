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
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class GetClippingById extends UseCase<GetClippingById.RequestValues,GetClippingById.ResponseValue>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        int clippingId = requestValues.getClippingId();
        Clipping clipping = ClippingsApplication.own().getClippingsDB().clippingDao().getClippingById(clippingId);
        ResponseValue responseValue = new ResponseValue(clipping);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static final class RequestValues implements UseCase.RequestValues{

        private int clippingId ;

        public RequestValues( int clippingId){
            this.clippingId = clippingId;
        }

        public int getClippingId(){
            return clippingId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        private Clipping clipping;

        public ResponseValue(Clipping clipping){
            this.clipping = clipping;
        }

        public Clipping getClipping(){
            return clipping;
        }
    }
}
