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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.os.Trace;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zql.android.clippings.ClippingsApplication;
import com.zql.android.clippings.R;
import com.zql.android.clippings.databinding.FragmentDetailBinding;
import com.zql.android.clippings.sdk.parser.Clipping;
import com.zql.android.clippings.sdk.provider.Label;
import com.zql.android.clippings.sdk.provider.LabelContract;
import com.zql.android.clippings.view.BaseFragment;
import com.zql.android.clippings.view.taggroup.TagsGroup;
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

    public interface DetailFragmentCallback{
        void clippingUpdate(Clipping clipping);
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
        view.findViewById(R.id.content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) ClippingsApplication.own().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("label",mCurrentClipping.content));
                Toast.makeText(ClippingsApplication.own(),"已复制",Toast.LENGTH_LONG).show();
                return true;
            }
        });
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
        mTagsGroup = (TagsGroup) getView().findViewById(R.id.tags_group);
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
    public void setPresenter(DetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
    public static DetailFragment getInstance(Bundle args){
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menus,menu);
    }


    boolean f = true;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_favourite:
                f = !f;
                if(f){
                    item.setIcon(getResources().getDrawable(R.drawable.menu_favourite_0));
                }else {
                    item.setIcon(getResources().getDrawable(R.drawable.menu_favourite_1));
                }
                break;
        }
        return true;
    }
}
