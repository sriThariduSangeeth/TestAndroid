package app.whatsdone.android.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import app.whatsdone.android.model.BaseEntity;
import app.whatsdone.android.model.Message;
import timber.log.Timber;

public interface ServiceListener {

    default  void onDataReceivedForMessage(ArrayList<Message> messages){

    }
    default void onDataReceived(List<BaseEntity> entities) {

    }
    default void onCompleted(boolean isSuccessful) {

    }
    default void onSuccess() {

    }
    default void onError(@Nullable String error){
        Timber.e(error);
    }

    default void onDataReceived(BaseEntity entity){

    }
}
