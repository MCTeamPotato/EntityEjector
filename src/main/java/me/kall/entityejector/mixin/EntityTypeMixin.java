package me.kall.entityejector.mixin;

import me.kall.entityejector.api.IEntityType;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin implements IEntityType {
    @Unique
    private boolean entityEjector$ejected = false;

    @Override
    public boolean entityEjector$ejected() {
        return this.entityEjector$ejected;
    }

    @Override
    public void entityEjector$setEjected(boolean ejected) {
        this.entityEjector$ejected = ejected;
    }
}
