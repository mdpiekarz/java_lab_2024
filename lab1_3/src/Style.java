import java.util.Locale;

public class Style {
    public final String fillColor, strokeColor;
    public final double strokeWidth;

    public Style(String fillColor, String strokeColor, double strokeWidth) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }
    public String toSvg(){
        return String.format(Locale.ENGLISH, "fill:%s;stroke:%s;strike-width:%f", fillColor, strokeColor, strokeWidth);
    }
}
