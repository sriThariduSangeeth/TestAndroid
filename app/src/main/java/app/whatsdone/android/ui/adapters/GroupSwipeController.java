package app.whatsdone.android.ui.adapters;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import app.whatsdone.android.model.ButtonsState;
import app.whatsdone.android.model.Group;
import app.whatsdone.android.services.AuthService;
import app.whatsdone.android.services.AuthServiceImpl;

import static android.support.v7.widget.helper.ItemTouchHelper.*;


public class GroupSwipeController extends Callback {


    private boolean swipeBack = false;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private RectF buttonInstance = null;
    private GroupSwipeControllerActions buttonsActions;
    private RecyclerView recyclerView;
    // private GroupSwipeControllerActions buttonsActions = null;

    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private static final float buttonWidth = getScreenWidth()/5;
    //private Group group;
//    DisplayMetrics displayMetrics = new DisplayMetrics();
//    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//    int height = displayMetrics.heightPixels;
//    int width = displayMetrics.widthPixels;

//    Display display = getWindowManager().getDefaultDisplay();
//    Point size = new Point();
//    display.getSize(size);
//    int width = size.x;
//    int height = size.y;

    public GroupSwipeController(GroupSwipeControllerActions buttonsActions, RecyclerView recyclerView) {
        this.buttonsActions = buttonsActions;
        this.recyclerView = recyclerView;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }



//direction flags
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,  RIGHT);
    }

    //drag
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int swipedPosition = viewHolder.getAdapterPosition();
        GroupsRecyclerViewAdapter adapter = (GroupsRecyclerViewAdapter)recyclerView.getAdapter();
        Group group = adapter.getGroup(swipedPosition);
        //this.group = group;
    }

    //layout direction of the view
    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if(swipeBack)
        {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return  0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

//Called by ItemTouchHelper on RecyclerView's onDraw callback.
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                 if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
//                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE)
//                {
//                    dX = Math.min(dX, -buttonWidth);
//
//                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // buttonShowedState = ButtonsState.GONE;
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
        drawButtons(c, viewHolder);

    }


    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {

        float corners = 0;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();
        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidth, itemView.getBottom());

//        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidth, itemView.getBottom());
//        p.setColor(Color.RED);
//        c.drawRoundRect(leftButton, corners, corners, p);

        Group group = ((GroupsRecyclerViewAdapter.RecyclerViewHolder)viewHolder).getGroup();
        System.out.println(group.getCreatedBy());
        System.out.println(AuthServiceImpl.getCurrentUser().getDocumentID());
        if(group.getCreatedBy().equals(AuthServiceImpl.getCurrentUser().getDocumentID())){
          //  RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidth, itemView.getBottom());
            p.setColor(Color.RED);
            c.drawRoundRect(leftButton, corners, corners, p);
            drawText("DELETE", c, leftButton, p);
        }else {
             p.setColor(Color.GRAY);
            c.drawRoundRect(leftButton, corners, corners, p);
            drawText("LEAVE", c, leftButton, p);

        }




       buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;

        }
        else
            buttonShowedState = ButtonsState.GONE;

        //c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);

    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 30;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }

    public void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;


                if (swipeBack) {
                    if (dX > buttonWidth )
                    {
                        buttonShowedState  = ButtonsState.LEFT_VISIBLE;
                    }

                    else if (dX < -buttonWidth)
                    {
                        buttonShowedState = ButtonsState.GONE;
                    }


                    if (buttonShowedState != ButtonsState.GONE)
                    {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }



                }


                return false;
            }


        });


    }

//simulate click on RecyclerView
    @SuppressLint("ClickableViewAccessibility")
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }

                return false;
            }
        });
      }


    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    GroupSwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);


                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                            buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                             }

                        else
                            buttonShowedState = ButtonsState.GONE;

                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;

                }

                return false;
            }
        });

    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {


        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);


        }

    }


    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,

    }


}





