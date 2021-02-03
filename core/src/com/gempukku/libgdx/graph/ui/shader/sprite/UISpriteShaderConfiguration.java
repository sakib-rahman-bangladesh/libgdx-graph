package com.gempukku.libgdx.graph.ui.shader.sprite;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.sprite.SpriteAnchorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.sprite.SpriteLayerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.sprite.SpritePositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.sprite.SpriteSizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.sprite.SpriteUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.sprite.producer.EndSpriteShaderBoxProducer;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UISpriteShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    private static Map<String, GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new TreeMap<>();

    public static void register(GraphBoxProducer<ShaderFieldType> producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndSpriteShaderBoxProducer());

        register(new GraphBoxProducerImpl<ShaderFieldType>(new SpritePositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SpriteLayerShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SpriteSizeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SpriteAnchorShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new SpriteUVShaderNodeConfiguration()));
    }

    @Override
    public Iterable<GraphBoxProducer<ShaderFieldType>> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer<ShaderFieldType>> getPropertyBoxProducers() {
        return Collections.emptyMap();
    }
}
