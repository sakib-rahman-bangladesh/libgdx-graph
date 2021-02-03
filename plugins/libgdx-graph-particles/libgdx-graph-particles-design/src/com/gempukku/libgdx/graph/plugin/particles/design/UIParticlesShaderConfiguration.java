package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLocationShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.EndBillboardParticlesShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.ParticleRandomShaderBoxProducer;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIParticlesShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    private static Map<String, GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new TreeMap();

    public static void register(GraphBoxProducer<ShaderFieldType> producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndBillboardParticlesShaderBoxProducer());

        register(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleLocationShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleUVShaderNodeConfiguration()));
        register(new ParticleRandomShaderBoxProducer());
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleLifetimeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleLifePercentageShaderNodeConfiguration()));
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
