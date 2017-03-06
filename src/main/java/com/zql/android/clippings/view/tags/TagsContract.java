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

package com.zql.android.clippings.view.tags;

import com.zql.android.clippings.mvpc.IContract;
import com.zql.android.clippings.mvpc.IPresenter;
import com.zql.android.clippings.mvpc.IView;

/**
 * @author qinglian.zhang, created on 2017/3/6.
 */
public class TagsContract implements IContract {

    public interface Presenter extends IPresenter{

    }

    public interface View extends IView<Presenter>{

    }
}
