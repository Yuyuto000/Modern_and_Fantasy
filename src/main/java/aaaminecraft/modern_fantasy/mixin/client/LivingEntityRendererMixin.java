package aaaminecraft.modern_fantasy.mixin.client;

import aaaminecraft.modern_fantasy.entity.SofaEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<
        T extends LivingEntity,
        M extends net.minecraft.client.model.EntityModel<T>
        > {

    @Unique
    private static final Logger aaa_modern_and_fantasy$LOGGER = LogUtils.getLogger();

    @Inject(
            method = "setupRotations",
            at = @At("TAIL")
    )
    private void modernfantasy$debugSofaRotations(
            T entity,
            PoseStack poseStack,
            float ageInTicks,
            float bodyYaw,
            float partialTicks,
            CallbackInfo ci
    ) {
        if (!(entity.getVehicle() instanceof SofaEntity sofa)) {
            return;
        }

        aaa_modern_and_fantasy$LOGGER.info(
                "[SOFA RENDERER DEBUG] " +
                        "EntityClass={} | Pose={} | Sleeping={} | " +
                        "BodyYaw={} | EntityYaw={} | " +
                        "SofaYaw={} | HeadDirection={} | FootDirection={}",
                entity.getClass().getName(),
                entity.getPose(),
                entity.isSleeping(),
                bodyYaw,
                entity.getYRot(),
                sofa.getYRot(),
                sofa.getFootDirection().getOpposite(),
                sofa.getFootDirection()
        );
    }
}