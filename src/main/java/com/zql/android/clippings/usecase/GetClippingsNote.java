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
import com.zql.android.clippings.device.db.Clipping;

/**
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class GetClippingsNote extends UseCase<GetClippingsNote.RequestValues,GetClippingsNote.ResponseValue>{

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Clipping clipping = requestValues.getClipping();
        String noteLocation = Clipping.getNoteLocation(clipping);
        Clipping note = ClippingsApplication.own().getClippingsDB().clippingDao().getClippingNode(clipping.title,clipping.author,noteLocation);
        if(note != null){
            getUseCaseCallback().onSuccess(new ResponseValue(note));
        }
    }

    public static final class RequestValues implements UseCase.RequestValues{

        private Clipping clipping;

        public RequestValues(Clipping clipping){
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
