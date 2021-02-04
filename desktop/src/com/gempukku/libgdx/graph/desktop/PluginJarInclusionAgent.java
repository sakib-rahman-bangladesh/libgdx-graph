package com.gempukku.libgdx.graph.desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class PluginJarInclusionAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        appendJars(instrumentation);
    }

    private static void appendJars(Instrumentation instrumentation) {
        System.out.println("Including Plugin JARs...");
        File jarList = new File(".prefs/", "com.gempukku.libgdx.graph.plugins.jars");
        if (jarList.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(jarList));
                try {
                    String jarPath = bufferedReader.readLine();
                    File jarFile = new File(jarPath);
                    if (jarFile.exists()) {
                        System.out.println("Loading JAR: " + jarFile.getName());
                        instrumentation.appendToSystemClassLoaderSearch(new JarFile(jarFile));
                    }
                } finally {
                    bufferedReader.close();
                }
            } catch (IOException exp) {
                throw new RuntimeException(exp);
            }
        }
    }
}
