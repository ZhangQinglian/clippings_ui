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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.zql.android.clippings.R;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/2/23.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View{

    private HomeContract.Presenter mPresenter ;

    private ProgressFrameLayout mProgressFrameLayout;

    private RecyclerView mHomeRecyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(HomeContract.QUERY_CLIPPINGS_ID,null,mPresenter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        mProgressFrameLayout = (ProgressFrameLayout) getView().findViewById(R.id.home_progress_frame_layout);
        mHomeRecyclerView = (RecyclerView) getView().findViewById(R.id.home_recycler_view);
    }

    @Override
    protected void initData() {

    }

    public static HomeFragment getInstance(Bundle args){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showContent() {
        mProgressFrameLayout.showLoading();
    }

    private class HomeAdapter extends RecyclerView.Adapter<ClippingHolder>{

        private List<Clipping> mClippingsList = new ArrayList<>();

        @Override
        public ClippingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ClippingHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mClippingsList.size();
        }
    }

    private class ClippingHolder extends RecyclerView.ViewHolder{

        public ClippingHolder(View itemView) {
            super(itemView);
        }
    }
}
