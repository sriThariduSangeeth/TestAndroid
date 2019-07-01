package app.whatsdone.android.services;

import app.whatsdone.android.model.Group;

public interface LogService extends ServiceBase {

    void getByTaskId(String id, Group group, ServiceListener serviceListener);
}
