package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class EmptyTabWidget extends Table {
    public EmptyTabWidget(Skin skin) {
        super(skin);
        Label label = new Label("Credits:\nCurveWidget and GradientWidget credits go to authors of Talos project:\nhttps://github.com/rockbite/talos", skin);
        add(label);
    }
}
