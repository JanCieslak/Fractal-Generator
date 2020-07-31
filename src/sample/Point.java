package sample;

public class Point {
    private double x;
    private double y;
    private double rotation;

    public Point(double x, double y, double rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
        this.rotation = point.rotation;
    }

    public void add(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void rotate(double theta) {
        rotation += theta;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", rotation=" + rotation +
                '}';
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }
}
