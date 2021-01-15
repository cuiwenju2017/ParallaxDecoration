package com.example.parallaxdecoration;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import kotlin.collections.IntIterator;
import kotlin.ranges.RangesKt;

public class ParallaxDecoration extends RecyclerView.ItemDecoration {

    private int screenWidth;
    private int screenHeight;
    private int maxVisibleCount;
    private int minVisibleCount;
    private int bitmapWidth;
    private int bitmapHeight;
    private int bitmapCount;
    private List bitmapPool;
    private boolean isHorizontal;
    private float parallax;
    private boolean autoFill;
    private ActivityManager am;
    private BitmapFactory.Options bitmapOption;
    private float scale;
    private int scaleBitmapWidth;
    private int scaleBitmapHeight;
    private final Context context;

    public final float getParallax() {
        return this.parallax;
    }

    public final void setParallax(float var1) {
        this.parallax = var1;
    }

    public final boolean getAutoFill() {
        return this.autoFill;
    }

    public final void setAutoFill(boolean var1) {
        this.autoFill = var1;
    }

    public final void setupBitmap(List bitmaps) {
       bitmapPool.clear();
       bitmapPool.addAll((Collection) bitmaps);
       updateConfig();
    }

    public final void addBitmap(Bitmap bitmap) {
        bitmapPool.add(bitmap);
        updateConfig();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public final void setupResource(List resources) {
        bitmapPool.clear();
        Iterator iterator = resources.iterator();
        while (iterator.hasNext()) {
            int resourceId = ((Number) iterator.next()).intValue();
            bitmapPool.add(decodeBitmap(resourceId));
        }
        this.updateConfig();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public final void addResource(int resourceId) {
        bitmapPool.add(decodeBitmap(resourceId));
        updateConfig();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private final Bitmap decodeBitmap(int resourceId) {
        if (am == null) {
            bitmapOption = new BitmapFactory.Options();
            Object object = context.getSystemService(Context.ACTIVITY_SERVICE);
            if (!(object instanceof ActivityManager)) {
                object = null;
            }

            am = (ActivityManager) object;
            ActivityManager activityManager = am;
            if (activityManager.isLowRamDevice()) {
                BitmapFactory.Options options = bitmapOption;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
            }
        }

        Resources resources = context.getResources();
        BitmapFactory.Options options = bitmapOption;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId, options);
        return bitmap;
    }

    private final void updateConfig() {
        bitmapCount = bitmapPool.size();
        bitmapWidth = ((Bitmap) bitmapPool.get(0)).getWidth();
        bitmapHeight = ((Bitmap) bitmapPool.get(0)).getHeight();
        scaleBitmapWidth = bitmapWidth;
        scaleBitmapHeight = bitmapHeight;
        Iterable iterable = (Iterable) bitmapPool;
        Iterator iterator = iterable.iterator();

        Bitmap it;
        do {
            if (!iterator.hasNext()) {
                return;
            }

            Object object = iterator.next();
            it = (Bitmap) object;
        } while (it.getWidth() == bitmapWidth && it.getHeight() == bitmapHeight);

        try {
            throw (Throwable) (new RuntimeException("Every bitmap of the backgrounds must has the same size!"));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Collection collection = (Collection) bitmapPool;
        if (!collection.isEmpty()) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            RecyclerView.LayoutManager lm = layoutManager;
            isHorizontal = lm.canScrollHorizontally();
            boolean doubleOutOfScreen;
            if (screenWidth == 0 || screenHeight == 0) {
                screenWidth = c.getWidth();
                screenHeight = c.getHeight();
                int allInScreen;
                if (isHorizontal) {
                    if (autoFill) {
                        scale = (float) screenHeight * 1.0F / (float) bitmapHeight;
                        scaleBitmapWidth = (int) ((float) bitmapWidth * scale);
                    }

                    allInScreen = screenWidth / scaleBitmapWidth;
                    doubleOutOfScreen = screenWidth % scaleBitmapWidth > 1;
                } else {
                    if (autoFill) {
                        scale = (float) screenWidth * 1.0F / (float) bitmapWidth;
                        scaleBitmapHeight = (int) ((float) bitmapHeight * scale);
                    }

                    allInScreen = screenHeight / scaleBitmapHeight;
                    doubleOutOfScreen = this.screenHeight % this.scaleBitmapHeight > 1;
                }

                this.minVisibleCount = allInScreen + 1;
                this.maxVisibleCount = doubleOutOfScreen ? allInScreen + 2 : this.minVisibleCount;
            }

            float parallaxOffset = 0.0F;
            float firstVisibleOffset = 0.0F;
            int firstVisible;
            if (this.isHorizontal) {
                parallaxOffset = (float) lm.computeHorizontalScrollOffset(state) * this.parallax;
                firstVisible = (int) (parallaxOffset / (float) this.scaleBitmapWidth);
                firstVisibleOffset = parallaxOffset % (float) this.scaleBitmapWidth;
            } else {
                parallaxOffset = (float) lm.computeVerticalScrollOffset(state) * this.parallax;
                firstVisible = (int) (parallaxOffset / (float) this.scaleBitmapHeight);
                firstVisibleOffset = parallaxOffset % (float) this.scaleBitmapHeight;
            }

            int bestDrawCount = (int) firstVisibleOffset == 0 ? this.minVisibleCount : this.maxVisibleCount;
            c.save();
            if (this.isHorizontal) {
                c.translate(-firstVisibleOffset, 0.0F);
            } else {
                c.translate(0.0F, -firstVisibleOffset);
            }

            if (autoFill) {
                c.scale(scale, scale);
            }

            int i = 0;

            for (Iterator iterator = ((Iterable) RangesKt.until(firstVisible, firstVisible + bestDrawCount)).iterator(); iterator.hasNext(); ++i) {
                int currentIndex = ((IntIterator) iterator).nextInt();
                if (isHorizontal) {
                    c.drawBitmap((Bitmap) bitmapPool.get(currentIndex % bitmapCount), (float) i * (float) bitmapWidth, 0.0F, (Paint) null);
                } else {
                    c.drawBitmap((Bitmap) bitmapPool.get(currentIndex % bitmapCount), 0.0F, (float) i * (float) bitmapHeight, (Paint) null);
                }
            }

            c.restore();
        }

    }

    public ParallaxDecoration(Context context) {
        super();
        this.context = context;
        List list = new ArrayList();
        bitmapPool = list;
        isHorizontal = true;
        parallax = 1.0F;
        scale = 1.0F;
    }
}
