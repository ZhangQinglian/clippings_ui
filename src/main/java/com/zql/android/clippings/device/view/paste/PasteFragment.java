package com.zql.android.clippings.device.view.paste;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zql.android.clippings.BR;
import com.zql.android.clippings.R;
import com.zql.android.clippings.databinding.ItemPasteBinding;
import com.zql.android.clippings.device.db.paste.PasteItem;
import com.zql.android.clippings.device.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/8/7.
 */

public class PasteFragment extends BaseFragment implements PasteContract.View {
    private PasteContract.Presenter mPresenter;
    private RecyclerView recyclerView ;
    private PasteAdapter adapter;
    @Override
    public void setPresenter(PasteContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_paste;
    }

    @Override
    protected void initView() {
        recyclerView = getView().findViewById(R.id.clip_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new PasteAdapter();
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        if(mPresenter != null){
            mPresenter.fresh();
        }
    }

    public static PasteFragment getInstance(Bundle args){
        PasteFragment pasteFragment = new PasteFragment();
        if(args != null){
            pasteFragment.setArguments(args);
        }
        return pasteFragment;
    }

    @Override
    public void showNewClipDialog(final CharSequence clipContent) {

        Snackbar.make(getView(),clipContent,Snackbar.LENGTH_INDEFINITE).setAction(R.string.paste_add_clip, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addPaste(clipContent);

            }
        }).show();
    }

    @Override
    public void updateList(List<PasteItem> pasteItems) {
        adapter.update(pasteItems);
    }

    @Override
    public void copySuccess() {
        Snackbar.make(getView(),R.string.paste_copy_success,Snackbar.LENGTH_SHORT).show();
    }


    private class PasteAdapter extends RecyclerView.Adapter<PasteHolder>{

        private List<PasteItem> pasteItems = new ArrayList<>();

        private void update(List<PasteItem> newClips){
            pasteItems.clear();;
            pasteItems.addAll(newClips);
            notifyDataSetChanged();
        }

        private PasteItem getPasteAt(int position){
            return pasteItems.get(position);
        }
        @Override
        public PasteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ItemPasteBinding itemPasteBinding = ItemPasteBinding.inflate(inflater);
            return new PasteHolder(itemPasteBinding);
        }

        @Override
        public void onBindViewHolder(PasteHolder holder, int position) {
            PasteItem pasteItem = pasteItems.get(position);
            holder.bind(pasteItem);
        }

        @Override
        public int getItemCount() {
            return pasteItems.size();
        }
    }

    private class PasteHolder extends RecyclerView.ViewHolder{

        private ItemPasteBinding itemPasteBinding;
        public PasteHolder(ItemPasteBinding itemPasteBinding) {
            super(itemPasteBinding.getRoot());
            this.itemPasteBinding = itemPasteBinding;
        }

        public void bind(final PasteItem pasteItem){
            itemPasteBinding.setVariable(BR.pasteItem, pasteItem);
            itemPasteBinding.executePendingBindings();
            itemPasteBinding.getRoot().findViewById(R.id.paste_content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String paste = adapter.getPasteAt(getAdapterPosition()).clipContent;
                    mPresenter.copy(paste);
                }
            });

            itemPasteBinding.getRoot().findViewById(R.id.paste_content).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final PasteItem paste = adapter.getPasteAt(getAdapterPosition());
                    Snackbar snackbar = Snackbar.make(getView(),getResources().getString(R.string.paste_delete_paste,paste),Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.comm_delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.deletePaste(paste);
                        }
                    }).show();
                    return true;
                }
            });
        }
    }
}
