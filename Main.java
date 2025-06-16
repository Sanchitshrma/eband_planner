import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int areaWidth = 800;
        int areaHeight = 600;

        List<Bubble> path = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            path.add(new Bubble(50 + i * 70, 300, 10));
        }

        List<Obstacle> obstacles = new ArrayList<>();
        obstacles.add(new Obstacle(400, 300, 0, -20, 40, 40)); // obstacle moving upward

        ElasticBandPlanner planner = new ElasticBandPlanner(path, obstacles);

        for (int step = 0; step < 50; step++) {
            planner.step(0.1);  // timestep = 0.1s
            System.out.println("Step " + step + ":");
            for (Bubble b : planner.getBand()) {
                System.out.printf("Bubble at (%.1f, %.1f)\n", b.x, b.y);
            }
            System.out.println("----------");
        }
    }
}