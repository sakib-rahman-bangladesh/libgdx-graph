package com.gempukku.libgdx.graph.plugin.models;

/**
 * Main interface that is used to operate on models rendered by GraphShaders. Any operation done on the models should
 * be done via use of this interface.
 */
public interface GraphModels {
    GraphModel addModel(String tag, RenderableModel model);

    void removeModel(GraphModel model);

    void setGlobalProperty(String tag, String name, Object value);

    void unsetGlobalProperty(String tag, String name);

    Object getGlobalProperty(String tag, String name);
}
