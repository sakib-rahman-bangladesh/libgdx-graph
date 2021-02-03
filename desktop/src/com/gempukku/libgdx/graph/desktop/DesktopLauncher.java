package com.gempukku.libgdx.graph.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gempukku.libgdx.graph.plugin.particles.design.ParticlesPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.sprites.design.SpritesPluginDesignInitializer;
import com.gempukku.libgdx.graph.plugin.ui.design.UIPluginDesignInitializer;
import com.gempukku.libgdx.graph.ui.LibgdxGraphApplication;
import com.gempukku.libgdx.graph.ui.plugin.PluginDesignRegistry;

public class DesktopLauncher {
    public static void main(String[] arg) {
        PluginDesignRegistry.register(UIPluginDesignInitializer.class);
        PluginDesignRegistry.register(ParticlesPluginDesignInitializer.class);
        PluginDesignRegistry.register(SpritesPluginDesignInitializer.class);

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1440;
        config.height = 810;
        new LwjglApplication(new LibgdxGraphApplication(), config);
    }
}
