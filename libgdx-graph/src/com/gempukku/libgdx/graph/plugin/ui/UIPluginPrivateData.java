package com.gempukku.libgdx.graph.plugin.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;

public class UIPluginPrivateData implements UIPluginPublicData {
    private ObjectMap<String, Stage> stages = new ObjectMap<>();

    @Override
    public void setStage(String id, Stage stage) {
        stages.put(id, stage);
    }

    @Override
    public Stage getStage(String id) {
        return stages.get(id);
    }
}
