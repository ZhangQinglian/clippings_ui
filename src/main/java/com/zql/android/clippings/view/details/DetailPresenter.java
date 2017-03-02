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

package com.zql.android.clippings.view.details;

import com.zql.android.clippings.mvpc.UseCase;
import com.zql.android.clippings.mvpc.UseCaseHandler;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.usecase.GetClipping;
import com.zql.android.clippings.usecase.GetClippingsNote;
import com.zqlite.android.logly.Logly;

/**
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class DetailPresenter implements DetailContract.Presenter {

    private DetailContract.View mView;

    public DetailPresenter(DetailContract.View view){
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
    public void getClipping(int id) {
        GetClipping getClipping = new GetClipping();
        UseCaseHandler.getInstance().execute(getClipping, new GetClipping.RequestValues(id), new UseCase.UseCaseCallback<GetClipping.ResponseValue>() {
            @Override
            public void onSuccess(GetClipping.ResponseValue response) {
                Clipping clipping = response.getClipping();
                mView.clippingUpdate(clipping);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void getClippingsNote(Clipping clipping) {
        GetClippingsNote getClippingsNote = new GetClippingsNote();
        UseCaseHandler.getInstance().execute(getClippingsNote, new GetClippingsNote.RequestValues(clipping), new UseCase.UseCaseCallback<GetClippingsNote.ResponseValue>() {
            @Override
            public void onSuccess(GetClippingsNote.ResponseValue response) {
                Clipping note = response.getNote();
                mView.updateNote(note);
            }

            @Override
            public void onError() {

            }
        });
    }

}
