package com.munkiphd.compass;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class CompassView extends View {
     public CompassView(Context context) {
        super(context);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCompassView();
    }


    private Paint markerPaint;
    private Paint textPaint;
    private Paint circlePaint;
    private String northString;
    private String southString;
    private String westString;
    private String eastString;
    private int textHeight;

    protected void initCompassView(){
        setFocusable(true);

        Resources r = this.getResources();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(r.getColor(R.color.background_color));
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        northString= r.getString(R.string.cardinal_north);
        eastString = r.getString(R.string.cardinal_east);
        westString = r.getString(R.string.cardinal_west);
        southString = r.getString(R.string.cardinal_south);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(r.getColor(R.color.text_color));

        textHeight = (int) textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(r.getColor(R.color.marker_color));
    }

    @Override
    protected void onDraw(Canvas canvas){
        int px = getMeasuredWidth() / 2;
        int py = getMeasuredHeight() / 2;

        int radius = Math.min(px, py);

        canvas.drawCircle(px, py, radius, circlePaint);

        canvas.save();
        canvas.rotate(-bearing, px, py);

        int textWidth = (int)textPaint.measureText("W");
        int cardinalX = px - textWidth / 2;
        int cardinalY = py - radius + textHeight;

        for(int i = 0; i < 24; i++){
            canvas.drawLine(px, py-radius, px, py-radius+10, markerPaint);

            canvas.save();
            canvas.translate(0, textHeight);

            if(i % 6 == 0){
                String dirString = "";
                switch(i){
                    case(0):{
                        dirString = northString;
                        int arrowY = 2*textHeight;
                        canvas.drawLine(px, arrowY, px-5, 3*textHeight, markerPaint);
                        canvas.drawLine(px, arrowY, px+5, 3*textHeight, markerPaint);
                        break;
                    }
                    case(6) : dirString = eastString; break;
                    case(12) : dirString = southString; break;
                    case(18) : dirString = westString; break;
                }

                canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
            }

            else if(i % 3 == 0){
                String angle = String.valueOf(i*15);
                float angleTextWidth = textPaint.measureText(angle);

                int angleTextX = (int) (px-angleTextWidth/2);
                int angleTextY = py-radius + textHeight;
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }

            canvas.restore();
            canvas.rotate(15, px, py);
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

       int d = Math.min(measuredWidth, measuredHeight);

        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec){
        int result = 0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }

        return result;
    }

    private float bearing;

    public void setBearing(float _bearing){
        bearing = _bearing;
    }

    public float getBearing(){
        return bearing;
    }
}
