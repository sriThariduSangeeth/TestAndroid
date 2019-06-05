package app.whatsdone.android.services;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import app.whatsdone.android.model.BaseEntity;

public interface ServiceListener {
    default void onDataReceived(List<BaseEntity> entities) {

    }
    default void onCompleted(@Nullable Map<String, Object> data) {

    }
    default void onSuccess() {

    }
    default void onError(@Nullable String error){

    }
}
