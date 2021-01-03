package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.utils.Json;
import com.gempukku.libgdx.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.entity.EntityDef;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EntityTest {
    public static void main(String[] args) throws IOException {
        EntityDef entityDef = new EntityDef();
        PositionComponent positionComponent = new PositionComponent();
        positionComponent.setPosition(0, 0);

        entityDef.setComponents(positionComponent);

        Json json = new Json();
        json.toJson(entityDef, new FileWriter(new File("/Users/marcin.sciesinski/private/libgdx-graph/libgdx-graph-test/resources/sprite/out.json")));
    }
}
