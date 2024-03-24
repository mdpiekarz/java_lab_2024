import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SvgScene {

    List<Shape> shapes;

    public SvgScene(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public SvgScene() {
        this.shapes = new ArrayList<>();
    }

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public void save(String urlPath) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(urlPath));
            bufferedWriter.write("<html> \n <head> \n </head> \n <body> \n ");
            bufferedWriter.write(" <svg width=\"500\" height=\"500\" > \n");
            for (Shape polygon : shapes) {
                bufferedWriter.write(polygon.toSvg(""));
                bufferedWriter.write("\n");
            }
            bufferedWriter.write("</svg> \n ");
            bufferedWriter.write("</body> \n </html> ");
            bufferedWriter.close();
        } catch (IOException exception) {
            System.out.println(exception);
        }
    }
}
