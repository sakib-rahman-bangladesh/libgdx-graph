package com.gempukku.libgdx.graph.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class FocusWindowCameraController implements CameraController {
    private Camera camera;
    private CameraFocus cameraFocus;
    private Vector2 min = new Vector2();
    private Vector2 max = new Vector2();

    private Vector2 tmpVector = new Vector2();

    public FocusWindowCameraController(Camera camera, CameraFocus cameraFocus, Vector2 min, Vector2 max) {
        this.camera = camera;
        this.cameraFocus = cameraFocus;
        this.min.set(min);
        this.max.set(max);
    }

    @Override
    public void update(float delta) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        float desiredAnchorX = 0.5f + (focus.x - camera.position.x) / camera.viewportWidth;
        float desiredAnchorY = 0.5f + (focus.y - camera.position.y) / camera.viewportHeight;
        float xChange = 0;
        float yChange = 0;
        if (desiredAnchorX < min.x) {
            xChange = desiredAnchorX - min.x;
        } else if (desiredAnchorX > max.x) {
            xChange = desiredAnchorX - max.x;
        }
        if (desiredAnchorY < min.y) {
            yChange = desiredAnchorY - min.y;
        } else if (desiredAnchorY > max.y) {
            yChange = desiredAnchorY - max.y;
        }
        if (xChange != 0 || yChange != 0) {
            camera.position.x += camera.viewportWidth * xChange;
            camera.position.y += camera.viewportHeight * yChange;
            camera.update();
        }
    }
}
