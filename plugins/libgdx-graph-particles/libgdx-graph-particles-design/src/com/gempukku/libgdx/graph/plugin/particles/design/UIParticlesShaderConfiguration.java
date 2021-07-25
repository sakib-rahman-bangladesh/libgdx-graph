package com.gempukku.libgdx.graph.plugin.particles.design;

import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleLocationShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.config.ParticleUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.EndBillboardParticlesShaderBoxProducer;
import com.gempukku.libgdx.graph.plugin.particles.design.producer.ParticleRandomShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class UIParticlesShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    static {
        register(new EndBillboardParticlesShaderBoxProducer());

        register(new GraphBoxProducerImpl(new ParticleLocationShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ParticleUVShaderNodeConfiguration()));
        register(new ParticleRandomShaderBoxProducer());
        register(new GraphBoxProducerImpl(new ParticleLifetimeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ParticleLifePercentageShaderNodeConfiguration()));
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return Collections.emptyMap();
    }
}
