import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

public class BoidController {
    private PApplet sketch;
    private List<Boid> boids;


    BoidController(PApplet sketch) {
        this.sketch = sketch;
        boids = BoidFactory.createBoids(sketch.width, sketch.height);
    }

    private void show(Boid boid, List<Boid> otherBoids) {
        makeBoidReAppearIfItHasExitedWindow(boid);
        boid.flockTo(otherBoids);
        updateSketchForBoid(boid);
    }

    void draw() {
        for (Boid boid : boids) {
            this.show(boid, boids);
        }
    }

    private void makeBoidReAppearIfItHasExitedWindow(Boid boid) {
        PVector location = boid.currentLocation();
        if (location.x > sketch.width) {
            location.x = 0;
        } else if (location.x < 0) {
            location.x = sketch.width;
        }

        if (location.y > sketch.height) {
            location.y = 0;
        } else if (location.y < 0) {
            location.y = sketch.height;
        }
    }

    private void updateSketchForBoid(Boid boid) {
        sketch.strokeWeight(8);
        sketch.stroke(255);
        PVector location = boid.currentLocation();
        sketch.point(location.x, location.y);
    }

}
