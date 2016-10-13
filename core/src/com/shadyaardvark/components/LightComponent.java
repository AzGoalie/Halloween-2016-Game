package com.shadyaardvark.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class LightComponent implements Component {
    public Color color;

    public int numRays;

    public float intensity;

    public LightType type;

    public enum LightType {
        PointLight, ConeLight, DirectionalLight;
    }
}
