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

import android.content.ContentValues;
import android.net.Uri;

import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.mvpc.UseCase;
import com.zql.android.clippings.sdk.provider.LabelContract;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class DeleteLabel extends UseCase <DeleteLabel.RequestValues,DeleteLabel.ResponseValue>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        ContentValues values = new ContentValues();
        values.put(LabelContract.TABLE_LABEL_MD5,requestValues.getMd5());
        values.put(LabelContract.TABLE_LABEL_LABEL,requestValues.getLabel());
        int i = ClippingsApplication.own()
                .getContentResolver()
                .delete(LabelContract.LABEL_URI,
                        LabelContract.LABEL_SELECTION_MD5_LABEL,
                        new String[]{requestValues.getMd5(),
                                requestValues.getLabel()});
        if(i>0){
            getUseCaseCallback().onSuccess(new ResponseValue());
        }else {
            getUseCaseCallback().onError();
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{
        private String md5;

        private String label;

        public RequestValues(String md5,String label){
            this.md5 = md5;
            this.label = label;
        }

        public String getMd5(){
            return md5;
        }

        public String getLabel(){
            return label;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{

    }
}
