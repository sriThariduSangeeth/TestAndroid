package app.whatsdone.android.services;

import app.whatsdone.android.model.BaseEntity;

public interface ServiceBase {
   void create(BaseEntity entity, ServiceListener listener);
   void update(BaseEntity entity, ServiceListener listener);
   void delete(String documentID, ServiceListener listener);
}
