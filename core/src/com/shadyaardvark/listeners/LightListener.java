package com.shadyaardvark.listeners;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.utils.ObjectMap;
import com.shadyaardvark.Constants;
import com.shadyaardvark.components.LightComponent;
import com.shadyaardvark.components.TransformComponent;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightListener implements EntityListener {
    private RayHandler rayHandler;

    private ComponentMapper<TransformComponent> tm =
            ComponentMapper.getFor(TransformComponent.class);

    private ComponentMapper<LightComponent> lm = ComponentMapper.getFor(LightComponent.class);

    private ObjectMap<Entity, Light> lights;

    public LightListener(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
        lights = new ObjectMap<>();
    }

    @Override
    public void entityAdded(Entity entity) {
        LightComponent lightComponent = lm.get(entity);
        TransformComponent transformComponent = tm.get(entity);

        switch (lightComponent.type) {
        case PointLight:
            if (transformComponent != null) {
                Light light = new PointLight(rayHandler,
                        Constants.NUM_RAYS,
                        lightComponent.color,
                        lightComponent.intensity,
                        transformComponent.position.x,
                        transformComponent.position.y);
                lights.put(entity, light);
            }
            break;
        case ConeLight:
        case DirectionalLight:
        default:
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (lights.containsKey(entity)) {
            lights.remove(entity)
                    .remove();
        }
    }

    public static Family getFamily() {
        return Family.all(LightComponent.class)
                .get();
    }
}
