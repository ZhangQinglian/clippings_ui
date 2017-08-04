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
import android.database.Cursor;

import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.db.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class GetMD5 extends UseCase <GetMD5.RequestValues,GetMD5.ResponseValue>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<String> md5s = ClippingsApplication.own().getClippingsDB().clippingDao().getMd5sByLabel(getRequestValues().label);
        if(md5s != null){
            getUseCaseCallback().onSuccess(new ResponseValue(md5s));
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{
        private String label;

        public RequestValues(String label){
            this.label = label;
        }

        public String getLabel(){
            return label;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        private List<String> labels;

        public ResponseValue(List<String> labels){
            this.labels = labels;
        }

        public List<String> getLabels(){
            return labels;
        }
    }
}
