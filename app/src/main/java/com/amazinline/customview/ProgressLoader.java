package com.amazinline.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.amazinline.R;

public class ProgressLoader extends FrameLayout {

    private RelativeLayout rootView;

    public ProgressLoader(Context context) {
        super(context);
        init();
    }

    public ProgressLoader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressLoader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressLoader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater != null) {

            layoutInflater.inflate(R.layout.view_progress_loader, this, true);

            rootView = findViewById(R.id.rootLayout);

            TextView momentTextView = findViewById(R.id.moment_tv);

            AppCompatImageView coopLoader = findViewById(R.id.coopLoader);
            ProgressBar nativeLoader = findViewById(R.id.nativeLoader);

            nativeLoader.setVisibility(GONE);
            coopLoader.setVisibility(VISIBLE);

            Drawable drawable = coopLoader.getDrawable();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            } else {
                nativeLoader.setVisibility(VISIBLE);
                coopLoader.setVisibility(GONE);
            }
        }
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
        if (getHandler() != null) getHandler().removeCallbacks(runnable);
    }

    public void showAfterDelay() {
        hide();

        if (getHandler() != null) getHandler().postDelayed(runnable, 1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            show();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (getHandler() != null) getHandler().removeCallbacks(runnable);
        super.onDetachedFromWindow();
    }

    /**
     * set background on some pages while loading
     *
     * @param color
     */
    public void setProgressBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
    }
}

