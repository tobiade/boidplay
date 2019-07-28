import processing.core.PApplet;

public class BoidSketch extends PApplet {

    private BoidController boidController = new BoidController(this);

    public void settings() {
        size(800, 800);
        smooth();
    }

    public void draw() {
        background(51);
        boidController.draw();
    }

    public static void main(String[] args) {
        String[] processingArgs = {"BoidSketch"};
        BoidSketch boidSketch = new BoidSketch();
        PApplet.runSketch(processingArgs, boidSketch);
    }
}
