package org.team.app.view;

import android.os.Handler;
import android.os.Looper;

/// A Timer that runs a provided function periodically, using the main thread's Looper
public class Timer {
    protected Handler handler;
    protected final Listener listener;
    protected final long tickRate;

    private Runnable runnable;
    private Object runnableHandle;

    protected long startTime;

    /// Listener for the Timer, includes function to run every tick
    public static interface Listener {
        public void onTimerResume();

        /// Called every tick
        /// @param timeElapsed: in milliseconds
        public void onTimerTick(long timeElapsed);

        /// Called when the timer is paused/stopped
        /// @param timeElapsed: in milliseconds
        public void onTimerPause(long timeElapsed);
    }

    /// Create a new timer for a given listener
    /// @param listener: The listener to call
    /// @param tickRate: The period in milliseconds
    public Timer(Listener listener, long tickRate) {
        this.handler = new Handler(Looper.getMainLooper());
        this.listener = listener;
        this.tickRate = tickRate;
    }

    /// (Re)start the timer
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

    /// Pause the timer
    /// @return time elapsed in milliseconds
    public long pause() {
        this.handler.removeCallbacks(runnable);
        runnable = null;

        long timeElapsed = System.currentTimeMillis() - startTime;
        listener.onTimerPause(timeElapsed);

        return timeElapsed;
    }

    /// Check if the timer is currently running
    public boolean running() {
        return runnable != null;
    }
}
