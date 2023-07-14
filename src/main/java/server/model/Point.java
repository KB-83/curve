package server.model;

public class Point{
    private int x;
    private int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public static Point middlePoint(int x, int y, int x1, int y1){
        return new Point((x+x1)/2,(y+y1)/2);
    }
    public static int distance(int x, int y, int x1, int y1){
        return (int) Math.sqrt(Math.pow(x-x1,2)+Math.pow(y-y1,2));
    }


}
