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
import android.net.Uri;

import com.android.annotations.NonNull;
import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.mvpc.UseCase;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.sdk.provider.ClippingContract;

/**
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class GetClippingsNote extends UseCase<GetClippingsNote.RequestValues,GetClippingsNote.ResponseValue>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Clipping clipping = requestValues.getClipping();
        if(clipping.type == Clipping.K_CLIPPING_TYPE_LABEL){
            ContentResolver resolver = ClippingsApplication.own().getContentResolver();
            Cursor cursor = resolver.query(ClippingContract.CLIPPINGS_URI,
                    ClippingContract.PROJECTION_CLIPPINGS_ALL,
                    ClippingContract.CLIPPING_NOTE_SELECTION,
                    new String[]{clipping.title,clipping.author,Clipping.getNoteLocation(clipping),String.valueOf(Clipping.K_CLIPPING_TYPE_NOTE)},null);
            if(cursor != null){
                try {
                    cursor.moveToFirst();
                    Clipping note = Clipping.getInstance(cursor);
                    ResponseValue responseValue = new ResponseValue(note);
                    getUseCaseCallback().onSuccess(responseValue);
                }catch (Exception e){
                    getUseCaseCallback().onError();
                }
                finally {
                    cursor.close();
                }
            }
        }


    }

    public static final class RequestValues implements UseCase.RequestValues{

        private Clipping clipping;

        public RequestValues(@NonNull Clipping clipping){
            this.clipping = clipping;
        }

        public Clipping getClipping(){
            return clipping;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        private Clipping note;

        public ResponseValue(Clipping clipping){
            this.note = clipping;
        }

        public Clipping getNote(){
            return note;
        }
    }
}
