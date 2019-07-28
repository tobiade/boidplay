import processing.core.PVector;

import java.util.List;

public interface Boid {
    PVector cohesionForce(List<Boid> boids);

    PVector separationForce(List<Boid> boids);

    PVector alignmentForce(List<Boid> boids);

    PVector currentLocation();

    PVector currentVelocity();

    void flockTo(List<Boid> otherBoids);

    boolean isNear(Boid otherBoid, float radius);
}
