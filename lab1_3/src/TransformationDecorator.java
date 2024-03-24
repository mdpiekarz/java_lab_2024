import java.util.Locale;

public class TransformationDecorator extends ShapeDecorator {
    String translate, rotate, scale;

    public TransformationDecorator(Shape shape) {
        super(shape);
    }

    @Override
    public String toSvg(String parameters) {
        return decoratedShape.toSvg(String.format(Locale.ENGLISH, "transform=\"%s %s %s\" %s", translate, rotate, scale, parameters));
    }

    public static class Builder {
        private boolean translate = false, rotate = false, scale = false;
        private Vec2 translateVector, rotateCenter, scaleVector;
        private double rotateAngle;
        private Shape shape;

        public Builder translate(Vec2 translateVector) {
            translate = true;
            this.translateVector = translateVector;
            return this;
        }

        public Builder rotate(Vec2 rotateCenter, double rotateAngle) {
            rotate = true;
            this.rotateCenter = rotateCenter;
            this.rotateAngle = rotateAngle;
            return this;
        }

        public Builder scale(Vec2 scaleVector) {
            scale = true;
            this.scaleVector = scaleVector;
            return this;
        }

        public Builder shape(Shape shape) {
            this.shape = shape;
            return this;
        }

        public TransformationDecorator build() {
            TransformationDecorator obj = new TransformationDecorator(shape);

            if (!translate)
                obj.translate = "";
            else {
                obj.translate = String.format(Locale.ENGLISH, "translate(%f %f) ", translateVector.x, translateVector.y);
            }

            if (!rotate)
                obj.rotate = "";
            else {
                obj.rotate = String.format(Locale.ENGLISH, "rotate(%f %f %f) ", rotateAngle, rotateCenter.x, rotateCenter.y);
            }

            if (!scale)
                obj.scale = "";
            else {
                obj.scale = String.format(Locale.ENGLISH, "scale(%f %f) ", scaleVector.x, scaleVector.y);
            }

            return obj;
        }
    }
}
