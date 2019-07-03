package app.whatsdone.android.model;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

import app.whatsdone.android.utils.TextUtil;

public class Message implements IMessage, MessageContentType.Image,MessageContentType,BaseEntity {

    private String id;
    private String text;
    private Date createdAt;
    private User user;
    private Image image;
    private MessageFormatter messageFormatter;

    public  Message(){
    }

    public Message(String id, User user, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getText() {
        if(TextUtil.isNullOrEmpty(text)) return text;
        String textMessage = text;

        if(messageFormatter != null)
            textMessage = messageFormatter.formatMessage(textMessage);

        return textMessage;
    }


    public String getPlainText() {
        return text;
    }
    @Override
    public IUser getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Nullable
    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    @Override
    public String getDocumentID() {
        return null;
    }

    @Override
    public void setDocumentID(String documentID) {

    }

    public void setMessageFormatter(MessageFormatter messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
