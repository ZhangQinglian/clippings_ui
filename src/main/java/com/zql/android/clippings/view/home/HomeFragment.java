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

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.zql.android.clippings.BR;
import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.R;
import com.zql.android.clippings.databinding.ListitemClippingBinding;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/2/23.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View{

    private final int kGridSpace = 8;

    private HomeContract.Presenter mPresenter ;

    private ProgressFrameLayout mProgressFrameLayout;

    private RecyclerView mHomeRecyclerView;

    private HomeAdapter mHomeAdapter ;

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
        mHomeAdapter = new HomeAdapter();
        mHomeRecyclerView.setAdapter(mHomeAdapter);

        mHomeRecyclerView.setHasFixedSize(true);
        mHomeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHomeRecyclerView.addItemDecoration(new HomeClippingItemDecoration(kGridSpace));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mHomeRecyclerView.setLayoutManager(staggeredGridLayoutManager);
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
        mProgressFrameLayout.showContent();
    }

    @Override
    public void updateClippings(List<Clipping> clippings) {
        if(clippings.size() > 0){
            mHomeAdapter.update(clippings);
            showContent();
        }
    }

    private class HomeClippingItemDecoration extends RecyclerView.ItemDecoration{

        private int size ;

        public HomeClippingItemDecoration(int size){
            this.size = size;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = getDipFromDimension(size);
            StaggeredGridLayoutManager.LayoutParams sgll = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int index = sgll.getSpanIndex();
            if(index % 2 == 0){
                outRect.left = getDipFromDimension(size);
                outRect.right = getDipFromDimension(size/2);
            }else {
                outRect.left = getDipFromDimension(size/2);
                outRect.right = getDipFromDimension(size);
            }
            if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1){
                outRect.bottom = getDipFromDimension(size);
            }
        }
    }
    private class HomeAdapter extends RecyclerView.Adapter<ClippingHolder>{

        private List<Clipping> mClippingsList = new ArrayList<>();

        public void update(List<Clipping> clippings){
            mClippingsList.clear();
            mClippingsList.addAll(clippings);
            notifyDataSetChanged();
        }
        @Override
        public ClippingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ClippingsApplication.own());
            ListitemClippingBinding binding = ListitemClippingBinding.inflate(inflater);
            ClippingHolder holder = new ClippingHolder(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(ClippingHolder holder, int position) {
            Object clipping = mClippingsList.get(position);
            holder.bind(clipping);
        }

        @Override
        public int getItemCount() {
            return mClippingsList.size();
        }
    }

    private class ClippingHolder extends RecyclerView.ViewHolder{

        private final ListitemClippingBinding binding;
        public ClippingHolder(ListitemClippingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object clipping){
            binding.setVariable(BR.clipping,clipping);
            binding.executePendingBindings();
        }
    }
}
