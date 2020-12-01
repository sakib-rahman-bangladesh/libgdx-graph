package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;

public class ShaderContextImpl implements ShaderContext {
    private int renderWidth;
    private int renderHeight;

    private Camera camera;
    private GraphShaderEnvironment graphShaderEnvironment;
    private Texture depthTexture;
    private Texture colorTexture;
    private TimeProvider timeProvider;
    private PropertyContainer propertyContainer;

    @Override
    public int getRenderWidth() {
        return renderWidth;
    }

    public void setRenderWidth(int renderWidth) {
        this.renderWidth = renderWidth;
    }

    @Override
    public int getRenderHeight() {
        return renderHeight;
    }

    public void setRenderHeight(int renderHeight) {
        this.renderHeight = renderHeight;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public GraphShaderEnvironment getGraphShaderEnvironment() {
        return graphShaderEnvironment;
    }

    public void setGraphShaderEnvironment(GraphShaderEnvironment graphShaderEnvironment) {
        this.graphShaderEnvironment = graphShaderEnvironment;
    }

    @Override
    public Texture getDepthTexture() {
        return depthTexture;
    }

    public void setDepthTexture(Texture depthTexture) {
        this.depthTexture = depthTexture;
    }

    @Override
    public Texture getColorTexture() {
        return colorTexture;
    }

    public void setColorTexture(Texture colorTexture) {
        this.colorTexture = colorTexture;
    }

    @Override
    public TimeProvider getTimeProvider() {
        return timeProvider;
    }

    public void setTimeProvider(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void setPropertyContainer(PropertyContainer propertyContainer) {
        this.propertyContainer = propertyContainer;
    }

    @Override
    public Object getProperty(String name) {
        return propertyContainer.getValue(name);
    }
}
