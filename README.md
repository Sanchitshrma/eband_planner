# Elastic Band Path Planner

A Java implementation of an elastic band path planning algorithm for dynamic obstacle avoidance in robotics and autonomous navigation.

## Overview

The Elastic Band Planner creates a flexible path that can dynamically adapt to moving obstacles while maintaining smooth trajectories. The algorithm treats the path as a series of connected "bubbles" that are influenced by two types of forces:

- **Internal Forces**: Keep bubbles at optimal distances from each other to maintain path smoothness
- **External Forces**: Push bubbles away from obstacles to ensure collision-free navigation

## Features

- Dynamic obstacle avoidance with moving obstacles
- Smooth path generation and adaptation
- Configurable force parameters for different scenarios
- Real-time path replanning capabilities
- Simple bubble-based path representation

## Algorithm Components

### Core Classes

#### `Bubble`
Represents a point on the path with the following parameters:
- `x, y`: Position coordinates in the 2D plane
- `radius`: Size of the bubble (affects collision detection)
- `fx, fy`: Accumulated force components for position updates

#### `Obstacle`
Moving rectangular obstacles with the following parameters:
- `x, y`: Current position coordinates
- `vx, vy`: Velocity components (pixels/second)
- `width, height`: Dimensions of the rectangular obstacle

#### `ElasticBandPlanner`
Main algorithm implementation with force calculations

#### `Main`
Demo application showing the planner in action

### Key Parameters

- `optimalDistance`: Desired spacing between path bubbles (default: 40)
- `kInternal`: Internal force strength for maintaining bubble spacing (default: 0.2)
- `kExternal`: External force strength for obstacle avoidance (default: 2000)
- `alpha`: Step size for position updates (default: 0.1)

## Usage

### Basic Example

```java
// Create initial path as a series of bubbles
List<Bubble> path = new ArrayList<>(); // Bubble(double x, double y, double radius)
for (int i = 0; i <= 10; i++) {
    path.add(new Bubble(50 + i * 70, 300, 10));
}

// Add moving obstacles
List<Obstacle> obstacles = new ArrayList<>();
obstacles.add(new Obstacle(400, 300, 0, -20, 40, 40)); // Obstacle(double x, double y, double vx, double vy, double width, double height)

// Initialize planner
ElasticBandPlanner planner = new ElasticBandPlanner(path, obstacles);

// Run simulation steps
for (int step = 0; step < 50; step++) {
    planner.step(0.1);  // timestep = 0.1s
    // Process updated path...
}
```

### Running the Demo

```bash
javac *.java
java Main
```

The demo creates a horizontal path that adapts as a moving obstacle approaches from below, demonstrating the dynamic replanning capabilities.

## How It Works

1. **Initialization**: Create an initial path as a series of bubbles between start and goal
2. **Force Calculation**: For each simulation step:
   - Calculate internal forces to maintain optimal bubble spacing
   - Calculate external forces to repel bubbles from obstacles
   - Move obstacle positions based on their velocities
3. **Position Update**: Update bubble positions using accumulated forces
4. **Iteration**: Repeat until convergence or for desired duration

### Force Equations

**Internal Force** (between adjacent bubbles):
```
F_internal = k_internal * (current_distance - optimal_distance) * unit_vector
```

**External Force** (obstacle repulsion):
```
F_external = k_external / (distance² + 1) * unit_vector_away_from_obstacle
```

## Applications

- Mobile robot navigation
- Autonomous vehicle path planning
- Drone trajectory planning
- Game AI pathfinding
- Any scenario requiring smooth, adaptive paths around dynamic obstacles

## Configuration

Adjust the planner parameters based on your specific requirements:

- Increase `kExternal` for stronger obstacle avoidance
- Increase `kInternal` for tighter path smoothness
- Decrease `alpha` for more stable but slower convergence
- Adjust `optimalDistance` based on your path resolution needs

## Future Enhancements

- Support for non-rectangular obstacles
- Variable bubble spacing based on curvature
- Multi-objective optimization (time, smoothness, safety)
- Integration with ROS (Robot Operating System)
- Visualization tools for path planning analysis
