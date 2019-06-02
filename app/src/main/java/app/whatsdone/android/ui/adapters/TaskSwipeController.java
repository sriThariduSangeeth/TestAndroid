package app.whatsdone.android.ui.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import app.whatsdone.android.ui.fragments.TaskSwipeControllerAction;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class TaskSwipeController extends ItemTouchHelper.Callback{
    private boolean swipeBack = false;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private RectF buttonInstance = null;
    private TaskSwipeControllerAction buttonsActions;
   // private TaskSwipeControllerAction buttonsActions = null;
    private TaskSwipeController.ButtonsState buttonShowedState = TaskSwipeController.ButtonsState.GONE;
    private static final float buttonWidth = 300;

    public TaskSwipeController(TaskSwipeControllerAction buttonsActions) {
        this.buttonsActions = buttonsActions;
    }


    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if(swipeBack)
        {
            swipeBack = buttonShowedState != TaskSwipeController.ButtonsState.GONE;
            return  0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != TaskSwipeController.ButtonsState.GONE) {
                // if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == TaskSwipeController.ButtonsState.RIGHT_VISIBLE)
                {
                    dX = Math.min(dX, -buttonWidth);

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // buttonShowedState = ButtonsState.GONE;
            }
        }

        if (buttonShowedState == TaskSwipeController.ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
        drawButtons(c, viewHolder);

    }


    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        // RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        // p.setColor(Color.WHITE);
        // c.drawRoundRect(leftButton, corners, corners, p);
        // drawText("EDIT", c, leftButton, p);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.WHITE);
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText("DELETE", c, rightButton, p);

        buttonInstance = null;
        if (buttonShowedState == TaskSwipeController.ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;

        }
        else
            buttonShowedState = TaskSwipeController.ButtonsState.GONE;
        //else if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
        //    buttonInstance = leftButton;
        // }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.BLACK);
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



    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;


                if (swipeBack) {
                    if (dX < -buttonWidth)
                    {
                        buttonShowedState = TaskSwipeController.ButtonsState.RIGHT_VISIBLE;
                    }
                    else if (dX > buttonWidth)
                    {
                        buttonShowedState = TaskSwipeController.ButtonsState.GONE;
                    }

                    //else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != TaskSwipeController.ButtonsState.GONE)
                    {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }


                }


                return false;
            }


        });


    }


    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    // buttonShowedState = ButtonsState.GONE;
                }
                //v.performClick();
                return false;
            }
        });
        //  recyclerView.performClick();
    }


    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    TaskSwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            ///  v.performClick();
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    buttonShowedState = TaskSwipeController.ButtonsState.GONE;
                    // v.performClick();
                }
                // v.performClick();
                return false;
            }
        });
        //  recyclerView.performClick();
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

}
