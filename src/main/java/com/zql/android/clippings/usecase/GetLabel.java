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
import android.database.Cursor;
import android.net.Uri;

import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.mvpc.UseCase;
import com.zql.android.clippings.sdk.provider.Label;
import com.zql.android.clippings.sdk.provider.LabelContract;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class GetLabel extends UseCase <GetLabel.RequestValues,GetLabel.ResponseValue>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        ContentResolver resolver = ClippingsApplication.own().getContentResolver();
        Cursor cursor = resolver.query(LabelContract.LABEL_URI,LabelContract.LABEL_PROJECTION_ALL,LabelContract.LABEL_SELECTION_MD5,new String[]{requestValues.getMd5()},null);
        if(cursor != null){
            try {
                List<Label> labels = new ArrayList<>();
                while (cursor.moveToNext()){
                    Label label = new Label(cursor);
                    labels.add(label);
                }
                getUseCaseCallback().onSuccess(new ResponseValue(labels));
            }catch (Exception e){
                e.printStackTrace();
                getUseCaseCallback().onError();
            }finally {
                cursor.close();
            }
        }

    }

    public static final class RequestValues implements UseCase.RequestValues{
        private String md5;


        public RequestValues(String md5){
            this.md5 = md5;
        }

        public String getMd5(){
            return md5;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        private List<Label> labels;

        public ResponseValue(List<Label> labels){
            this.labels = labels;
        }

        public List<Label> getLabels(){
            return labels;
        }
    }
}
