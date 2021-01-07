package com.gempukku.libgdx.graph.system.camera.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.system.camera.constraint.focus.CameraFocusConstraint;
import com.gempukku.libgdx.graph.system.camera.focus.CameraFocus;

public class ConstraintCameraFocusController {
    private Camera camera;
    private CameraFocus cameraFocus;
    private CameraFocusConstraint[] focusConstraints;
    private CameraConstraint[] constraints;

    private Vector2 tmpVector = new Vector2();

    public ConstraintCameraFocusController(Camera camera, CameraFocus cameraFocus,
                                           CameraFocusConstraint[] focusConstraints,
                                           CameraConstraint... constraints) {
        this.camera = camera;
        this.cameraFocus = cameraFocus;
        this.focusConstraints = focusConstraints;
        this.constraints = constraints;
    }

    public void update(float delta) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        for (CameraFocusConstraint constraint : focusConstraints) {
            constraint.applyConstraint(camera, focus, delta);
        }
        for (CameraConstraint constraint : constraints) {
            constraint.applyConstraint(camera, delta);
        }
    }
}
