package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.plugin.particles.config.EndBillboardParticlesShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.part.FloatBoxPart;
import com.gempukku.libgdx.graph.ui.part.IntegerBoxPart;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.kotcrab.vis.ui.util.Validators;

public class EndBillboardParticlesShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public EndBillboardParticlesShaderBoxProducer() {
        super(new EndBillboardParticlesShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final ParticlesShaderPreviewBoxPart previewBoxPart = new ParticlesShaderPreviewBoxPart();
        previewBoxPart.initialize(data);

        GraphBoxImpl<ShaderFieldType> result = new GraphBoxImpl<ShaderFieldType>(id, getConfiguration()) {
            @Override
            public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
                if (event.isData() || event.isStructure()) {
                    previewBoxPart.graphChanged(hasErrors, graph);
                }
            }
        };

        IntegerBoxPart<ShaderFieldType> particleCountBox = new IntegerBoxPart<>("Max particles ", "maxParticles", 100,
                new Validators.GreaterThanValidator(0, false));
        particleCountBox.initialize(data);
        result.addGraphBoxPart(particleCountBox);

        IntegerBoxPart<ShaderFieldType> initialCountBox = new IntegerBoxPart<>("Initial particles ", "initialParticles", 0,
                new Validators.GreaterThanValidator(0, true));
        initialCountBox.initialize(data);
        result.addGraphBoxPart(initialCountBox);

        FloatBoxPart<ShaderFieldType> perSecondCountBox = new FloatBoxPart<>("Particles/second ", "perSecondParticles", 1f,
                new Validators.GreaterThanValidator(0, false));
        perSecondCountBox.initialize(data);
        result.addGraphBoxPart(perSecondCountBox);

        addConfigurationInputsAndOutputs(result);

        SelectBoxPart<ShaderFieldType> blendingBox = new SelectBoxPart<>("Blending", "blending", BasicShader.Blending.values());
        blendingBox.initialize(data);
        result.addGraphBoxPart(blendingBox);

        SelectBoxPart<ShaderFieldType> depthTestBox = new SelectBoxPart<>("DepthTest", "depthTest", BasicShader.DepthTesting.values());
        depthTestBox.initialize(data);
        result.addGraphBoxPart(depthTestBox);

        result.addGraphBoxPart(previewBoxPart);
        return result;
    }
}
