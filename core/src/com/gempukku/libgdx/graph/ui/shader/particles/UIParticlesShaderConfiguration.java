package com.gempukku.libgdx.graph.ui.shader.particles;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributePositionBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.model.producer.attribute.AttributeUVBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.particles.producer.EndBillboardParticlesShaderBoxProducer;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UIParticlesShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    public static Set<GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new LinkedHashSet<>();

    static {
        graphBoxProducers.add(new EndBillboardParticlesShaderBoxProducer());

        graphBoxProducers.add(new AttributePositionBoxProducer());
        graphBoxProducers.add(new AttributeUVBoxProducer());
    }

    @Override
    public Set<GraphBoxProducer<ShaderFieldType>> getGraphBoxProducers() {
        return graphBoxProducers;
    }

    @Override
    public Map<String, PropertyBoxProducer<ShaderFieldType>> getPropertyBoxProducers() {
        return Collections.emptyMap();
    }
}
