package com.gempukku.libgdx.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.entity.SensorData;
import com.gempukku.libgdx.graph.sprite.def.PhysicsDef;

public class PhysicsComponent implements Component {
    private PhysicsDef physicsDef;

    private Body body;
    private Array<SensorData> sensorDataArray = new Array<>();

    public PhysicsDef getPhysicsDef() {
        return physicsDef;
    }

    public void setPhysicsDef(PhysicsDef physicsDef) {
        this.physicsDef = physicsDef;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void addSensor(SensorData sensorData) {
        sensorDataArray.add(sensorData);
    }

    public SensorData getSensorDataOfType(String type) {
        for (SensorData sensorDatum : sensorDataArray) {
            if (sensorDatum.getType().equals(type))
                return sensorDatum;
        }
        return null;
    }
}
