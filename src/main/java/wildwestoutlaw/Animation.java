package wildwestoutlaw;

// Keeps track of which frame of an animation to show. update() is called once per game tick.
public class Animation {

    private final int frameCount;
    private final int ticksPerFrame;
    private final boolean loops;
    private int tick = 0;

    public Animation(int frameCount, int ticksPerFrame, boolean loops) {
        this.frameCount = frameCount;
        this.ticksPerFrame = ticksPerFrame;
        this.loops = loops;
    }

    public void update() {
        int length = frameCount * ticksPerFrame;
        if (loops) {
            tick = (tick + 1) % length;
        } else if (tick < length) {
            tick++;
        }
    }

    public int frame() {
        return Math.min(tick / ticksPerFrame, frameCount - 1);
    }

    // Only for animations that don't loop
    public boolean isFinished() {
        return !loops && tick >= frameCount * ticksPerFrame;
    }

    public void restart() {
        tick = 0;
    }
}
