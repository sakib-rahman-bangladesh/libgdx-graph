package com.gempukku.libgdx.graph.ui.shader.common.producer.math.common;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.common.math.common.ConditionalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class ConditionalShaderBoxProducer extends GraphBoxProducerImpl<ShaderFieldType> {
    public ConditionalShaderBoxProducer() {
        super(new ConditionalShaderNodeConfiguration());
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl<ShaderFieldType> result = createGraphBox(skin, id);
        addConfigurationInputsAndOutputs(skin, result);
        SelectBoxPart<ShaderFieldType> operationType = new SelectBoxPart<ShaderFieldType>(skin, "Operation", "operation",
                ">", ">=", "==", "<=", "<", "!=");
        operationType.initialize(data);
        result.addGraphBoxPart(operationType);
        SelectBoxPart<ShaderFieldType> aggregationType = new SelectBoxPart<ShaderFieldType>(skin, "Aggregate", "aggregate",
                "any", "all");
        aggregationType.initialize(data);
        result.addGraphBoxPart(aggregationType);

        return result;
    }
}
