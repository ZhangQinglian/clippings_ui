package com.zql.android.clippings.usecase;

import com.zql.android.clippings.bridge.mvpc.UseCase;
import com.zql.android.clippings.device.ClippingsApplication;
import com.zql.android.clippings.device.db.Clipping;

/**
 * Created by scott on 2017/8/8.
 */

public class UpdateStatus extends UseCase <UpdateStatus.RequstValues,UpdateStatus.ResponseValues>{

    @Override
    protected void executeUseCase(RequstValues requestValues) {
        Clipping clipping = requestValues.clipping;
        int status = requestValues.status;
        clipping.status = status;
        int c = ClippingsApplication.own().getClippingsDB().clippingDao().updateClipping(clipping);
        if( c == 1){
            getUseCaseCallback().onSuccess(new UpdateStatus.ResponseValues(status));
        }
    }

    public static final class RequstValues implements UseCase.RequestValues{
        public Clipping clipping;
        public int status;
    }

    public static final class ResponseValues implements UseCase.ResponseValue{
        public ResponseValues(int status){
            this.status = status;
        }
        public int status;
    }
}
