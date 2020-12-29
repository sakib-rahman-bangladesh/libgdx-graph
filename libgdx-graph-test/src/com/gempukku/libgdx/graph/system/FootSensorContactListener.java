package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.graph.entity.SensorData;

public class FootSensorContactListener implements SensorContactListener {
    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        if (other.getFilterData().categoryBits == PhysicsSystem.ENVIRONMENT_GEOMETRY) {
            FootSensorData footSensorData = (FootSensorData) sensor.getValue();
            if (footSensorData == null) {
                footSensorData = new FootSensorData();
                sensor.setValue(footSensorData);
            }
            footSensorData.setGrounded(true);
        }
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        if (other.getFilterData().categoryBits == PhysicsSystem.ENVIRONMENT_GEOMETRY) {
            FootSensorData footSensorData = (FootSensorData) sensor.getValue();
            footSensorData.setGrounded(false);
        }
    }
}
