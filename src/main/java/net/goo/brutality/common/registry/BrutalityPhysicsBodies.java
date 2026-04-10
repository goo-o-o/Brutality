package net.goo.brutality.common.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.physics_bodies.CoinRenderer;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.xmx.velthoric.physics.body.registry.VxBodyRegistry;
import net.xmx.velthoric.physics.body.registry.VxBodyType;

public class BrutalityPhysicsBodies {

    public static final VxBodyType<CoinRigidBody> COIN = VxBodyType.Builder
            .<CoinRigidBody>create(CoinRigidBody::new) // Pass the server-side constructor reference.
            .build(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "coin"));

    public static void register() {
        // Server-side registration
        VxBodyRegistry.getInstance().register(COIN);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerClient() {
        var registry = VxBodyRegistry.getInstance();

        // Client-side factory registration
        registry.registerClientFactory(COIN.getTypeId(), (type, id) -> new CoinRigidBody((VxBodyType<CoinRigidBody>) type, id));

        // Client-side renderer registration
        registry.registerClientRenderer(COIN.getTypeId(), new CoinRenderer());
    }

}
