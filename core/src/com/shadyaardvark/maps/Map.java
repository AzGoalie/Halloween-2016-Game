package com.shadyaardvark.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.shadyaardvark.Constants;
import com.shadyaardvark.Player;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Map implements Disposable {
    private static final String GRAVITY_PROPERTY = "gravity";

    private static final String COLLISION_LAYER = "collisions";

    private static final String LIGHT_LAYER = "lights";

    private static final String LADDER = "ladder";

    private static final String PLAYER = "player";

    private TiledMap tiledMap;

    private World world;

    private RayHandler rayHandler;

    private Player player;

    public Map(String filename) {
        tiledMap = new TmxMapLoader().load(filename);

        Vector2 gravity = Constants.DEFAULT_GRAVITY;
        if (tiledMap.getProperties()
                .containsKey(GRAVITY_PROPERTY)) {
            gravity = new Vector2(tiledMap.getProperties()
                    .get(GRAVITY_PROPERTY, Float.class), 0);
        }
        world = new World(gravity, true);

        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 1f);
        rayHandler.setBlurNum(3);

        createCollisions();
        createLights();
    }

    public void update(float dt) {
        player.update(dt);

        world.step(dt, 8, 3);

    }

    private void createCollisions() {
        MapLayer collisionLayer = tiledMap.getLayers()
                .get(COLLISION_LAYER);
        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof TextureMapObject) {
                continue;
            }

            if (PLAYER.equals(object.getName())) {
                float x = object.getProperties()
                        .get("x", 0f, Float.class) * Constants.PPM;
                float y = object.getProperties()
                        .get("y", 0f, Float.class) * Constants.PPM;
                player = new Player(world, new Vector2(x, y));
                continue;
            }

            Shape shape;
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                shape = getRectangle(rectangle);
            } else if (object instanceof PolygonMapObject) {
                Polygon polygon = ((PolygonMapObject) object).getPolygon();
                shape = getPolygon(polygon);
            } else if (object instanceof PolylineMapObject) {
                Polyline polyline = ((PolylineMapObject) object).getPolyline();
                shape = getChain(polyline);
            } else if (object instanceof CircleMapObject) {
                Circle circle = ((CircleMapObject) object).getCircle();
                shape = getCircle(circle);

            } else {
                continue;
            }

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bodyDef);
            Fixture fixture = body.createFixture(shape, 1);
            if (LADDER.equals(object.getName())) {
                fixture.setSensor(true);
            }

            shape.dispose();
        }
    }

    private void createLights() {
        MapLayer lightLayer = tiledMap.getLayers()
                .get(LIGHT_LAYER);
        for (MapObject light : lightLayer.getObjects()) {
            Ellipse shape = ((EllipseMapObject) light).getEllipse();
            new PointLight(rayHandler,
                    Constants.NUM_RAYS,
                    new Color(1, 1, 1, 0.5f),
                    shape.width * Constants.PPM,
                    (shape.x + shape.width / 2) * Constants.PPM,
                    (shape.y + shape.height / 2) * Constants.PPM);
        }
    }

    private PolygonShape getRectangle(Rectangle rectangle) {
        PolygonShape polygonShape = new PolygonShape();
        Vector2 size = new Vector2(Constants.PPM * (rectangle.x + rectangle.width * 0.5f),
                Constants.PPM * (rectangle.y + rectangle.height * 0.5f));

        polygonShape.setAsBox(Constants.PPM * (rectangle.width * 0.5f),
                Constants.PPM * (rectangle.height * 0.5f),
                size,
                0.0f);

        return polygonShape;
    }

    private PolygonShape getPolygon(Polygon polygon) {
        PolygonShape polygonShape = new PolygonShape();
        float[] vertices = polygon.getTransformedVertices();
        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = Constants.PPM * vertices[i];
        }

        polygonShape.set(worldVertices);
        return polygonShape;
    }

    private ChainShape getChain(Polyline polyline) {
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

    private CircleShape getCircle(Circle circle) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Constants.PPM * circle.radius);
        circleShape.setPosition(new Vector2(Constants.PPM * circle.x, Constants.PPM * circle.y));
        return circleShape;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public World getWorld() {
        return world;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void dispose() {
        world.dispose();
        tiledMap.dispose();
        rayHandler.dispose();
    }
}
