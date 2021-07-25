package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;


public interface GraphBoxPart<T extends FieldType> extends PropertyBoxPart<T>, Disposable {
    GraphBoxOutputConnector<T> getOutputConnector();

    GraphBoxInputConnector<T> getInputConnector();
}
