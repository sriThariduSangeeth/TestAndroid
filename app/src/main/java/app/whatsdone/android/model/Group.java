package app.whatsdone.android.model;

public class Group implements BaseEntity {

   // public static final int text_type = 0;

    private String documentID = "";
  //  private int id;
    private String groupName;
  //  private int timeStamp;
   // private String description;

  /*  public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
*/



    public String getGroupName() {
      return groupName;
  }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

  @Override
  public String getDocumentID() {
    return this.documentID;
  }

  @Override
  public void setDocumentID(String documentID) {
    this.documentID = documentID;
  }
}
