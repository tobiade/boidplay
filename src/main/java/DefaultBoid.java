import processing.core.PVector;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultBoid implements Boid {
    private PVector location;
    private PVector velocity;
    private PVector acceleration;
    private static final float NEIGHBOURHOOD_RADIUS = 50;
    private static final float MAX_SPEED = 4;
    private static final float MAX_FORCE = 1;

    DefaultBoid(PVector location) {
        this.location = location;
        this.velocity = PVector.random2D();
        this.acceleration = new PVector();
    }

    public PVector alignmentForce(List<Boid> boids) {
        List<Boid> boidsInNeighbourhood = boids.stream()
                .filter(otherBoid -> otherBoid.isNear(this, NEIGHBOURHOOD_RADIUS))
                .collect(Collectors.toList());
        if (boidsInNeighbourhood.isEmpty()) return new PVector();

        PVector totalVelocityOfBoidsInNeighbourhood = boidsInNeighbourhood.stream()
                .map(Boid::currentVelocity)
                .reduce(new PVector(), (totalVelocity, currentVelocity) -> totalVelocity.add(currentVelocity));

        PVector targetForce = totalVelocityOfBoidsInNeighbourhood.div(boidsInNeighbourhood.size());
        targetForce.setMag(MAX_SPEED);//upscale magnitude
        targetForce.sub(this.velocity);
        return targetForce.limit(MAX_FORCE); //steering force
    }

    public PVector cohesionForce(List<Boid> boids) {
        List<Boid> boidsInNeighbourhood = boids.stream()
                .filter(otherBoid -> otherBoid.isNear(this, NEIGHBOURHOOD_RADIUS * 2))
                .collect(Collectors.toList());
        if (boidsInNeighbourhood.isEmpty()) return new PVector();

        PVector totalLocationOfBoidsInNeighbourhood = boidsInNeighbourhood.stream()
                .map(Boid::currentLocation)
                .reduce(new PVector(), (totalLocation, currentLocation) -> totalLocation.add(currentLocation));

        PVector averageLocationOfBoidsInNeighbourhood = totalLocationOfBoidsInNeighbourhood.div(boidsInNeighbourhood.size());//average currentLocation
        averageLocationOfBoidsInNeighbourhood.sub(this.location); //steering force
        averageLocationOfBoidsInNeighbourhood.setMag(MAX_SPEED);
        return averageLocationOfBoidsInNeighbourhood.limit(MAX_FORCE);

    }

    public PVector separationForce(List<Boid> boids) {
        List<Boid> boidsInNeighbourhood = boids.stream()
                .filter(otherBoid -> otherBoid.isNear(this, NEIGHBOURHOOD_RADIUS))
                .collect(Collectors.toList());
        if (boidsInNeighbourhood.isEmpty()) return new PVector();

        PVector totalSeparationOfBoidsInNeighbourhood = boidsInNeighbourhood.stream()
                .map(boid -> {
                    PVector copy = this.location.copy();
                    PVector diff = copy.sub(boid.currentLocation());
                    diff.div(diff.mag() * diff.mag());
                    return diff;
                })
                .reduce(new PVector(), (totalSeparation, currentSeparation) -> totalSeparation.add(currentSeparation));

        PVector averageSeparationOfBoidsInNeighbourhood = totalSeparationOfBoidsInNeighbourhood.div(boidsInNeighbourhood.size());//average currentLocation
        averageSeparationOfBoidsInNeighbourhood.setMag(MAX_SPEED);
        return averageSeparationOfBoidsInNeighbourhood.limit(MAX_FORCE * 1.5f);
    }


    public void flockTo(List<Boid> boids) {
        this.acceleration.add(alignmentForce(boids));
        this.acceleration.add(cohesionForce(boids));
        this.acceleration.add(separationForce(boids));
        this.update();
    }

    private void update() {
        this.velocity.add(this.acceleration);
        this.velocity.limit(MAX_SPEED);
        this.location.add(this.velocity);
        this.acceleration.mult(0);
    }

    public PVector currentLocation() {
        return location;
    }

    public PVector currentVelocity() {
        return velocity;
    }

    public boolean isNear(Boid otherBoid, float radius) {
        if (otherBoid == this) return false;
        float distanceBetweenBoids = this.location.dist(otherBoid.currentLocation());
        return distanceBetweenBoids < radius;
    }

}
