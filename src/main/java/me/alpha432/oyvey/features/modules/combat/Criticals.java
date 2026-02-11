// src/main/java/com/grok/hitboxexpander/mixin/EntityMixin.java
package com.grok.hitboxexpander.mixin;

import com.grok.hitboxexpander.HitboxExpander;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
    private void modifyDimensions(EntityPose pose, CallbackInfoReturnable<Vec3d> cir) {
        if (!HitboxExpander.enabled) return;

        Entity self = (Entity) (Object) this;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (self instanceof LivingEntity && self != mc.player && mc.world != null && mc.world.getEntityById(self.getId()) == self) {
            Vec3d original = cir.getReturnValue();
            double expand = HitboxExpander.expandAmount;
            // Expand width/depth (x/z), keep height (y)
            cir.setReturnValue(new Vec3d(
                original.x + expand * 2,
                original.y,
                original.z + expand * 2
            ));
        }
    }
}
