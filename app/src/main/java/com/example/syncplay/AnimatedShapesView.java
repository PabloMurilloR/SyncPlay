package com.example.syncplay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AnimatedShapesView extends View {

    private Paint paint;
    private RectF rect;
    private float x, y;
    private float speedX = 5;
    private float speedY = 5;
    private boolean isAnimating = false;

    public AnimatedShapesView(Context context) {
        super(context);
        init();
    }

    public AnimatedShapesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedShapesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF00FF00); // Color verde
        rect = new RectF(0, 0, 100, 100); // Tamaño del rectángulo
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dibuja el rectángulo en las coordenadas x, y
        canvas.drawRect(x, y, x + rect.width(), y + rect.height(), paint);

        // Mueve el rectángulo si se está animando
        if (isAnimating) {
            x += speedX;
            y += speedY;

            // Cambia de dirección si llega a los bordes de la vista
            if (x <= 0 || x >= getWidth() - rect.width()) {
                speedX *= -1;
            }
            if (y <= 0 || y >= getHeight() - rect.height()) {
                speedY *= -1;
            }

            // Invalida la vista para forzar un redibujado
            invalidate();
        }
    }


    public void startAnimation() {
        isAnimating = true;
        invalidate();
    }

    public void stopAnimation() {
        isAnimating = false;
    }
}
