package me.kall.entityejector;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import me.kall.entityejector.api.IEntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod(EntityEjector.MOD_ID)
public final class EntityEjector {
    public static final String MOD_ID = "entityejector";
    public static final String MOD_NAME = "EntityEjector";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public EntityEjector(@NotNull FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, (EntityJoinLevelEvent event) -> {
            if (((IEntityType)event.getEntity().getType()).entityEjector$ejected()) event.setCanceled(true);
        });
        modBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            for (String name : ENTITIES.get()) {
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(name));
                if (entityType != null) {
                    ((IEntityType)entityType).entityEjector$setEjected(true);
                } else {
                    LOGGER.error("Invalid entry: {}", name);
                }
            }
        }));
        context.registerConfig(ModConfig.Type.COMMON, CONFIG);
    }

    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ENTITIES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push(MOD_NAME);
        ENTITIES = builder.defineList("EjectedEntities", Lists.newArrayList(), Predicates.alwaysTrue());
        builder.pop();
        CONFIG = builder.build();
    }
}
