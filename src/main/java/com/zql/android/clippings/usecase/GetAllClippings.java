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
import com.zql.android.clippings.device.db.Clipping;

import java.util.List;

/**
 * Created by scott on 2017/8/3.
 */

public class GetAllClippings extends UseCase<GetAllClippings.RequestValues,GetAllClippings.ResponseValues> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {

        boolean isFavourite = requestValues.isFavourite;

        List<Clipping> clippings;
        if(isFavourite){
            clippings = ClippingsApplication.own().getClippingsDB().clippingDao().getAllFavourite();
        }else {
            clippings = ClippingsApplication.own().getClippingsDB().clippingDao().getAllClippings();
        }

        ResponseValues responseValues =  new ResponseValues(clippings);
        getUseCaseCallback().onSuccess(responseValues);
    }

    public static final class RequestValues implements UseCase.RequestValues{
        private boolean isFavourite = false;

        public RequestValues(boolean isFavourite){
            this.isFavourite = isFavourite;
        }
    }

    public static final class ResponseValues implements UseCase.ResponseValue{
        public List<Clipping> clippings ;

        public ResponseValues(List<Clipping> clippingList){
            clippings = clippingList;
        }
    }
}
