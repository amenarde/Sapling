package cis350.upenn.edu.sapling;

/**
 * Created by amlcurran
 * Adapted by makvan 4/7/18.
 */


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import com.github.amlcurran.showcaseview.ShowcaseDrawer;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

class SquareShowcaseView implements ShowcaseDrawer {

            private final float width;
            private final float height;
            private final Paint eraserPaint;
            private final Paint basicPaint;
            private final int eraseColour;
            private final RectF renderRect;

            public SquareShowcaseView(Resources resources, int settings) {
                if (settings == 1) {
                    this.width = resources.getDimension(R.dimen.custom_showcase_width);
                    this.height = resources.getDimension(R.dimen.custom_showcase_height);

                } else {
                    this.width = resources.getDimension(R.dimen.custom_showcase_width2);
                    this.height = resources.getDimension(R.dimen.custom_showcase_height2);
                }
                PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
                eraserPaint = new Paint();
                eraserPaint.setColor(0xFFFFFF);
                eraserPaint.setAlpha(0);
                eraserPaint.setXfermode(xfermode);
                eraserPaint.setAntiAlias(true);
                eraseColour = resources.getColor(R.color.colorPrimaryDark);
                basicPaint = new Paint();
                renderRect = new RectF();
            }

            @Override
            public void setShowcaseColour(int color) {
                eraserPaint.setColor(color);
            }

            @Override
            public void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier) {
                Canvas bufferCanvas = new Canvas(buffer);
                renderRect.left = x - width / 2f;
                renderRect.right = x + width / 2f;
                renderRect.top = y - height / 2f;
                renderRect.bottom = y + height / 2f;
                eraserPaint.setAlpha(0);
                eraserPaint.setColor(0xFFFFFF);
                bufferCanvas.drawRect(renderRect, eraserPaint);
            }

            @Override
            public int getShowcaseWidth() {
                return (int) width;
            }

            @Override
            public int getShowcaseHeight() {
                return (int) height;
            }

            @Override
            public float getBlockedRadius() {
                return width;
            }

            @Override
            public void setBackgroundColour(int backgroundColor) {
                // No-op, remove this from the API?
            }

            @Override
            public void erase(Bitmap bitmapBuffer) {
                bitmapBuffer.eraseColor(eraseColour);
            }

            @Override
            public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer) {
                canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
            }

        }




