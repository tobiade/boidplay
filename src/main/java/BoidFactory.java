import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BoidFactory {
    private static final Random random = new Random();
    private final static List<Boid> boids = new ArrayList<>();

    private static DefaultBoid create(PVector location) {
        return new DefaultBoid(location);
    }

    static List<Boid> createBoids(int initX, int initY) {
        IntStream.range(0, 10).forEach(index -> boids.add(BoidFactory.create(new PVector(random.nextInt(initX), random.nextInt(initY)))));
        return boids;
    }
}
