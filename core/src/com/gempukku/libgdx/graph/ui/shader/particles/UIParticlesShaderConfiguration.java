package com.gempukku.libgdx.graph.ui.shader.particles;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleLocationShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleSeedShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.particles.ParticleUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.particles.producer.EndBillboardParticlesShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.particles.producer.ParticleRandomShaderBoxProducer;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UIParticlesShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    public static Set<GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new LinkedHashSet<>();

    static {
        graphBoxProducers.add(new EndBillboardParticlesShaderBoxProducer());

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleLocationShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleUVShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleSeedShaderNodeConfiguration()));
        graphBoxProducers.add(new ParticleRandomShaderBoxProducer());
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleLifetimeShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ParticleLifePercentageShaderNodeConfiguration()));

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
