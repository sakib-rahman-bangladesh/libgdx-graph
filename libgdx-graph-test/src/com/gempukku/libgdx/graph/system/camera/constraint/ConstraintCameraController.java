package com.gempukku.libgdx.graph.system.camera.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.system.GameSystem;
import com.gempukku.libgdx.graph.system.camera.focus.CameraFocus;

public class ConstraintCameraController implements GameSystem {
    private Camera camera;
    private CameraFocus cameraFocus;
    private CameraConstraint[] constraints;

    private Vector2 tmpVector = new Vector2();

    public ConstraintCameraController(Camera camera, CameraFocus cameraFocus,
                                      CameraConstraint... constraints) {
        this.camera = camera;
        this.cameraFocus = cameraFocus;
        this.constraints = constraints;
    }

    @Override
    public void update(float delta) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        for (CameraConstraint constraint : constraints) {
            constraint.applyConstraint(camera, focus, delta);
        }
    }

    @Override
    public void dispose() {
    }
}
