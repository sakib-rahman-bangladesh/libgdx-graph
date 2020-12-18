package com.gempukku.libgdx.graph.time;

public class SimpleTimeKeeper implements TimeKeeper {
    private float timeCumulative = -1;
    private float delta;
    private boolean paused;

    @Override
    public void updateTime(float delta) {
        if (!paused) {
            this.delta = delta;
            if (timeCumulative > -1)
                timeCumulative += delta;
            else
                timeCumulative = 0;
        } else {
            this.delta = 0;
        }
    }

    @Override
    public float getTime() {
        return timeCumulative;
    }

    @Override
    public float getDelta() {
        return delta;
    }

    public void pauseTime() {
        paused = true;
    }

    public void resumeTime() {
        paused = false;
    }

    public void setTime(float time) {
        timeCumulative = time;
    }
}
