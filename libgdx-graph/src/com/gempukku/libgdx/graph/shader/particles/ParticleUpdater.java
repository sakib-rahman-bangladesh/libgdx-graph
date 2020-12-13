package com.gempukku.libgdx.graph.shader.particles;

import com.badlogic.gdx.math.Vector3;

public interface ParticleUpdater {
    void updateParticle(ParticleUpdateInfo particleUpdateInfo);

    class ParticleUpdateInfo {
        public final Vector3 location = new Vector3();
        public float seed;
    }
}
