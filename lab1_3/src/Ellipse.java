import java.util.Locale;

public class Ellipse implements Shape {
    private Vec2 center;
    private double rx, ry;

    public Ellipse(Vec2 center, double rx, double ry) {
//        super(style);
        this.center = center;
        this.rx = rx;
        this.ry = ry;
    }

    @Override
    public String toSvg(String parameters) {
        return String.format(Locale.ENGLISH, "<ellipse cx=\"%f\" cy=\"%f\" rx=\"%f\" ry=\"%f\" %s />",
                center.x, center.y, rx, ry, parameters);

    }
}
