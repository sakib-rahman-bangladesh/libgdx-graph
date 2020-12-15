package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.test.episodes.Episode11Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode12Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode13Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode14Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode15Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode16Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode17Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode18Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode1Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode2Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode3Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode4Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode5Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode6Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode7Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode8Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode9Scene;

public class ReloadableGraphTestApplication extends ApplicationAdapter {
    private LibgdxGraphTestScene[] scenes;
    private int loadedIndex;
    private int width;
    private int height;

    public ReloadableGraphTestApplication() {
        scenes = new LibgdxGraphTestScene[]{
                new Episode1Scene(),
                new Episode2Scene(),
                new Episode3Scene(),
                new Episode4Scene(),
                new Episode5Scene(),
                new Episode6Scene(),
                new Episode7Scene(),
                new Episode8Scene(),
                new Episode9Scene(),
                new Episode11Scene(),
                new Episode12Scene(),
                new Episode13Scene(),
                new Episode14Scene(),
                new Episode15Scene(),
                new Episode16Scene(),
                new Episode17Scene(),
                new Episode18Scene()
        };
        loadedIndex = scenes.length - 1;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        scenes[loadedIndex].initializeScene();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        scenes[loadedIndex].resizeScene(width, height);
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            scenes[loadedIndex].disposeScene();
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.P) && loadedIndex > 0) {
            scenes[loadedIndex].disposeScene();
            loadedIndex--;
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) && loadedIndex < scenes.length - 1) {
            scenes[loadedIndex].disposeScene();
            loadedIndex++;
            scenes[loadedIndex].initializeScene();
            scenes[loadedIndex].resizeScene(width, height);
        }

        scenes[loadedIndex].renderScene();
    }

    @Override
    public void dispose() {
        scenes[loadedIndex].disposeScene();

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", TextureArray.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
    }
}
