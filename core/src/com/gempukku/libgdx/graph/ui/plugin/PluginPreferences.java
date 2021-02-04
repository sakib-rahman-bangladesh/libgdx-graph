package com.gempukku.libgdx.graph.ui.plugin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PluginPreferences {
    private static final String PLUGIN_NAME = "libGDX-Graph-Plugin-Name";
    private static final String PLUGIN_VERSION = "libGDX-Graph-Plugin-Version";
    private static final String PLUGIN_CLASS = "libGDX-Graph-Plugin-Class";

    public static void savePlugins(Iterable<String> plugins) {
        FileHandle jarFile = Gdx.files.local(".prefs/com.gempukku.libgdx.graph.plugins.jars");
        try {
            Writer writer = jarFile.writer(false);
            try {
                for (String plugin : plugins) {
                    writer.write(plugin);
                    writer.write('\n');
                }
            } finally {
                writer.close();
            }
        } catch (IOException exp) {
            throw new GdxRuntimeException(exp);
        }
    }

    public static Iterable<String> getPlugins() {
        FileHandle jarFile = Gdx.files.local(".prefs/com.gempukku.libgdx.graph.plugins.jars");
        if (!jarFile.exists())
            return new Array<>();

        try {
            Array<String> result = new Array<>();
            BufferedReader reader = new BufferedReader(jarFile.reader());
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.add(line);
                }
                return result;
            } finally {
                reader.close();
            }
        } catch (IOException exp) {
            throw new GdxRuntimeException(exp);
        }
    }

    public static PluginDefinition getPluginDefinition(FileHandle pluginFile) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(pluginFile.file());
        Manifest manifest = jarFile.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        if (!attributes.containsKey(PLUGIN_NAME) || !attributes.containsKey(PLUGIN_VERSION)
                || !attributes.containsKey(PLUGIN_CLASS)) {
            throw new GdxRuntimeException("Specified JAR does not contain plugin");
        }
        String pluginName = attributes.getValue(PLUGIN_NAME);
        String pluginVersion = attributes.getValue(PLUGIN_VERSION);
        Class<? extends PluginDesignInitializer> pluginClass = (Class<? extends PluginDesignInitializer>) Class.forName(attributes.getValue(PLUGIN_CLASS));
        return new PluginDefinition(
                pluginFile.path(), pluginClass, pluginName, pluginVersion, false, true);
    }
}
