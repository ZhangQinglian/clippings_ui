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

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.zql.android.clippings.view.details.DetailActivity;
import com.zql.android.clippings.view.details.DetailContract;
import com.zql.android.clippings.view.tagfilter.TagFilterActivity;
import com.zqlite.android.logly.Logly;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author qinglian.zhang, created on 2017/2/23.
 */
public class HomeFragment extends BaseFragment implements HomeContract.View{

    private final int kGridSpace = 8;

    private HomeContract.Presenter mPresenter ;

    private ProgressFrameLayout mProgressFrameLayout;

    private RecyclerView mHomeRecyclerView;

    private HomeAdapter mHomeAdapter ;

    private List<String> mMd5s ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(HomeContract.QUERY_CLIPPINGS_ID,null,mPresenter);
        Bundle args = getArguments();
        if(args != null){
            mMd5s = getArguments().getStringArrayList(TagFilterActivity.FILTER_MD5S);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState != null){
            Logly.d("    "   + savedInstanceState.getString("a"));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("a","a");
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

        HomeClippingItemTouchCallback homeClippingItemTouchCallback = new HomeClippingItemTouchCallback(
                ItemTouchHelper.LEFT|
                ItemTouchHelper.RIGHT|
                ItemTouchHelper.DOWN|
                ItemTouchHelper.UP,
                ItemTouchHelper.ACTION_STATE_IDLE);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(homeClippingItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mHomeRecyclerView);

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

    public void hideContent(){
        mProgressFrameLayout.showEmpty(R.drawable.ic_empty,"暂无内容","请导入");
    }
    @Override
    public void updateClippings(List<Clipping> clippings) {
        if(clippings.size() > 0){
            if(mMd5s != null){
                List<Clipping> filterClippings = new ArrayList<>();
                for(String md5:mMd5s){
                    for(int i = 0;i<clippings.size();i++){
                        if(md5.equals(clippings.get(i).md5)){
                            filterClippings.add(clippings.get(i));
                        }
                    }
                }
                mHomeAdapter.update(filterClippings);
            }else {
                mHomeAdapter.update(clippings);
            }
            showContent();
        }else {
            hideContent();
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

    private class HomeClippingItemTouchCallback extends ItemTouchHelper.SimpleCallback {

        View view ;
        public HomeClippingItemTouchCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleX", 1.0f, 1.2f);
                ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1.0f, 1.2f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
                animatorSet.start();
                view = viewHolder.itemView;
            }
            if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1.0f);
                ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
                animatorSet.start();
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

        public Clipping getItem(int index){
            return mClippingsList.get(index);
        }
    }

    private class ClippingHolder extends RecyclerView.ViewHolder{

        private final ListitemClippingBinding binding;
        public ClippingHolder(final ListitemClippingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Object clipping){
            binding.setVariable(BR.clipping,clipping);
            binding.executePendingBindings();
            ViewCompat.setTransitionName(binding.getRoot(),getAdapterPosition() + "_clipping");
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(final ObservableEmitter<Integer> e) throws Exception {
                    binding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!e.isDisposed()){
                                e.onNext(getAdapterPosition());
                            }else {
                                e.onComplete();
                            }
                        }
                    });
                }
            }).throttleFirst(1,TimeUnit.SECONDS).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    onClippingClick(integer,binding.getRoot());
                }
            });
            CardView cardView = (CardView) binding.getRoot().findViewById(R.id.card_view);
            cardView.setCardBackgroundColor(Color.WHITE);
        }

    }


    private void onClippingClick(int index,View view){
        Intent intent = new Intent(ClippingsApplication.own(), DetailActivity.class);
        intent.putExtra(DetailContract.PICK_CLIPPING_ID,mHomeAdapter.getItem(index).id);
        getActivity().startActivity(intent);
    }


}
