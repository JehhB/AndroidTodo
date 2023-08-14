package com.example.todo.ui.helper;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

@SuppressWarnings("unused")
public class ItemSwipeHelper extends RecyclerView.ItemDecoration implements OnItemTouchListener {
    private static final int SWIPE_START_THRESHOLD = 4;

    public static final int SWIPE_RIGHT = 1;
    public static final int SWIPE_LEFT = 2;

    private VelocityTracker tracker;
    private ViewHolder selected;
    private float initX;
    private float initY;
    private float currentDx;

    public void attachToRecyclerView(RecyclerView rv) {
        rv.addOnItemTouchListener(this);
        rv.addItemDecoration(this);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (tracker != null) {
                tracker.clear();
            } else {
                tracker = VelocityTracker.obtain();
            }
            initX = e.getX();
            initY = e.getY();
            currentDx = 0;
            return false;
        }

        if (tracker != null) {
            tracker.addMovement(e);
        }

        selectIfSwipe(rv, e);

        return selected != null;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if (selected == null) return;

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                currentDx = getDx(e);
                rv.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                revert(rv, selected);
                tracker.recycle();
                tracker = null;
                selected = null;
                break;
            case MotionEvent.ACTION_UP:
                if (isSwipe(e)) {
                    finishSwipe(rv, selected);
                    onSwipe(rv, selected, getDirection(e));
                } else {
                    revert(rv, selected);
                }
                tracker.recycle();
                tracker = null;
                selected = null;
                break;
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (!disallowIntercept) return;
        selected = null;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (selected != null) {
            onDrawChild(parent, selected, currentDx, true);
        }
    }

    protected void onDrawChild(RecyclerView rv, ViewHolder vh, float dX, boolean isForward) {
        vh.itemView.setTranslationX(dX);
    }

    protected void onSwipe(RecyclerView rv, ViewHolder vh, int direction) {
        if(rv.getAdapter() != null) {
            rv.getAdapter().notifyItemChanged(vh.getAdapterPosition());
        }
    }

    protected float getThreshold() {
        return 0.5f;
    }

    protected float getVelocityThreshold() {
        return 1000f;
    }

    protected float getWidth(ViewHolder vh) {
        return vh.itemView.getWidth();
    }

    protected long getAnimationDuration() {
        return 50L;
    }

    private float getDx(MotionEvent e) {
        return e.getX() - initX;
    }

    private float getDy(MotionEvent e) {
        return e.getY() - initY;
    }

    private int getDirection(MotionEvent e) {
        return getDx(e) >= 0 ? SWIPE_RIGHT : SWIPE_LEFT;
    }

    private boolean isSwipe(MotionEvent e) {
        return Math.abs(getDx(e)) > Math.abs(getDy(e)) &&
                (Math.abs(getDx(e)) > getWidth(selected) * getThreshold() ||
                tracker.getXVelocity() > getVelocityThreshold());
    }

    private void selectIfSwipe(RecyclerView rv, MotionEvent e) {
        if (selected != null) return;
        if (e.getAction() != MotionEvent.ACTION_MOVE) return;
        if (rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) return;
        if (Math.abs(getDy(e)) >= Math.abs(getDx(e))) return;
        if (Math.abs(getDx(e)) < SWIPE_START_THRESHOLD) return;

        selected = findViewHolder(rv, e);
    }

    private ViewHolder findViewHolder(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child == null) return null;

        return rv.getChildViewHolder(child);
    }

    private void revert(RecyclerView rv, ViewHolder vh) {
        ValueAnimator animator = ValueAnimator.ofFloat(currentDx, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(getAnimationDuration());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            onDrawChild(rv, vh, value, false);
        });
        animator.start();
    }

    private void finishSwipe(RecyclerView rv, ViewHolder vh) {
        if (Math.abs(currentDx) > getWidth(vh)) return;

        ValueAnimator animator = ValueAnimator.ofFloat(currentDx, signNum(currentDx) * getWidth(vh));
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(getAnimationDuration());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            onDrawChild(rv, vh, value, false);
        });
        animator.start();
    }

    private float signNum(float x) {
        return x >= 0 ? 1 : 0;
    }
}
