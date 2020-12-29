package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gempukku.libgdx.graph.GameSystem;
import com.gempukku.libgdx.graph.sprite.Sprite;

public class PhysicsSystem implements GameSystem {
    public static final float PIXELS_TO_METERS = 100f;

    private static final short CHARACTER_GEOMETRY = 0x1;
    private static final short ENVIRONMENT_GEOMETRY = 0x1 << 1;

    private World world;

    public PhysicsSystem(float gravity) {
        world = new World(new Vector2(0, gravity), true);
    }

    public World getWorld() {
        return world;
    }

    public Body createDynamicBody(Sprite sprite, Vector2 colliderAnchor, Vector2 colliderScale) {
        Vector2 position = sprite.getPosition(new Vector2());
        Vector2 size = sprite.getSize(new Vector2());
        Vector2 anchor = sprite.getAnchor(new Vector2());

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        bodyDef.position.set(position).scl(1 / PIXELS_TO_METERS, 1 / PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x * colliderScale.x / 2 / PIXELS_TO_METERS, size.y * colliderScale.y / 2 / PIXELS_TO_METERS, new Vector2(size.x * (anchor.x - colliderAnchor.x) / PIXELS_TO_METERS, size.y * (anchor.y - colliderAnchor.y) / PIXELS_TO_METERS), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.filter.categoryBits = CHARACTER_GEOMETRY;
        fixtureDef.filter.maskBits = ENVIRONMENT_GEOMETRY;
        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    public Body createStaticBody(Sprite sprite, Vector2 colliderAnchor, Vector2 colliderScale) {
        Vector2 position = sprite.getPosition(new Vector2());
        Vector2 size = sprite.getSize(new Vector2());
        Vector2 anchor = sprite.getAnchor(new Vector2());

        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position).scl(1 / PIXELS_TO_METERS, 1 / PIXELS_TO_METERS);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x * colliderScale.x / 2 / PIXELS_TO_METERS, size.y * colliderScale.y / 2 / PIXELS_TO_METERS, new Vector2(size.x * (anchor.x - colliderAnchor.x) / PIXELS_TO_METERS, size.y * (anchor.y - colliderAnchor.y) / PIXELS_TO_METERS), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = ENVIRONMENT_GEOMETRY;
        fixtureDef.filter.maskBits = CHARACTER_GEOMETRY;

        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    @Override
    public void update(float delta) {
        world.step(delta, 6, 2);
    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
