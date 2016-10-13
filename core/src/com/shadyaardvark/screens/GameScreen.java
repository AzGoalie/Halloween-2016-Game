package com.shadyaardvark.screens;

import static com.shadyaardvark.ShapeFactory.getChain;
import static com.shadyaardvark.ShapeFactory.getCircle;
import static com.shadyaardvark.ShapeFactory.getPolygon;
import static com.shadyaardvark.ShapeFactory.getRectangle;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shadyaardvark.Constants;
import com.shadyaardvark.HalloweenGame2016;
import com.shadyaardvark.components.LightComponent;
import com.shadyaardvark.components.PhysicsComponent;
import com.shadyaardvark.components.PlayerComponent;
import com.shadyaardvark.components.TransformComponent;
import com.shadyaardvark.listeners.LightListener;
import com.shadyaardvark.systems.Box2DDebugSystem;
import com.shadyaardvark.systems.LightingSystem;
import com.shadyaardvark.systems.MapRenderSystem;
import com.shadyaardvark.systems.PhysicsSystem;
import com.shadyaardvark.systems.PlayerSystem;

import box2dLight.RayHandler;

public class GameScreen extends ScreenAdapter {
    private Viewport viewport;

    private OrthographicCamera camera;

    private Music spookyMusic;

    private Engine engine;

    private World world;

    private Box2DDebugRenderer b2dr;

    private TiledMap tiledMap;

    private OrthogonalTiledMapRenderer tiledMapRenderer;

    private RayHandler rayHandler;

    private LightingSystem lightingSystem;

    public GameScreen(HalloweenGame2016 game) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
        viewport.apply(true);

        tiledMap = new TmxMapLoader().load("test.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.PPM);

        world = new World(getGravity(), true);
        b2dr = new Box2DDebugRenderer();

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.2f);

        engine = new Engine();

        PhysicsSystem physicsSystem = new PhysicsSystem(world);
        MapRenderSystem mapRenderSystem = new MapRenderSystem(tiledMapRenderer, camera);
        lightingSystem = new LightingSystem(rayHandler, viewport);
        Box2DDebugSystem box2DDebugSystem = new Box2DDebugSystem(b2dr, world, camera);

        engine.addSystem(physicsSystem);
        engine.addSystem(mapRenderSystem);
        engine.addSystem(lightingSystem);
        engine.addSystem(box2DDebugSystem);
        engine.addSystem(new PlayerSystem());

        engine.addEntityListener(LightListener.getFamily(), new LightListener(rayHandler));

        spookyMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/spooky song.mp3"));
        spookyMusic.setLooping(true);

        createMap();
    }

    @Override
    public void show() {
        spookyMusic.play();
    }

    @Override
    public void resume() {
        spookyMusic.play();
    }

    @Override
    public void hide() {
        spookyMusic.pause();
    }

    @Override
    public void pause() {
        spookyMusic.pause();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        lightingSystem.resize();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);
    }

    @Override
    public void dispose() {
        spookyMusic.dispose();
        world.dispose();
        tiledMap.dispose();
        tiledMapRenderer.dispose();
        rayHandler.dispose();
    }

    private Vector2 getGravity() {
        Vector2 gravity = Constants.DEFAULT_GRAVITY;

        if (tiledMap.getProperties()
                .containsKey("gravity")) {
            gravity = new Vector2(0,
                    tiledMap.getProperties()
                            .get("gravity", Float.class));
        }

        return gravity;
    }

    private void createMap() {
        MapLayer collisionLayer = tiledMap.getLayers()
                .get("collisions");
        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof TextureMapObject) {
                continue;
            }

            Entity entity = new Entity();

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

            if ("player".equals(object.getName())) {
                entity.add(new PlayerComponent());
                bodyDef.type = BodyDef.BodyType.DynamicBody;
            } else {
                bodyDef.type = BodyDef.BodyType.StaticBody;
            }
            Body body = world.createBody(bodyDef);
            Fixture fixture = body.createFixture(shape, 1);
            if ("ladder".equals(object.getName())) {
                fixture.setSensor(true);
            }

            shape.dispose();

            TransformComponent transformComponent = new TransformComponent();
            transformComponent.position.set(body.getPosition(), 0);

            PhysicsComponent physicsComponent = new PhysicsComponent();
            physicsComponent.body = body;

            entity.add(transformComponent);
            entity.add(physicsComponent);
            engine.addEntity(entity);
        }

        createLights();
    }

    private void createLights() {
        MapLayer lightLayer = tiledMap.getLayers()
                .get("lights");
        for (MapObject light : lightLayer.getObjects()) {
            Ellipse shape = ((EllipseMapObject) light).getEllipse();

            LightComponent lightComponent = new LightComponent();
            lightComponent.type = LightComponent.LightType.PointLight;
            lightComponent.intensity = shape.width * Constants.PPM;
            lightComponent.numRays = Constants.NUM_RAYS;

            TransformComponent transformComponent = new TransformComponent();
            transformComponent.position.x = (shape.x + shape.width / 2) * Constants.PPM;
            transformComponent.position.y = (shape.y + shape.height / 2) * Constants.PPM;

            Entity entity = new Entity();
            entity.add(lightComponent);
            entity.add(transformComponent);
            engine.addEntity(entity);
        }
    }
}
