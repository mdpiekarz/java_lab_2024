public class Main {
    public static void main(String[] args) {
        /*lab1_3.Vec2 p1 = new lab1_3.Vec2(150, 50);
        lab1_3.Vec2 p2 = new lab1_3.Vec2(200, 100);

        lab1_3.Segment line = new lab1_3.Segment(p1, p2);
        System.out.println(line.toSvg());

        lab1_3.Vec2 p3 = new lab1_3.Vec2(5,5);
        lab1_3.Segment[] arr = lab1_3.Segment.perpendicural(line,p3);

        System.out.println("x= " + arr[0].getP2().x + " y= " + arr[0].getP2().y);
        System.out.println("xp= " + arr[1].getP2().x + " yp= " + arr[1].getP2().y);

        lab1_3.Vec2[] points = new lab1_3.Vec2[4];
        points[0] = p1;
        points[1] = p2;
        points[2] = new lab1_3.Vec2(200, 200);
        points[3] = new lab1_3.Vec2(0, 200);

        lab1_3.Polygon square = lab1_3.Polygon.square(new lab1_3.Segment(new lab1_3.Vec2(50,50),new lab1_3.Vec2(100,100)), new lab1_3.Style("red","black",2));

        lab1_3.Polygon polygon = new lab1_3.Polygon(points);
        lab1_3.SvgScene svgScene = new lab1_3.SvgScene();
        svgScene.add(polygon);
        svgScene.add(square);

        lab1_3.Ellipse ellipse = new lab1_3.Ellipse(new lab1_3.Vec2(250,250), 100,50,
                new lab1_3.Style("blue", "black", 5) );
        svgScene.add(ellipse);
        svgScene.save("out.html");
        System.out.println(polygon.toSvg());*/


        Shape polygon = new Polygon(new Vec2[]{new Vec2(120,60),new Vec2(270,280),new Vec2(240,320), new Vec2(110,80)});
        Shape ellipse = new Ellipse(new Vec2(300,400),200,50);

        polygon = new SolidFillShapeDecorator(polygon,"red");
        ellipse = new SolidFillShapeDecorator(ellipse,"green");
        System.out.println(polygon.toSvg(""));
        System.out.println(ellipse.toSvg(""));

        polygon = new StrokeShapeDecorator(polygon, "black", 3);
        System.out.println(polygon.toSvg(""));

        ellipse = new TransformationDecorator.Builder()
                .shape(ellipse)
                .rotate(new Vec2(300, 400), 90)
                .build();

        System.out.println(ellipse.toSvg(""));

    }

}