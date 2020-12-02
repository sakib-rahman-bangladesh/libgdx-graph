package com.gempukku.libgdx.graph.shader.config;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;

public interface GraphConfiguration {
    Array<GraphShaderPropertyProducer> getPropertyProducers();

    ObjectMap<String, GraphShaderNodeBuilder> getGraphShaderNodeBuilders();
}
