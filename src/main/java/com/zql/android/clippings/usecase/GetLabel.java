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


import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.Label;

import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class GetLabel extends UseCase <GetLabel.RequestValues,GetLabel.ResponseValue>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        List<Label> labelList ;
        if(requestValues.md5 == null || requestValues.md5.trim().length() == 0){
            labelList = ClippingsApplication.own().getClippingsDB().clippingDao().getAllLabels();
        }else {
            labelList = ClippingsApplication.own().getClippingsDB().clippingDao().getLabelsByMd5(requestValues.md5);
        }
        if(labelList != null){
            getUseCaseCallback().onSuccess(new ResponseValue(labelList));
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
