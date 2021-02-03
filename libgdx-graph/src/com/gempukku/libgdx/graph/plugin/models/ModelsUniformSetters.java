package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;

public class ModelsUniformSetters {
    public final static UniformRegistry.UniformSetter projViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getModelInstanceData().getWorldTransform()).mul(shaderContext.getCamera().combined));
        }
    };

    public final static UniformRegistry.UniformSetter normalProjViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getModelInstanceData().getWorldTransform()).mul(shaderContext.getCamera().combined).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter viewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getModelInstanceData().getWorldTransform()).mul(shaderContext.getCamera().view));
        }
    };

    public final static UniformRegistry.UniformSetter normalViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getModelInstanceData().getWorldTransform()).mul(shaderContext.getCamera().view).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter worldTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, ((ModelShaderContext) shaderContext).getModelInstanceData().getWorldTransform());
        }
    };

    public final static UniformRegistry.UniformSetter normalWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmpM = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmpM.set(((ModelShaderContext) shaderContext).getModelInstanceData().getWorldTransform()).toNormalMatrix());
        }
    };

    public static class Bones implements UniformRegistry.UniformSetter {
        private final static Matrix4 idtMatrix = new Matrix4();
        public final float bones[];

        public Bones(final int numBones) {
            this.bones = new float[numBones * 16];
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Matrix4[] modelBones = ((ModelShaderContext) shaderContext).getModelInstanceData().getBones();
            for (int i = 0; i < bones.length; i += 16) {
                final int idx = i / 16;
                if (modelBones == null || idx >= modelBones.length || modelBones[idx] == null)
                    System.arraycopy(idtMatrix.val, 0, bones, i, 16);
                else
                    System.arraycopy(modelBones[idx].val, 0, bones, i, 16);
            }
            shader.setUniformMatrixArray(location, bones);
        }
    }

    public static class MaterialFloat implements UniformRegistry.UniformSetter {
        private long type;
        private float defaultValue;

        public MaterialFloat(long type, float defaultValue) {
            this.type = type;
            this.defaultValue = defaultValue;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Float value = ((ModelShaderContext) shaderContext).getModelInstanceData().getMaterialFloatData(type);
            if (value == null)
                value = defaultValue;
            shader.setUniform(location, value);
        }
    }

    public static class MaterialColor implements UniformRegistry.UniformSetter {
        private long type;
        private Color defaultColor;

        public MaterialColor(long type, Color defaultColor) {
            this.type = type;
            this.defaultColor = defaultColor;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Color color = ((ModelShaderContext) shaderContext).getModelInstanceData().getMaterialColorData(type);
            if (color == null)
                color = defaultColor;
            shader.setUniform(location, color);
        }
    }

    public static class MaterialTexture implements UniformRegistry.UniformSetter {
        private long type;
        private TextureDescriptor<Texture> temp = new TextureDescriptor<>();

        public MaterialTexture(long type) {
            this.type = type;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            TextureDescriptor<Texture> textureDescriptor = ((ModelShaderContext) shaderContext).getModelInstanceData().getMaterialTextureData(type);
            if (textureDescriptor == null) {
                textureDescriptor = temp;
                textureDescriptor.texture = shader.getDefaultTexture();
            }
            final int unit = shader.getContext().textureBinder.bind(textureDescriptor);
            shader.setUniform(location, unit);
        }
    }

    public static class MaterialTextureUV implements UniformRegistry.UniformSetter {
        private long type;
        private Vector2 uvTemp = new Vector2(0, 0);
        private Vector2 uvScaleTemp = new Vector2(1, 1);

        public MaterialTextureUV(long type) {
            this.type = type;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Vector2 uv = ((ModelShaderContext) shaderContext).getModelInstanceData().getMaterialUVData(type);
            Vector2 uvScale = ((ModelShaderContext) shaderContext).getModelInstanceData().getMaterialUVScaleData(type);
            if (uv == null)
                uv = uvTemp;
            if (uvScale == null)
                uvScale = uvScaleTemp;

            shader.setUniform(location, uv.x, uv.y, uvScale.x, uvScale.y);
        }
    }
}
