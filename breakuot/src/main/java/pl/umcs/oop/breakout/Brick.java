package pl.umcs.oop.breakout;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Brick extends GraphicsItem{

    static private int gridRows;
    static private int gridCols;
    private Color color;

    public static void setGridRows(int gridRows) {
        Brick.gridRows = gridRows;
    }

    public static void setGridCols(int gridCols) {
        Brick.gridCols = gridCols;
    }
    public Brick(int x, int y, Color color){
        this.width = canvasWidth/gridCols;
        this.height = canvasHeight/gridRows;
        this.x = width * x;
        this.y = height * y;
        this.color = color;
    }
    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x, y, width, height);

        // Dodanie efektu 3D
        Paint darkerColor = color.darker();
        Paint brighterColor = color.brighter();

        graphicsContext.setFill(brighterColor);
        graphicsContext.fillPolygon(
                new double[]{x, x + width, x + width - width * 0.1, x + width * 0.1},
                new double[]{y, y, y - height * 0.1, y - height * 0.1},
                4
        );

        graphicsContext.setFill(darkerColor);
        graphicsContext.fillPolygon(
                new double[]{x + width, x + width, x + width - width * 0.1, x + width - width * 0.1},
                new double[]{y, y + height, y + height - height * 0.1, y - height * 0.1},
                4
        );

        graphicsContext.setFill(darkerColor);
        graphicsContext.fillPolygon(
                new double[]{x, x + width, x + width - width * 0.1, x + width * 0.1},
                new double[]{y + height, y + height, y + height - height * 0.1, y + height - height * 0.1},
                4
        );

        graphicsContext.setFill(brighterColor);
        graphicsContext.fillPolygon(
                new double[]{x, x, x + width * 0.1, x + width * 0.1},
                new double[]{y, y + height, y + height - height * 0.1, y - height * 0.1},
                4
        );

        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeRect(x, y, width, height);
    }

    // Definicja typu wyliczeniowego CrushType
    public enum CrushType {
        NoCrush,
        HorizontalCrush,
        VerticalCrush
    }

    // Metoda do sprawdzania kolizji i określania typu crush
    public CrushType checkCrush(Point2D topLeft, Point2D bottomLeft, Point2D topRight, Point2D bottomRight) {
        // Sprawdź, czy którykolwiek z punktów jest w obrębie cegły
        boolean isColliding = (topLeft.getX() >= x && topLeft.getX() <= x + width && topLeft.getY() >= y && topLeft.getY() <= y + height) ||
                (bottomLeft.getX() >= x && bottomLeft.getX() <= x + width && bottomLeft.getY() >= y && bottomLeft.getY() <= y + height) ||
                (topRight.getX() >= x && topRight.getX() <= x + width && topRight.getY() >= y && topRight.getY() <= y + height) ||
                (bottomRight.getX() >= x && bottomRight.getX() <= x + width && bottomRight.getY() >= y && bottomRight.getY() <= y + height);

        if (!isColliding) {
            return CrushType.NoCrush;
        }

        // Sprawdź, czy kolizja jest pozioma czy pionowa
        boolean verticalCrush = (topLeft.getY() <= y + height && bottomLeft.getY() >= y) || (topRight.getY() <= y + height && bottomRight.getY() >= y);
        boolean horizontalCrush = (topLeft.getX() <= x + width && topRight.getX() >= x) || (bottomLeft.getX() <= x + width && bottomRight.getX() >= x);

        if (verticalCrush && !horizontalCrush) {
            return CrushType.VerticalCrush;
        } else if (horizontalCrush && !verticalCrush) {
            return CrushType.HorizontalCrush;
        } else {
            // Decyduj, czy bardziej pionowy czy poziomy na podstawie kierunku piłki
            double ballXCenter = (topLeft.getX() + bottomRight.getX()) / 2;
            double ballYCenter = (topLeft.getY() + bottomRight.getY()) / 2;
            if (Math.abs(ballXCenter - x - width / 2) > Math.abs(ballYCenter - y - height / 2)) {
                return CrushType.HorizontalCrush;
            } else {
                return CrushType.VerticalCrush;
            }
        }
    }

}
