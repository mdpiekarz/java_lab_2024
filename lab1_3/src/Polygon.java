import java.util.Arrays;

public class Polygon implements Shape {
    private Vec2[] points;

    public Polygon(Vec2[] points){
//        super(new lab1_3.Style("transparent", "black", 1));
        this.points = Arrays.copyOf(points, points.length);
    }

    public Polygon(Vec2[] points,Style style){
//        super(new lab1_3.Style("transparent", "black", 1));
        this.points = Arrays.copyOf(points, points.length);
    }

    public Polygon(Polygon pol) {
//        super(style);
        points = new Vec2[pol.points.length];
        for  (int i = 0; i < pol.points.length; ++i) {
            points[i] = new Vec2(pol.points[i].x, pol.points[i].y);
        }
    }

    @Override
    public String toSvg(String parameters){
        String res = "<polygon points=\"";
        for (Vec2 point : points)
            res += point.x + "," + point.y + " ";
        res+="\" "+ parameters + " />";
        return res;
    }

    public static Polygon square(Segment line, Style style){
        Vec2 center = new Vec2((line.getP1().x + line.getP2().x)/2, (line.getP1().y + line.getP2().y)/2);

        Segment[] lines = Segment.perpendicural(line,center,line.distance()/2);

        return new Polygon(new Vec2[] {line.getP1(),lines[0].getP2(), line.getP2(), lines[1].getP2()},
                style);

    }
}
