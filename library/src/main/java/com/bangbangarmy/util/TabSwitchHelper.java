package com.bangbangarmy.util;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

    @Deprecated
    public void init(Activity activity, @IdRes int... ids) {
        View view = activity.findViewById(android.R.id.content);
        init(view, ids);
    }

    @Deprecated
    public void init(Fragment fragment, @IdRes int... ids) {
        if (fragment.getView() == null) {
            throw new IllegalArgumentException("fragment.getView() is null");
        }
        View view = fragment.getView().findViewById(android.R.id.content);
        init(view, ids);
    }

    @Deprecated
    public void init(DialogFragment fragment, @IdRes int... ids) {
        if (fragment.getView() == null) {
            throw new IllegalArgumentException("fragment.getView() is null");
        }
        View view = fragment.getView().findViewById(android.R.id.content);
        init(view, ids);
    }

    @Deprecated
    private void init(View view, @IdRes int... ids) {
        if (view == null) {
            throw new NullPointerException("the view is null");
        }
        for (int id : ids) {
            if (view.findViewById(id) == null) {
                throw new IllegalArgumentException("can not find view with id " + id);
            }
            if (!(view.findViewById(id) instanceof CompoundButton)) {
                throw new IllegalArgumentException("the view is not of a compoundButton");
            }
            CompoundButton compoundButton = (CompoundButton) view.findViewById(id);
            add(compoundButton);
        }
    }

    public void add(Activity activity, @IdRes int... ids) {
        View view = activity.findViewById(android.R.id.content);
        add(view, ids);
    }

    public void add(Fragment fragment, @IdRes int... ids) {
        if (fragment.getView() == null) {
            throw new IllegalArgumentException("fragment.getView() is null");
        }
        View view = fragment.getView().findViewById(android.R.id.content);
        add(view, ids);
    }

    public void add(DialogFragment fragment, @IdRes int... ids) {
        if (fragment.getView() == null) {
            throw new IllegalArgumentException("fragment.getView() is null");
        }
        View view = fragment.getView().findViewById(android.R.id.content);
        add(view, ids);
    }

    private void add(View view, @IdRes int... ids) {
        if (view == null) {
            throw new NullPointerException("the view is null");
        }
        for (int id : ids) {
            if (view.findViewById(id) == null) {
                throw new IllegalArgumentException("can not find view with id " + id);
            }
            if (!(view.findViewById(id) instanceof CompoundButton)) {
                throw new IllegalArgumentException("the view is not of a compoundButton");
            }
            CompoundButton compoundButton = (CompoundButton) view.findViewById(id);
            add(compoundButton);
        }
    }

    private boolean isAdded(CompoundButton button) {
        return button != null && mButtons.get(button.getId()) != null;
    }

    private boolean isAdded(int id) {
        return mButtons.get(id) != null;
    }


    public void add(final CompoundButton button) {
        if (isAdded(button)) {
            return;
        }
        final int id = button.getId();

        mButtons.put(id, button);

        //提供打断check事件的功能
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

    public void remove(@IdRes int id) {
        if (!isAdded(id)) {
            return;
        }
        CompoundButton button = mButtons.get(id);
        button.setOnCheckedChangeListener(null);
        button.setOnTouchListener(null);
        mButtons.remove(id);
    }

    public void clear() {
        for (int i = 0; i < mButtons.size(); i++) {
            int key = mButtons.keyAt(i);
            remove(key);
        }
    }

    public void checked(@IdRes int id) {
        if (!isAdded(id)) {
            return;
        }
        if (mCheckedId != -1) {
            mButtons.get(mCheckedId).setChecked(false);
        }
        mButtons.get(id).setChecked(true);
        setCheckedId(id);
    }

    public void checkedFirst() {
        int key = mButtons.keyAt(0);
        checked(key);
    }

    public void checked(CompoundButton compoundButton) {
        checked(compoundButton.getId());
    }

    public CompoundButton getChecked() {
        for (int i = 0; i < mButtons.size(); i++) {
            int key = mButtons.keyAt(i);
            CompoundButton button = mButtons.get(key);
            if (button.isChecked()) {
                return button;
            }
        }
        return null;
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

    public class SimpleStateChangedListener implements OnTabStateChangedListener {
        @Override
        public void afterTabStateChanged(CompoundButton button, boolean isChecked) {

        }

        @Override
        public boolean beforeTabStateChanged(CompoundButton button, boolean isChecked) {
            return false;
        }
    }
}
