package com.gempukku.libgdx.graph.system.camera.constraint;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PixelPerfectCameraConstraint implements CameraConstraint {
    @Override
    public void applyConstraint(Camera camera, Vector2 focus, float delta) {
        camera.position.x = MathUtils.round(camera.position.x);
        camera.position.y = MathUtils.round(camera.position.y);
        camera.update();
    }
}
