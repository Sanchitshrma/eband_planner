public class Obstacle {
    public double x, y;
    public double vx, vy;
    public double width, height;

    public Obstacle(double x, double y, double vx, double vy, double width, double height) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.width = width;
        this.height = height;
    }

    public void move(double dt) {
        x += vx * dt;
        y += vy * dt;
    }

    public boolean isNear(Bubble b, double safeDist) {
        double dx = b.x - x;
        double dy = b.y - y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist < safeDist + Math.max(width, height) / 2;
    }
}