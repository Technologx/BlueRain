package com.technologx.bluerain.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.technologx.bluerain.R;
import com.technologx.bluerain.utilities.ThemeUtils;

public class BlueRainCardView extends CardView {

    private Context context;

    public BlueRainCardView(Context context) {
        super(context);
        setupRightCardColor(context);
    }

    public BlueRainCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupRightCardColor(context);
    }

    public BlueRainCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupRightCardColor(context);
    }

    @Override
    public void setCardBackgroundColor(int ignoredColor) {
        super.setCardBackgroundColor(ThemeUtils.darkLightOrTransparent(context, R.color
                .card_dark_background, R.color.card_light_background, R.color
                .card_clear_background));
    }

    private void setupRightCardColor(Context context) {
        this.context = context;
        setCardBackgroundColor(0);
    }

}