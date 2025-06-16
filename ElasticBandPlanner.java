import java.util.List;

public class ElasticBandPlanner {
    private List<Bubble> band;
    private List<Obstacle> obstacles;
    private double optimalDistance = 40;
    private double kInternal = 0.2;
    private double kExternal = 2000;
    private double alpha = 0.1;

    public ElasticBandPlanner(List<Bubble> band, List<Obstacle> obstacles) {
        this.band = band;
        this.obstacles = obstacles;
    }

    public void step(double dt) {
        for (Obstacle obs : obstacles)
            obs.move(dt);

        for (int i = 1; i < band.size() - 1; i++) {
            Bubble prev = band.get(i - 1);
            Bubble curr = band.get(i);
            Bubble next = band.get(i + 1);

            curr.fx = 0;
            curr.fy = 0;

            applyInternal(curr, prev);
            applyInternal(curr, next);

            for (Obstacle obs : obstacles) {
                applyExternal(curr, obs);
            }

            curr.x += alpha * curr.fx;
            curr.y += alpha * curr.fy;
        }
    }

    private void applyInternal(Bubble curr, Bubble neighbor) {
        double dx = neighbor.x - curr.x;
        double dy = neighbor.y - curr.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double diff = dist - optimalDistance;

        if (dist > 0.01) {
            curr.fx += kInternal * diff * (dx / dist);
            curr.fy += kInternal * diff * (dy / dist);
        }
    }

    private void applyExternal(Bubble bubble, Obstacle obs) {
        double dx = bubble.x - obs.x;
        double dy = bubble.y - obs.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double safeDist = 0.5 * Math.max(obs.width, obs.height) + 50;

        if (dist < safeDist && dist > 0.01) {
            double force = kExternal / (dist * dist + 1);
            bubble.fx += (dx / dist) * force;
            bubble.fy += (dy / dist) * force;
        }
    }

    public List<Bubble> getBand() {
        return band;
    }
}