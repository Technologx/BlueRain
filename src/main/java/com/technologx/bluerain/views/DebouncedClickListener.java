package com.technologx.bluerain.views;

import android.view.View;

public abstract class DebouncedClickListener implements View.OnClickListener {

    private boolean clickable = true;

    @Override
    public final void onClick(View v) {
        if (clickable) {
            clickable = false;
            onDebouncedClick(v);
            reset();
        }
    }

    /**
     * Override this function to handle clicks. reset() must be called after each click for this
     * function to be called again.
     */
    public abstract void onDebouncedClick(View v);

    /**
     * Allows another click.
     */
    private void reset() {
        clickable = true;
    }

}