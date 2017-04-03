package com.appersiano.goustoexercise.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LabelTextView {

    private TextView textView;

    private LabelTextView(final LabelTextViewBuilder builder, final Context context) {
        textView = new TextView(context);

        GradientDrawable gd = getDefaultGradientDrawable(builder);
        textView.setBackground(gd);

        LinearLayout.LayoutParams lpp =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        lpp.setMargins(12, 0, 12, 0);
        textView.setLayoutParams(lpp);
        textView.setPadding(20, 10, 20, 10);

        textView.setTextColor(context.getResources().getColor(builder.textColor));
        textView.setText(builder.text);

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                GradientDrawable gd = null;
                if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_MOVE){
                    gd = new GradientDrawable();
                    gd.setColor(builder.pressedColor);
                    gd.setCornerRadius(builder.cornerRadius);
                    textView.setTextColor(context.getResources().getColor(builder.pressedTextColor));
                }else if (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL){
                    gd = getDefaultGradientDrawable(builder);
                    textView.setTextColor(context.getResources().getColor(builder.textColor));
                }

                textView.setBackground(gd);
                return false;
            }
        });

        textView.setOnClickListener(builder.clickListener);
    }

    @NonNull
    private GradientDrawable getDefaultGradientDrawable(LabelTextViewBuilder builder) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(builder.backgroundColor);
        gd.setCornerRadius(builder.cornerRadius);
        return gd;
    }

    public TextView getTextView() {
        return textView;
    }

    public static class LabelTextViewBuilder {

        private Context context;
        private String text = "Message";
        private int textColor = android.R.color.white;
        private int backgroundColor = Color.BLACK;
        private int cornerRadius = 20;
        private int pressedColor = Color.YELLOW;
        private int pressedTextColor = android.R.color.black;
        private View.OnClickListener clickListener;

        public LabelTextViewBuilder(Context context) {
            this.context = context;
        }

        public LabelTextViewBuilder setText(String text) {
            this.text = text;
            return this;
        }

        public LabelTextViewBuilder setTextColor(@ColorRes int color) {
            this.textColor = color;
            return this;
        }

        public LabelTextViewBuilder setDefaultBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public LabelTextViewBuilder setCornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }

        public LabelTextViewBuilder setPressedBackgroundColor(@ColorInt int argb) {
            this.pressedColor = argb;
            return this;
        }

        public LabelTextViewBuilder setTextPressedColor(@ColorRes int color) {
            this.pressedTextColor = color;
            return this;
        }

        public LabelTextViewBuilder setOnClickListener(View.OnClickListener listener) {
            this.clickListener = listener;
            return this;
        }

        public TextView build() {
            LabelTextView labelTextView = new LabelTextView(this, context);
            return labelTextView.getTextView();
        }

    }
}
