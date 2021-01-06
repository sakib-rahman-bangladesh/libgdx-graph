package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.test.episodes.Episode11Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode12Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode13Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode14Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode15Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode16Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode17Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode18Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode19Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode1Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode20Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode21Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode22Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode23Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode2Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode3Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode4Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode5Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode6Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode7Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode8Scene;
import com.gempukku.libgdx.graph.test.episodes.Episode9Scene;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;

public class ReloadableGraphTestApplication extends ApplicationAdapter {
    private LibgdxGraphTestScene[] scenes;
    private int loadedIndex;
    private int width;
    private int height;
    private FPSLogger fpsLogger = new FPSLogger();

    private boolean profile = false;
    private GLProfiler profiler;
    private Skin profileSkin;
    private Stage profileStage;
    private Label profileLabel;

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
                new Episode18Scene(),
                new Episode19Scene(),
                new Episode20Scene(),
                new Episode21Scene(),
                new Episode22Scene(),
                new Episode23Scene(),
                new TestScene()
        };
        loadedIndex = scenes.length - 1;
    }

    @Override
    public void create() {
        //Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);

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
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            if (profile)
                disableProfiler();
            else
                enableProfiler();
        }

        long start = 0;
        if (profile) {
            profiler.reset();
            start = System.nanoTime();
        }

        //if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
        fpsLogger.log();

        scenes[loadedIndex].renderScene();

        if (profile) {
            StringBuilder sb = new StringBuilder();
            float ms = (System.nanoTime() - start) / 1000000f;
            sb.append("Time: " + SimpleNumberFormatter.format(ms) + "ms\n");
            sb.append("Calls: " + profiler.getCalls() + "\n");
            sb.append("Draw calls: " + profiler.getDrawCalls() + "\n");
            sb.append("Shader switches: " + profiler.getShaderSwitches() + "\n");
            sb.append("Texture bindings: " + profiler.getTextureBindings() + "\n");
            sb.append("Vertex calls: " + profiler.getVertexCount().total + "\n");
            profileLabel.setText(sb.toString());

            profileStage.draw();
        }
    }

    @Override
    public void dispose() {
        scenes[loadedIndex].disposeScene();

        if (profile) {
            profileSkin.dispose();
            profileStage.dispose();
        }

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", TextureArray.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
    }

    private void enableProfiler() {
        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();

        profileSkin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));
        profileStage = new Stage();
        profileLabel = new Label("", profileSkin);

        Table tbl = new Table(profileSkin);

        tbl.setFillParent(true);
        tbl.align(Align.topRight);

        tbl.add(profileLabel).pad(10f);
        tbl.row();

        profileStage.addActor(tbl);

        profile = true;
    }

    private void disableProfiler() {
        profileSkin.dispose();
        profileStage.dispose();

        profiler.disable();

        profile = false;
    }
}
