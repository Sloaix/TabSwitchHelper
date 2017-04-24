package com.bangbangarmy.util;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v4.util.SparseArrayCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;

public class TabSwitchHelper {
    private SparseArrayCompat<CompoundButton> mButtons;
    private boolean mProtectFromCheckedChange = false;
    private int mCheckedId = -1;
    private OnTabStateChangedListener mListener;

    public TabSwitchHelper() {
        mButtons = new SparseArrayCompat<>();
    }

    public void init(Activity view, @IdRes int... ids) {
        if (view == null) {
            throw new NullPointerException("the view is null");
        }
        for (int id : ids) {
            if (view.findViewById(id) == null) {
                throw new IllegalArgumentException("can not find view with id " + id);
            }
            if (!(view.findViewById(id) instanceof CompoundButton)) {
                throw new IllegalArgumentException("the id is not of a compoundButton");
            }
            CompoundButton compoundButton = (CompoundButton) view.findViewById(id);
            add(id, compoundButton);
        }
    }


    public void init(View view, @IdRes int... ids) {
        if (view == null) {
            throw new NullPointerException("the view is null");
        }
        for (int id : ids) {
            if (view.findViewById(id) == null) {
                throw new IllegalArgumentException("can not find view with id " + id);
            }
            if (!(view.findViewById(id) instanceof CompoundButton)) {
                throw new IllegalArgumentException("the id is not of a compoundButton");
            }
            CompoundButton compoundButton = (CompoundButton) view.findViewById(id);
            add(id, compoundButton);
        }
    }

    private void add(@IdRes int id, final CompoundButton button) {
        mButtons.put(id, button);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CompoundButton compoundButton = (CompoundButton) v;
                return event.getAction() == MotionEvent.ACTION_DOWN && mListener != null && mListener.beforeTabStateChanged(compoundButton, compoundButton.isChecked());
            }
        });
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 避免无限循环
                if (mProtectFromCheckedChange) {
                    return;
                }

                mProtectFromCheckedChange = true;

                if (mCheckedId != -1) {
                    mButtons.get(mCheckedId).setChecked(false);
                }
                mProtectFromCheckedChange = false;

                int id = buttonView.getId();
                setCheckedId(id);
                if (mListener != null) {
                    mListener.afterTabStateChanged(buttonView, isChecked);
                }
            }
        });
    }

    public void checked(@IdRes int id) {
        if (mButtons.get(id) == null) {
            return;
        }
        if (mCheckedId != -1) {
            mButtons.get(mCheckedId).setChecked(false);
        }
        mButtons.get(id).setChecked(true);
        setCheckedId(id);
    }

    private void setCheckedId(@IdRes int checkedId) {
        mCheckedId = checkedId;
    }

    public void setListener(OnTabStateChangedListener listener) {
        mListener = listener;
    }

    public interface OnTabStateChangedListener {
        void afterTabStateChanged(CompoundButton button, boolean isChecked);

        boolean beforeTabStateChanged(CompoundButton button, boolean isChecked);
    }
}
