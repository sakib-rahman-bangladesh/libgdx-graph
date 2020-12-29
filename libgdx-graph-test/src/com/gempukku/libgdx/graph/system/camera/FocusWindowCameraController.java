package com.gempukku.libgdx.graph.system.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class FocusWindowCameraController implements CameraController {
    private Camera camera;
    private CameraFocus cameraFocus;
    private Rectangle focusRectangle = new Rectangle();
    private Rectangle snapRectangle = new Rectangle();
    private Vector2 snapSpeed = new Vector2();

    private Vector2 tmpVector = new Vector2();

    public FocusWindowCameraController(Camera camera, CameraFocus cameraFocus, Rectangle focusRectangle) {
        this(camera, cameraFocus, focusRectangle, new Rectangle(0.5f, 0.5f, 0, 0), new Vector2(0, 0));
    }

    public FocusWindowCameraController(Camera camera, CameraFocus cameraFocus, Rectangle focusRectangle,
                                       Rectangle snapRectangle, Vector2 snapSpeed) {
        this.camera = camera;
        this.cameraFocus = cameraFocus;
        this.focusRectangle.set(focusRectangle);
        this.snapRectangle.set(snapRectangle);
        this.snapSpeed.set(snapSpeed);
    }

    @Override
    public void update(float delta) {
        Vector2 focus = cameraFocus.getFocus(tmpVector);
        float currentAnchorX = 0.5f + (focus.x - camera.position.x) / camera.viewportWidth;
        float currentAnchorY = 0.5f + (focus.y - camera.position.y) / camera.viewportHeight;
        Vector2 requiredChange = getRequiredChangeToRectangle(focusRectangle, currentAnchorX, currentAnchorY);
        Vector2 snapChange = getRequiredChangeToRectangle(snapRectangle, currentAnchorX, currentAnchorY);
        if (!MathUtils.isEqual(requiredChange.x, 0)) {
            camera.position.x += camera.viewportWidth * requiredChange.x;
        } else if (snapSpeed.x != 0) {
            snapChange.x = Math.signum(snapChange.x) * Math.min(snapSpeed.x * delta, Math.abs(snapChange.x));
            camera.position.x += camera.viewportWidth * snapChange.x;
        }
        if (!MathUtils.isEqual(requiredChange.y, 0)) {
            camera.position.y += camera.viewportHeight * requiredChange.y;
        } else if (snapSpeed.y != 0) {
            snapChange.y = Math.signum(snapChange.y) * Math.min(snapSpeed.y * delta, Math.abs(snapChange.y));
            camera.position.y += camera.viewportHeight * snapChange.y;
        }
        camera.update();
    }

    private Vector2 getRequiredChangeToRectangle(Rectangle rectangle, float desiredAnchorX, float desiredAnchorY) {
        Vector2 requiredChange = tmpVector.set(0, 0);
        if (desiredAnchorX < rectangle.x) {
            requiredChange.x = desiredAnchorX - rectangle.x;
        } else if (desiredAnchorX > rectangle.x + rectangle.width) {
            requiredChange.x = desiredAnchorX - (rectangle.x + rectangle.width);
        }
        if (desiredAnchorY < rectangle.y) {
            requiredChange.y = desiredAnchorY - rectangle.y;
        } else if (desiredAnchorY > rectangle.y + rectangle.height) {
            requiredChange.y = desiredAnchorY - (rectangle.y + rectangle.height);
        }
        return requiredChange;
    }

    @Override
    public void dispose() {

    }
}
