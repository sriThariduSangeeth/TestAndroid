package app.whatsdone.android.ui.adapters;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;


import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

public class TaskSwipeController extends ItemTouchHelper.Callback{
    private boolean swipeBack = false;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private RectF buttonInstance = null;
    private TaskSwipeControllerAction buttonsActions;
    private TaskSwipeController.ButtonsState buttonShowedState = TaskSwipeController.ButtonsState.GONE;
    private static final float buttonWidth = getScreenWidth()/5;
    private static final float rightSwipeWidth = buttonWidth*3;
    private  RectF leftButton, rightInProgressButton, rightOnHoldButton, rightDoneButton;


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


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
                if (buttonShowedState == TaskSwipeController.ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == TaskSwipeController.ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -rightSwipeWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        }

        if (buttonShowedState == TaskSwipeController.ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
        drawButtons(c, viewHolder);

    }


    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
       // float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 0;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        //delete
        leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidth, itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText("DELETE", c, leftButton, p);

        //in progress
        rightInProgressButton = new RectF(itemView.getRight() - rightSwipeWidth, itemView.getTop(), itemView.getRight()-2*buttonWidth, itemView.getBottom());
        p.setColor(Color.YELLOW);
        c.drawRoundRect(rightInProgressButton, corners, corners, p);
        drawText("In Progress", c, rightInProgressButton, p);


        //onHold
        rightOnHoldButton = new RectF(itemView.getRight()-buttonWidth , itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.GRAY);
        c.drawRoundRect(rightOnHoldButton, corners, corners, p);
        drawText("On Hold", c, rightOnHoldButton, p);


        //done
        rightDoneButton = new RectF(itemView.getRight()-2*buttonWidth, itemView.getTop(), itemView.getRight()-buttonWidth, itemView.getBottom());
        p.setColor(Color.BLUE);
        c.drawRoundRect(rightDoneButton, corners, corners, p);
        drawText("Done", c, rightDoneButton, p);


        buttonInstance = null;
        if (buttonShowedState == TaskSwipeController.ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightDoneButton;
            buttonInstance=rightOnHoldButton;
            buttonInstance=rightInProgressButton;

        }

        else if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        }
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


//swipe back
    @SuppressLint("ClickableViewAccessibility")
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

                    else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

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


    @SuppressLint("ClickableViewAccessibility")
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


    @SuppressLint("ClickableViewAccessibility")
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {

                    TaskSwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);

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
                            buttonsActions.onTaskDeleteClicked(viewHolder.getAdapterPosition());

                        }
                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                            if (rightDoneButton.contains(event.getX(), event.getY())) buttonsActions.onTaskDoneClicked(viewHolder.getAdapterPosition());

                            if(rightOnHoldButton.contains(event.getX(), event.getY())) buttonsActions.onTaskOnHoldClicked(viewHolder.getAdapterPosition());

//
                            if(rightInProgressButton.contains(event.getX(), event.getY()))  buttonsActions.onTaskInProgressClicked(viewHolder.getAdapterPosition());

                              //if(buttonInstance.contains(rightInProgressButton))  buttonsActions.onTaskInProgressClicked(viewHolder.getAdapterPosition());
//
//                            if(buttonInstance.contains(rightOnHoldButton)) buttonsActions.onTaskOnHoldClicked(viewHolder.getAdapterPosition());

                            //System.out.println("touch up");
                            // buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                        }

                    }

                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                    ;
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

}
