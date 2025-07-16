import java.util.ArrayList;
import java.util.List;

public class Main {
   public static void main(String[] args) {
    int areaWidth = 800;
    int areaHeight = 600;

    // Create initial path with fewer bubbles to see dynamic addition
    List<Bubble> path = new ArrayList<>();
    for (int i = 0; i <= 5; i++) {
        path.add(new Bubble(50 + i * 120, 300, 10));
    }

    List<Obstacle> obstacles = new ArrayList<>();
    // Add multiple obstacles to create more complex scenarios
    obstacles.add(new Obstacle(300, 350, 0, -15, 40, 40)); // moving up
    obstacles.add(new Obstacle(500, 250, 0, 20, 50, 30));  // moving down

    ElasticBandPlanner planner = new ElasticBandPlanner(path, obstacles);
    
    // Configure dynamic bubble management
    planner.setMaxDistance(80);  // Add bubbles when segments exceed this
    planner.setMinDistance(25);  // Remove bubbles when segments are shorter
    planner.setMinBubbles(4);    // Always keep at least 4 bubbles

    System.out.println("Starting with " + planner.getBubbleCount() + " bubbles");
    System.out.println("==========================================");

    for (int step = 0; step < 100; step++) {
        int beforeCount = planner.getBubbleCount();
        planner.step(0.1);
        int afterCount = planner.getBubbleCount();
        
        System.out.println("Step " + step + " (Bubbles: " + afterCount + "):");
        
        // Only show bubble positions every 10 steps to reduce output
        if (step % 10 == 0) {
            for (int i = 0; i < planner.getBand().size(); i++) {
                Bubble b = planner.getBand().get(i);
                String type = (i == 0) ? " [START]" : (i == planner.getBand().size() - 1) ? " [END]" : "";
                System.out.printf("  Bubble %d at (%.1f, %.1f)%s\n", i, b.x, b.y, type);
            }
        }
        
        if (beforeCount != afterCount) {
            System.out.println("  >>> Bubble count changed: " + beforeCount + " -> " + afterCount);
        }
        
        System.out.println("----------");
    }
}
}