import java.util.ArrayList;
import java.util.List;

public class ElasticBandPlanner {
    private List<Bubble> band;
    private List<Obstacle> obstacles;
    private double optimalDistance = 40;
    private double kInternal = 0.2;
    private double maxDistance = 60;  // Distance threshold for adding bubbles
    private double minDistance = 20;  // Distance threshold for removing bubbles
    private int minBubbles = 3;       // Minimum number of bubbles to maintain
    private double kExternal = 2000;
    private double alpha = 0.1;

    public ElasticBandPlanner(List<Bubble> band, List<Obstacle> obstacles) {
        this.band = new ArrayList<>(band); // Make a copy to allow modifications
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

        // Dynamic bubble management
        manageBubbles();
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

    private void manageBubbles() {
    // Process from end to beginning to avoid index issues when removing
    for (int i = band.size() - 1; i > 0; i--) {
        Bubble curr = band.get(i);
        Bubble prev = band.get(i - 1);
        
        double distance = getDistance(curr, prev);
        
        // Add bubble if segment is too long
        if (distance > maxDistance) {
            addBubble(i - 1, i);
        }
        // Remove bubble if segment is too short (but maintain minimum count)
        else if (distance < minDistance && band.size() > minBubbles && i > 1 && i < band.size() - 1) {
            removeBubble(i);
        }
    }
}

private void addBubble(int index1, int index2) {
    Bubble bubble1 = band.get(index1);
    Bubble bubble2 = band.get(index2);
    
    // Create new bubble at midpoint
    double midX = (bubble1.x + bubble2.x) / 2;
    double midY = (bubble1.y + bubble2.y) / 2;
    double avgRadius = (bubble1.radius + bubble2.radius) / 2;
    
    Bubble newBubble = new Bubble(midX, midY, avgRadius);
    band.add(index2, newBubble);
    
    System.out.println("Added bubble at (" + String.format("%.1f", midX) + ", " + String.format("%.1f", midY) + ")");
}

private void removeBubble(int index) {
    // Don't remove start or end bubbles
    if (index > 0 && index < band.size() - 1) {
        Bubble removed = band.remove(index);
        System.out.println("Removed bubble at (" + String.format("%.1f", removed.x) + ", " + String.format("%.1f", removed.y) + ")");
    }
}

private double getDistance(Bubble b1, Bubble b2) {
    double dx = b1.x - b2.x;
    double dy = b1.y - b2.y;
    return Math.sqrt(dx * dx + dy * dy);
}

// Getters and setters for the new parameters
public void setMaxDistance(double maxDistance) {
    this.maxDistance = maxDistance;
}

public void setMinDistance(double minDistance) {
    this.minDistance = minDistance;
}

public void setMinBubbles(int minBubbles) {
    this.minBubbles = minBubbles;
}

public double getMaxDistance() {
    return maxDistance;
}

public double getMinDistance() {
    return minDistance;
}

public int getMinBubbles() {
    return minBubbles;
}

public int getBubbleCount() {
    return band.size();
}
}