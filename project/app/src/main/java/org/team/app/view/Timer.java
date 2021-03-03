package org.team.app.view;

import android.os.Handler;
import android.os.Looper;

public class Timer {
    protected Handler handler;
    protected final Listener listener;
    protected final long tickRate;

    private Runnable runnable;
    private Object runnableHandle;

    protected long startTime;

    public static interface Listener {
        public void onTimerResume();

        public void onTimerTick(long timeElapsed);

        public void onTimerPause(long timeElapsed);
    }

    public Timer(Listener listener, long tickRate) {
        this.handler = new Handler(Looper.getMainLooper());
        this.listener = listener;
        this.tickRate = tickRate;
    }

    public void resume() {
        listener.onTimerResume();

        this.startTime = System.currentTimeMillis();
        runnable = new Runnable() {
                @Override
                public void run() {
                    long timeElapsed = System.currentTimeMillis() - startTime;
                    listener.onTimerTick(timeElapsed);

                    if(this == runnable)
                        handler.postDelayed(this, tickRate);
                }
            };

        runnable.run();
    }

    public long pause() {
        this.handler.removeCallbacks(runnable);
        runnable = null;

        long timeElapsed = System.currentTimeMillis() - startTime;
        listener.onTimerPause(timeElapsed);

        return timeElapsed;
    }

    public boolean running() {
        return runnable != null;
    }
}
