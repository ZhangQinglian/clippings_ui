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

import com.zql.android.clippings.mvpc.IContract;
import com.zql.android.clippings.mvpc.IPresenter;
import com.zql.android.clippings.mvpc.IView;
import com.zql.android.clippings.sdk.parser.Clipping;

/**
 * @author qinglian.zhang, created on 2017/3/1.
 */
public class DetailContract implements IContract {

    public static final String PICK_CLIPPING_ID = "pick_clipping_id";

    public interface Presenter extends IPresenter{
        void getClipping(int id);

        void getClippingsNote(Clipping clipping);
    }

    public interface View extends IView<Presenter>{
        void clippingUpdate(Clipping clipping);

        void updateNote(Clipping clipping);
    }
}
