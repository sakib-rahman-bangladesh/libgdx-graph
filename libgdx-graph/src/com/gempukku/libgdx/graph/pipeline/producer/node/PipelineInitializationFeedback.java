package com.gempukku.libgdx.graph.pipeline.producer.node;

import com.gempukku.libgdx.graph.pipeline.impl.BufferCopyHelper;
import com.gempukku.libgdx.graph.pipeline.impl.TextureFrameBufferCache;

public interface PipelineInitializationFeedback {
    <T> T getPrivatePluginData(Class<T> clazz);

    TextureFrameBufferCache getTextureBufferCache();

    BufferCopyHelper getBufferCopyHelper();
}
