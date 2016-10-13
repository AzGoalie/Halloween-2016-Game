package com.shadyaardvark;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class ShapeFactory {
    public static PolygonShape getRectangle(Rectangle rectangle) {
        PolygonShape polygonShape = new PolygonShape();
        Vector2 size = new Vector2(Constants.PPM * (rectangle.x + rectangle.width * 0.5f),
                Constants.PPM * (rectangle.y + rectangle.height * 0.5f));

        polygonShape.setAsBox(Constants.PPM * (rectangle.width * 0.5f),
                Constants.PPM * (rectangle.height * 0.5f),
                size,
                0.0f);

        return polygonShape;
    }

    public static PolygonShape getPolygon(Polygon polygon) {
        PolygonShape polygonShape = new PolygonShape();
        float[] vertices = polygon.getTransformedVertices();
        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = Constants.PPM * vertices[i];
        }

        polygonShape.set(worldVertices);
        return polygonShape;
    }

    public static ChainShape getChain(Polyline polyline) {
        ChainShape chain = new ChainShape();
        float[] vertices = polyline.getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = Constants.PPM * vertices[i * 2];
            worldVertices[i].y = Constants.PPM * vertices[i * 2 + 1];
        }

        chain.createChain(worldVertices);
        return chain;
    }

    public static CircleShape getCircle(Circle circle) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Constants.PPM * circle.radius);
        circleShape.setPosition(new Vector2(Constants.PPM * circle.x, Constants.PPM * circle.y));
        return circleShape;
    }
}
