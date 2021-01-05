package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.shader.model.ModelGraphShader;
import com.gempukku.libgdx.graph.shader.particles.ParticlesGraphShader;
import com.gempukku.libgdx.graph.shader.screen.ScreenGraphShader;
import com.gempukku.libgdx.graph.shader.sprite.SpriteGraphShader;

public interface PipelineInitializationFeedback {
    void registerScreenShader(String tag, ScreenGraphShader shader);

    void registerParticleEffectShader(String tag, ParticlesGraphShader shader);

    void registerModelShader(String tag, ModelGraphShader shader);

    void registerSpriteShader(String tag, SpriteGraphShader shader);
}
