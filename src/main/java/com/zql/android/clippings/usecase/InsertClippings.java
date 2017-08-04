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
import com.zql.android.clippings.device.db.ClippingDao;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/8/3.
 */

public class InsertClippings extends UseCase<InsertClippings.RequestValue,InsertClippings.ResponseValue> {


    @Override
    protected void executeUseCase(RequestValue requestValues) {
        ClippingDao dao = ClippingsApplication.own().getClippingsDB().clippingDao();
        List<Clipping> rawClippings = requestValues.clippings;
        List<String> md5 = dao.getAllMd5();
        List<Clipping> filterClippings = new ArrayList<>();
        for(int i = 0;i<rawClippings.size();i++){
            Clipping c = rawClippings.get(i);
            if(!md5.contains(c.md5)){
                filterClippings.add(c);
                Logly.d("    " + c.content + "   is new");
            }
        }
        dao.insertClippings(filterClippings);
    }

    public static final class RequestValue implements UseCase.RequestValues{
        public List<Clipping> clippings ;

        public RequestValue(List<Clipping> clippings){
            this.clippings = clippings;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{

    }
}
