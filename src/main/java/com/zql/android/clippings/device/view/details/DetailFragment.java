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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zql.android.clippings.R;
import com.zql.android.clippings.databinding.FragmentDetailBinding;
import com.zql.android.clippings.device.db.Clipping;
import com.zql.android.clippings.device.db.Label;
import com.zql.android.clippings.device.view.BaseFragment;
import com.zql.android.clippings.device.view.taggroup.TagsGroup;
import com.zqlite.android.logly.Logly;

import java.util.List;


/**
 * @author qinglian.zhang, created on 2017/2/24.
 */
public class DetailFragment extends BaseFragment implements DetailContract.View{

    private DetailContract.Presenter mPresenter;

    private FragmentDetailBinding mDetailBinding;

    private TagsGroup mTagsGroup ;

    private Clipping mCurrentClipping ;

    private View mDetailLayout;

    private Menu mMenu;

    public interface DetailFragmentCallback{
        void clippingUpdate(Clipping clipping);
        void updateFavourite(int favourite);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDetailBinding = FragmentDetailBinding.inflate(getActivity().getLayoutInflater());
        View view = mDetailBinding.getRoot();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        int id = args.getInt(DetailContract.PICK_CLIPPING_ID);
        mPresenter.getClipping(id);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initView() {
        mTagsGroup = getView().findViewById(R.id.tags_group);
        mTagsGroup.addCallback(new TagsGroup.TagsGroupCallback() {
            @Override
            public void onTagChanged(String tagText, int status) {
                Logly.d("     tagText : " + tagText +  "   status : " + status);
                if(status == TagsGroup.TAG_CHANGE_ADD){
                    mPresenter.addLabel(mCurrentClipping.md5,tagText);
                }
                if(status == TagsGroup.TAG_CHANGE_REMOVE){
                    mPresenter.deleteLabel(mCurrentClipping.md5,tagText);
                }
            }

            @Override
            public void onTagClick(int index, String label) {

            }
        });
        mDetailLayout = getView().findViewById(R.id.detail_layout);
        mDetailLayout.setDrawingCacheEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void clippingUpdate(Clipping clipping) {
        ((DetailFragmentCallback)(getActivity())).clippingUpdate(clipping);
        mDetailBinding.setClipping(clipping);
        mCurrentClipping = clipping;
        if(clipping.type == Clipping.K_CLIPPING_TYPE_LABEL){
            mPresenter.getClippingsNote(clipping);
        }
        //更新完clipping数据后加载对应label
        mPresenter.loadLabels(mCurrentClipping.md5);
        updateFavouriteMenu(mCurrentClipping.favourite);
    }

    private void updateFavouriteMenu(int favourite){
        ((DetailFragmentCallback)getActivity()).updateFavourite(favourite);
    }
    @Override
    public void updateNote(Clipping clipping) {
        mDetailBinding.setNote(clipping);
    }

    @Override
    public void showLabels(List<Label> labels) {
        mTagsGroup.showLables(labels);
    }

    @Override
    public void updateFavourite(int favourite) {
        mCurrentClipping.favourite = favourite;
        updateFavouriteMenu(favourite);
    }

    @Override
    public void onMeneClick(int id) {
        if(id == R.id.menu_favourite){
            int favourite = mCurrentClipping.favourite;
            if(favourite == Clipping.K_CLIPPINGS_FAVOURITE){
                mPresenter.updateFavourite(mCurrentClipping,Clipping.K_CLIPPINGS_UN_FAVOURITE);
            }else{
                mPresenter.updateFavourite(mCurrentClipping,Clipping.K_CLIPPINGS_FAVOURITE);
            }
        }
    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
    public static DetailFragment getInstance(Bundle args){
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

}
