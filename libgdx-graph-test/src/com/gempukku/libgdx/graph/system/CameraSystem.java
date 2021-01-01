package com.gempukku.libgdx.graph.system;

import com.badlogic.ashley.core.EntitySystem;
import com.gempukku.libgdx.graph.system.camera.constraint.ConstraintCameraController;

public class CameraSystem extends EntitySystem {
    private ConstraintCameraController constraintCameraController;

    public CameraSystem(int priority) {
        super(priority);
    }

    public void setConstraintCameraController(ConstraintCameraController constraintCameraController) {
        this.constraintCameraController = constraintCameraController;
    }

    @Override
    public void update(float deltaTime) {
        constraintCameraController.update(deltaTime);
    }
}
