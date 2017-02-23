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

package com.zql.android.clippings.view.home;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;

import com.zql.android.clippings.mvpc.IContract;
import com.zql.android.clippings.mvpc.IPresenter;
import com.zql.android.clippings.mvpc.IView;
import com.zql.android.clippings.sdk.parser.Clipping;

import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/2/23.
 */
public class HomeContract implements IContract {

    /**
     * {@link ClippingsLoader}对应id
     */
    public static final int QUERY_CLIPPINGS_ID = 0x01;

    public interface Presenter extends IPresenter,LoaderManager.LoaderCallbacks<Cursor>{
        void showContent();

        void loadClippings();
    }

    public interface View extends IView<Presenter>{
        void showContent();
        void updateClippings(List<Clipping> clippings);
    }
}
