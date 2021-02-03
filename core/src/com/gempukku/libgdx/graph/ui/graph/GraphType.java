package com.gempukku.libgdx.graph.ui.graph;

public abstract class GraphType {
    private String type;
    private boolean exportable;

    protected GraphType(String type, boolean exportable) {
        this.type = type;
        this.exportable = exportable;
    }

    public String getType() {
        return type;
    }

    public boolean isExportable() {
        return exportable;
    }
}
