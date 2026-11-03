package aaaminecraft.modern_fantasy.mixin.client;

import aaaminecraft.modern_fantasy.entity.SofaEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    /**
     * ソファに乗っている場合だけ、
     * バニラの乗車アニメーションフラグを無効化する。
     */
    @Inject(
            method = "setupAnim",
            at = @At("HEAD")
    )
    private void modernfantasy$disableSofaRidingPose(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (entity.getVehicle() instanceof SofaEntity) {
            ((HumanoidModel<?>) (Object) this).riding = false;
        }
    }
}