package com.gempukku.libgdx.graph.shader.config.common.math.common;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

import java.util.function.Function;

public class ConditionalShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ConditionalShaderNodeConfiguration() {
        super("Conditional", "Conditional", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("a", "A", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("b", "B", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("true", "True", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("false", "False", true, ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new Function<ObjectMap<String, Array<ShaderFieldType>>, ShaderFieldType>() {
                            @Override
                            public ShaderFieldType apply(ObjectMap<String, Array<ShaderFieldType>> entries) {
                                Array<ShaderFieldType> a = entries.get("a");
                                Array<ShaderFieldType> b = entries.get("b");
                                Array<ShaderFieldType> aTrue = entries.get("true");
                                Array<ShaderFieldType> aFalse = entries.get("false");
                                if (a == null || a.size != 1 || b == null || b.size != 1
                                        || aTrue == null || aTrue.size != 1 || aFalse == null || aFalse.size != 1)
                                    return null;
                                if (a.get(0) != b.get(0) || aTrue.get(0) != aFalse.get(0))
                                    return null;

                                return aTrue.get(0);
                            }
                        },
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
