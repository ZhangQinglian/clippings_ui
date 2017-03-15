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

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.mvpc.UseCase;
import com.zql.android.clippings.mvpc.UseCaseHandler;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.usecase.AddLabel;
import com.zql.android.clippings.usecase.DeleteLabel;
import com.zql.android.clippings.usecase.GetClippingById;
import com.zql.android.clippings.usecase.GetClippingsNote;
import com.zql.android.clippings.usecase.GetLabel;
import com.zql.android.clippings.usecase.UpdateFavourite;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Executors;

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
        GetClippingById getClippingById = new GetClippingById();
        UseCaseHandler.getInstance().execute(getClippingById, new GetClippingById.RequestValues(id), new UseCase.UseCaseCallback<GetClippingById.ResponseValue>() {
            @Override
            public void onSuccess(GetClippingById.ResponseValue response) {
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

    @Override
    public void addLabel(String md5, String label) {
        AddLabel addLabel = new AddLabel();
        UseCaseHandler.getInstance().execute(addLabel, new AddLabel.RequestValues(md5, label), new UseCase.UseCaseCallback<AddLabel.ResponseValue>() {
            @Override
            public void onSuccess(AddLabel.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void loadLabels(String md5) {
        GetLabel getLabel = new GetLabel();
        UseCaseHandler.getInstance().execute(getLabel, new GetLabel.RequestValues(md5), new UseCase.UseCaseCallback<GetLabel.ResponseValue>() {
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
    public void deleteLabel(String md5, String label) {
        DeleteLabel deleteLabel = new DeleteLabel();
        UseCaseHandler.getInstance().execute(deleteLabel, new DeleteLabel.RequestValues(md5, label), new UseCase.UseCaseCallback<DeleteLabel.ResponseValue>() {
            @Override
            public void onSuccess(DeleteLabel.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void saveDetailScreen(final Bitmap bitmap,Clipping clipping) {
        final String fileName = clipping.title+"-"+clipping.location+".jpeg";
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    bitmap.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void updateFavourite(int id, int favourite) {
        UpdateFavourite updateFavourite = new UpdateFavourite();
        UseCaseHandler.getInstance().execute(updateFavourite, new UpdateFavourite.RequestValues(id, favourite), new UseCase.UseCaseCallback<UpdateFavourite.ResponseValue>() {
            @Override
            public void onSuccess(UpdateFavourite.ResponseValue response) {
                mView.updateFavourite(response.getFavourite());
            }

            @Override
            public void onError() {

            }
        });
    }

}
