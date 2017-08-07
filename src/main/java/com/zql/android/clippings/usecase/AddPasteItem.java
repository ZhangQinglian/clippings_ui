package com.zql.android.clippings.usecase;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.paste.PasteItem;

/**
 * Created by scott on 2017/8/7.
 */

public class AddPasteItem extends UseCase<AddPasteItem.RequestValues,AddPasteItem.ResponseValue> {
    @Override
    protected void executeUseCase(AddPasteItem.RequestValues requestValues) {
        String content = requestValues.content;
        if(content != null && content.trim().length()>0){
            PasteItem item = new PasteItem();
            item.clipContent = content;
            long n = ClippingsApplication.own().getClippingsDB().clippingDao().addPasteItem(item);
            if(n > 0){
                getUseCaseCallback().onSuccess(new ResponseValue());
            }
        }
    }

    public final static class RequestValues implements UseCase.RequestValues{
        public String content;
    }

    public final class ResponseValue implements UseCase.ResponseValue{

    }
}
