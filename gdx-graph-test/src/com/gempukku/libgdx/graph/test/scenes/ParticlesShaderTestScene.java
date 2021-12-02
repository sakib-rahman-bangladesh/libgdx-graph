package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.particles.GraphParticleEffects;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.particles.CommonPropertiesParticleEffectAdapter;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.SpherePositionGenerator;

import java.io.IOException;
import java.io.InputStream;

public class ParticlesShaderTestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private Camera camera;

    @Override
    public void initializeScene() {
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100;
        camera.position.set(0, 0, 3);
        camera.up.set(0, 1, 0);
        camera.lookAt(0, 0, 0);
        camera.update();

        pipelineRenderer = loadPipelineRenderer();

        GraphParticleEffects particleEffects = pipelineRenderer.getPluginData(GraphParticleEffects.class);
        particleEffects.setGlobalProperty("Test", "Color", Color.RED);

        createEffect(particleEffects, new Vector3(0, 0, 0), 0.1f);
        createEffect(particleEffects, new Vector3(2, 0, 0), 0.2f);
    }

    private void createEffect(GraphParticleEffects particleEffects, Vector3 center, float size) {
        SpherePositionGenerator positionGenerator = new SpherePositionGenerator();
        positionGenerator.getCenter().set(center);
        positionGenerator.setRadius(0.3f);
        DefaultParticleGenerator particleGenerator = new DefaultParticleGenerator(timeKeeper, 1f, 0, 10);
        particleGenerator.setPositionGenerator(positionGenerator);

        MapWritablePropertyContainer properties = new MapWritablePropertyContainer();
        properties.setValue("Size", size);

        CommonPropertiesParticleEffectAdapter particleEffectAdapter = new CommonPropertiesParticleEffectAdapter(particleEffects, Vector3.Zero, null, properties);
        particleEffectAdapter.addTag("Test", particleGenerator);
        particleEffectAdapter.startEffect("Test");
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void disposeScene() {
        pipelineRenderer.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("test/particles-shader-test.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new PipelineLoaderCallback(timeKeeper));
                setupPipeline(pipelineRenderer);
                return pipelineRenderer;
            } finally {
                stream.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Unable to load pipeline", exp);
        }
    }

    private void setupPipeline(PipelineRenderer pipelineRenderer) {
        pipelineRenderer.setPipelineProperty("Camera", camera);
    }
}