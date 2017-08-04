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

package com.zql.android.clippings.device.view.tags;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.bridge.mvpc.UseCaseHandler;
import com.zql.android.clippings.usecase.GetLabel;
import com.zql.android.clippings.usecase.GetMD5;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class TagsPresenter implements TagsContract.Presenter {

    private TagsContract.View mView;

    public TagsPresenter(TagsContract.View view){
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
    public void getAllLabels() {
        GetLabel getLabel = new GetLabel();
        UseCaseHandler.getInstance().execute(getLabel, new GetLabel.RequestValues(""), new UseCase.UseCaseCallback<GetLabel.ResponseValue>() {
            @Override
            public void onSuccess(GetLabel.ResponseValue response) {
                mView.showLabels(response.getLabels());
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void loadMd5(final String label) {
        GetMD5 getMD5 = new GetMD5();
        UseCaseHandler.getInstance().execute(getMD5, new GetMD5.RequestValues(label), new UseCase.UseCaseCallback<GetMD5.ResponseValue>() {
            @Override
            public void onSuccess(GetMD5.ResponseValue response) {
                mView.loadMd5Finish(response.getLabels(),label);
            }

            @Override
            public void onError() {

            }
        });
    }
}
