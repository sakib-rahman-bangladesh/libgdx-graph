package com.gempukku.libgdx.graph.system;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.entity.SensorData;
import com.gempukku.libgdx.graph.sprite.Sprite;
import com.gempukku.libgdx.graph.system.sensor.SensorContactListener;

public class PhysicsSystem implements GameSystem {
    public static final float PIXELS_TO_METERS = 100f;

    public static final short CHARACTER_GEOMETRY = 0x1;
    public static final short ENVIRONMENT_GEOMETRY = 0x1 << 1;

    private World world;
    private ObjectMap<String, SensorContactListener> sensorContactListeners = new ObjectMap<>();
    private ObjectMap<String, Short> categoryBits = new ObjectMap<>();

    public PhysicsSystem(float gravity) {
        world = new World(new Vector2(0, gravity), true);
        world.setContactListener(
                new ContactListener() {
                    @Override
                    public void beginContact(Contact contact) {
                        contactBegan(contact);
                    }

                    @Override
                    public void endContact(Contact contact) {
                        contactEnded(contact);
                    }

                    @Override
                    public void preSolve(Contact contact, Manifold oldManifold) {

                    }

                    @Override
                    public void postSolve(Contact contact, ContactImpulse impulse) {

                    }
                }
        );
        categoryBits.put("Character", CHARACTER_GEOMETRY);
        categoryBits.put("Environment", ENVIRONMENT_GEOMETRY);
    }

    public void addSensorContactListener(String type, SensorContactListener sensorContactListener) {
        sensorContactListeners.put(type, sensorContactListener);
    }

    public void removeSensorContactListener(String type) {
        sensorContactListeners.remove(type);
    }

    public World getWorld() {
        return world;
    }

    public Body createDynamicBody(Sprite sprite, Vector2 colliderAnchor, Vector2 colliderScale,
                                  String category, String[] mask) {
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
        fixtureDef.filter.categoryBits = getBits(category);
        fixtureDef.filter.maskBits = getBits(mask);
        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    public Body createStaticBody(Sprite sprite, Vector2 colliderAnchor, Vector2 colliderScale,
                                 String category, String[] mask) {
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
        fixtureDef.filter.categoryBits = getBits(category);
        fixtureDef.filter.maskBits = getBits(mask);

        body.createFixture(fixtureDef);

        shape.dispose();

        return body;
    }

    private short getBits(String... categories) {
        short result = 0;
        if (categories != null) {
            for (String category : categories) {
                result |= categoryBits.get(category);
            }
        }
        return result;
    }

    @Override
    public void update(float delta) {
        world.step(delta, 6, 2);
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    public SensorData createSensor(Sprite sprite, Body body, String type, Vector2 sensorAnchor, Vector2 sensorScale) {
        Vector2 size = sprite.getSize(new Vector2());
        Vector2 anchor = sprite.getAnchor(new Vector2());

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x * sensorScale.x / 2 / PIXELS_TO_METERS, size.y * sensorScale.y / 2 / PIXELS_TO_METERS, new Vector2(size.x * (anchor.x - sensorAnchor.x) / PIXELS_TO_METERS, size.y * (anchor.y - sensorAnchor.y) / PIXELS_TO_METERS), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        Fixture fixture = body.createFixture(fixtureDef);
        SensorData sensorData = new SensorData(type);
        fixture.setUserData(sensorData);

        return sensorData;
    }

    private void contactBegan(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        processContactBegun(fixtureB, fixtureA);
        processContactBegun(fixtureA, fixtureB);
    }

    private void processContactBegun(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureB.getUserData() != null) {
            SensorData sensorData = (SensorData) fixtureB.getUserData();
            String type = sensorData.getType();
            SensorContactListener sensorContactListener = sensorContactListeners.get(type);
            if (sensorContactListener != null) {
                sensorContactListener.contactBegun(sensorData, fixtureA);
            }
        }
    }

    private void contactEnded(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        processContactEnded(fixtureB, fixtureA);
        processContactEnded(fixtureA, fixtureB);
    }

    private void processContactEnded(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureB.getUserData() != null) {
            SensorData sensorData = (SensorData) fixtureB.getUserData();
            String type = sensorData.getType();
            SensorContactListener sensorContactListener = sensorContactListeners.get(type);
            if (sensorContactListener != null) {
                sensorContactListener.contactEnded(sensorData, fixtureA);
            }
        }
    }
}
