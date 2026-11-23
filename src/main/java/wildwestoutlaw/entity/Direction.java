package wildwestoutlaw.entity;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(1, -1),
    DOWN_LEFT(-1, 1),
    DOWN_RIGHT(1, 1);

    // How x and y change when moving one step this way
    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    // e.g. of(1, -1) is UP_RIGHT; of(0, 0) is null
    public static Direction of(int dx, int dy) {
        for (Direction d : values()) {
            if (d.dx == dx && d.dy == dy) {
                return d;
            }
        }
        return null;
    }
}
