package com.gempukku.libgdx.graph;

import com.badlogic.gdx.Gdx;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphLoader {
    private static final int VERSION_MAJOR = 0;
    private static final int VERSION_MINOR = 1;
    private static final int VERSION_PATCH = 0;
    public static final String VERSION = VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH;

    public static <T> T loadGraph(InputStream inputStream, GraphLoaderCallback<T> graphLoaderCallback) throws IOException {
        try {
            JSONParser parser = new JSONParser();
            JSONObject graph = (JSONObject) parser.parse(new InputStreamReader(inputStream));
            return loadGraph(graph, graphLoaderCallback);
        } catch (ParseException exp) {
            throw new IOException("Unable to parse graph", exp);
        }
    }

    public static <T> T loadGraph(JSONObject graph, GraphLoaderCallback<T> graphLoaderCallback) {
        String version = (String) graph.get("version");
        if (version == null) {
            // Assuming default
            version = "0.1.0";
        }
        if (!canReadVersion(version)) {
            throw new IllegalArgumentException("Unable to read a graph of version " + version);
        }
        if (!VERSION.equals(version))
            Gdx.app.debug("GraphLoader", "Reading a graph from different version " + VERSION + " != " + version);

        graphLoaderCallback.start();
        for (JSONObject object : (List<JSONObject>) graph.get("nodes")) {
            String type = (String) object.get("type");
            String id = (String) object.get("id");
            float x = ((Number) object.get("x")).floatValue();
            float y = ((Number) object.get("y")).floatValue();
            JSONObject data = (JSONObject) object.get("data");
            graphLoaderCallback.addPipelineNode(id, type, x, y, data);
        }
        for (JSONObject connection : (List<JSONObject>) graph.get("connections")) {
            String fromNode = (String) connection.get("fromNode");
            String fromField = (String) connection.get("fromField");
            String toNode = (String) connection.get("toNode");
            String toField = (String) connection.get("toField");
            graphLoaderCallback.addPipelineVertex(fromNode, fromField, toNode, toField);
        }
        for (JSONObject property : (List<JSONObject>) graph.get("properties")) {
            String type = (String) property.get("type");
            String name = (String) property.get("name");
            JSONObject data = (JSONObject) property.get("data");
            graphLoaderCallback.addPipelineProperty(type, name, data);
        }
        List<JSONObject> groups = (List<JSONObject>) graph.get("groups");
        if (groups != null) {
            for (JSONObject group : groups) {
                String name = (String) group.get("name");
                JSONArray nodes = (JSONArray) group.get("nodes");
                Set<String> nodeIds = new HashSet<>(nodes);
                graphLoaderCallback.addNodeGroup(name, nodeIds);
            }
        }

        return graphLoaderCallback.end();
    }

    private static boolean canReadVersion(String version) {
        String[] split = version.split("\\.");
        if (split.length != 3)
            return false;

        int major = Integer.parseInt(split[0]);
        int minor = Integer.parseInt(split[1]);
        int patch = Integer.parseInt(split[2]);

        // Breaking compatibility on major version
        if (major != VERSION_MAJOR)
            return false;
        // Should support every previous minor version
        if (minor > VERSION_MINOR)
            return false;
        // Should support same minor if previous patch version
        if (minor == VERSION_MINOR && patch > VERSION_PATCH)
            return false;

        return true;
    }
}
