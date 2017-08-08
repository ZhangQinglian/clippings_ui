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

package com.zql.android.clippings.device.view.details;

import android.graphics.Bitmap;

import com.zql.android.clippings.bridge.mvpc.IContract;
import com.zql.android.clippings.bridge.mvpc.IPresenter;
import com.zql.android.clippings.bridge.mvpc.IView;
import com.zql.android.clippings.device.db.Clipping;
import com.zql.android.clippings.device.db.Label;

import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class DetailContract implements IContract {

    public static final String PICK_CLIPPING_ID = "pick_clipping_id";

    public interface Presenter extends IPresenter{
        void getClipping(int id);

        void getClippingsNote(Clipping clipping);

        void addLabel(String md5,String label);

        void loadLabels(String md5);

        void deleteLabel(String md5,String label);

        /**
         * 更新数据库
         * @param clipping
         * @param favourite 目标值
         */
        void updateFavourite(Clipping clipping,int favourite);

        void updateStatus(Clipping clipping,int status);

        void onMenuClick(int id);
    }

    public interface View extends IView<Presenter>{
        void clippingUpdate(Clipping clipping);

        void updateNote(Clipping clipping);

        void showLabels(List<Label> labels);

        /**
         * 数据库更新成功后的回调
         * @param favourite
         */
        void updateFavourite(int favourite);

        void updateStatus(int status);

        void onMeneClick(int id);
    }
}
