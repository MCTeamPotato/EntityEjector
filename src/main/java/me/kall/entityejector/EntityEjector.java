package me.kall.entityejector;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import me.kall.entityejector.api.IEntityType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod(EntityEjector.MOD_ID)
public final class EntityEjector {
    public static final String MOD_ID = "entityejector";
    public static final String MOD_NAME = "EntityEjector";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public EntityEjector(@NotNull IEventBus modEventBus, Dist dist, @NotNull ModContainer container) {
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, (EntityJoinLevelEvent event) -> {
            if (((IEntityType)event.getEntity().getType()).entityEjector$ejected()) event.setCanceled(true);
        });
        modEventBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            for (String name : ENTITIES.get()) {
                ((IEntityType) BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(name))).entityEjector$setEjected(true);
            }
        }));
        container.registerConfig(ModConfig.Type.COMMON, CONFIG);
    }

    public static final ModConfigSpec CONFIG;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ENTITIES;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push(MOD_NAME);
        ENTITIES = builder.defineList("EjectedEntities", Lists.newArrayList(), Predicates.alwaysTrue());
        builder.pop();
        CONFIG = builder.build();
    }
}
