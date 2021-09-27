package com.gempukku.libgdx.graph.plugin.models.producer;

public interface ModelDataProducer {
    void fillData(ModelInstanceDataImpl data);

    boolean isEnabled();
}
