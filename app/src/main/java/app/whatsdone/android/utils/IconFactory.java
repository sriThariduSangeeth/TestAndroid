package app.whatsdone.android.utils;

import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import app.whatsdone.android.model.Change;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.model.Task;

public class IconFactory {

    private Map<String, TextDrawable> memory = new HashMap<>();

    private IconFactory() {}

    private static class LazyHolder {
        static final IconFactory INSTANCE = new IconFactory();
    }
    public static IconFactory getInstance() {
        return IconFactory.LazyHolder.INSTANCE;
    }

    public TextDrawable get(ImageView imageView, Group group) {
        if(memory.containsKey(group.getDocumentID()))
            return memory.get(group.getDocumentID());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int colorGen = generator.getColor(group.getGroupName());
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .width(imageView.getLayoutParams().width)
                .height(imageView.getLayoutParams().height)
                .endConfig()
                .rect();
        TextDrawable icon =  builder.build(group.getGroupName().substring(0,1), colorGen);
        memory.put(group.getDocumentID(), icon);
        return icon;
    }

    public TextDrawable get(ImageView imageView, Task task) {
        if(memory.containsKey(task.getAssignedUser()))
            return memory.get(task.getAssignedUser());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int colorGen = generator.getColor(task.getAssignedUser());
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .width(imageView.getLayoutParams().width)
                .height(imageView.getLayoutParams().height)
                .endConfig()
                .rect();
        String token = task.getAssignedUserName();
        if(TextUtil.isNullOrEmpty(token)) token = task.getAssignedUser();
        TextDrawable icon =  builder.build(token.substring(0,1), colorGen);
        memory.put(task.getAssignedUser(), icon);
        return icon;
    }


    public TextDrawable get(ImageView imageView, Change change) {
        if(memory.containsKey(change.getType().toString()))
            return memory.get(change.getType().toString());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int colorGen = generator.getColor(change.getType().toString());
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .width(imageView.getLayoutParams().width)
                .height(imageView.getLayoutParams().height)
                .endConfig()
                .rect();
        TextDrawable icon =  builder.build(change.getType().toString().substring(0,1), colorGen);
        memory.put(change.getType().toString(), icon);
        return icon;
    }
}
