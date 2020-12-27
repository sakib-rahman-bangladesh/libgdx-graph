package com.gempukku.libgdx.graph.ui.shader.sprite;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.sprite.SpriteLayerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.sprite.SpriteUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.sprite.producer.EndSpriteShaderBoxProducer;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UISpriteShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    public static Set<GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new LinkedHashSet<>();

    static {
        graphBoxProducers.add(new EndSpriteShaderBoxProducer());

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SpriteUVShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SpriteLayerShaderNodeConfiguration()));
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
