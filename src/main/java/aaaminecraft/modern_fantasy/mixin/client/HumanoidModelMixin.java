package aaaminecraft.modern_fantasy.mixin.client;

import aaaminecraft.modern_fantasy.entity.SofaEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> {

    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * モデルパーツの状態を文字列化する。
     * 回転値はラジアンと度数の両方を出力する。
     */
    private String modernfantasy$partInfo(String name, ModelPart part) {
        return String.format(
                "%s{pos=(%.4f, %.4f, %.4f), rotRad=(%.4f, %.4f, %.4f), rotDeg=(%.2f, %.2f, %.2f), visible=%s}",
                name,
                part.x, part.y, part.z,
                part.xRot, part.yRot, part.zRot,
                Math.toDegrees(part.xRot),
                Math.toDegrees(part.yRot),
                Math.toDegrees(part.zRot),
                part.visible
        );
    }

    /**
     * EntityとSofaEntityの状態を取得する。
     */
    private String modernfantasy$entityInfo(T entity) {
        Entity vehicle = entity.getVehicle();

        String vehicleInfo;

        if (vehicle instanceof SofaEntity sofa) {
            Direction foot = sofa.getFootDirection();
            Direction head = foot.getOpposite();

            vehicleInfo = String.format(
                    "SofaEntity{uuid=%s, pos=(%.4f, %.4f, %.4f), yaw=%.2f, pitch=%.2f, headPos=%s, footPos=%s, footDirection=%s, headDirection=%s, sleepHeight=%.4f}",
                    sofa.getUUID(),
                    sofa.getX(), sofa.getY(), sofa.getZ(),
                    sofa.getYRot(), sofa.getXRot(),
                    sofa.getHeadPos(),
                    sofa.getFootPos(),
                    foot,
                    head,
                    sofa.getSleepHeight()
            );
        } else if (vehicle != null) {
            vehicleInfo = String.format(
                    "OtherVehicle{type=%s, uuid=%s}",
                    vehicle.getType(),
                    vehicle.getUUID()
            );
        } else {
            vehicleInfo = "null";
        }

        return String.format(
                "Player{uuid=%s, tick=%d, pos=(%.4f, %.4f, %.4f), yaw=%.2f, pitch=%.2f, headYaw=%.2f, bodyYaw=%.2f, pose=%s, sleeping=%s, passenger=%s, vehicle=%s, bb=%s}",
                entity.getUUID(),
                entity.tickCount,
                entity.getX(), entity.getY(), entity.getZ(),
                entity.getYRot(), entity.getXRot(),
                entity.getYHeadRot(), entity.yBodyRot,
                entity.getPose(),
                entity.isSleeping(),
                entity.isPassenger(),
                vehicleInfo,
                entity.getBoundingBox()
        );
    }

    /**
     * Mixin実行前の状態を記録する。
     */
    @Inject(method = "setupAnim", at = @At("HEAD"))
    private void modernfantasy$beforeSetupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!(entity.getVehicle() instanceof SofaEntity)) return;

        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;

        // 既存の重要処理を維持
        model.riding = false;

        // ログ量を抑制：20tickに1回
        if (entity.tickCount % 20 != 0) return;

        LOGGER.info(
                """
                ========== SOFA MODEL DEBUG [HEAD] ==========
                Entity:
                {}

                Animation:
                limbSwing={}, limbSwingAmount={}, ageInTicks={}, netHeadYaw={}, headPitch={}

                Model Flags:
                riding={}, crouching={}, young={}

                Model Parts:
                {}
                {}
                {}
                {}
                {}
                {}
                ==============================================
                """,
                modernfantasy$entityInfo(entity),
                limbSwing,
                limbSwingAmount,
                ageInTicks,
                netHeadYaw,
                headPitch,
                model.riding,
                model.crouching,
                model.young,
                modernfantasy$partInfo("HEAD", model.head),
                modernfantasy$partInfo("BODY", model.body),
                modernfantasy$partInfo("RIGHT_ARM", model.rightArm),
                modernfantasy$partInfo("LEFT_ARM", model.leftArm),
                modernfantasy$partInfo("RIGHT_LEG", model.rightLeg),
                modernfantasy$partInfo("LEFT_LEG", model.leftLeg)
        );
    }

    /**
     * バニラのアニメーション処理が終了した後の状態を記録する。
     */
    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void modernfantasy$afterSetupAnim(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!(entity.getVehicle() instanceof SofaEntity)) return;
        if (entity.tickCount % 20 != 0) return;

        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;

        LOGGER.info(
                """
                ========== SOFA MODEL DEBUG [TAIL] ==========
                Entity:
                {}

                Model Flags:
                riding={}, crouching={}, young={}

                Model Parts:
                {}
                {}
                {}
                {}
                {}
                {}
                ==============================================
                """,
                modernfantasy$entityInfo(entity),
                model.riding,
                model.crouching,
                model.young,
                modernfantasy$partInfo("HEAD", model.head),
                modernfantasy$partInfo("BODY", model.body),
                modernfantasy$partInfo("RIGHT_ARM", model.rightArm),
                modernfantasy$partInfo("LEFT_ARM", model.leftArm),
                modernfantasy$partInfo("RIGHT_LEG", model.rightLeg),
                modernfantasy$partInfo("LEFT_LEG", model.leftLeg)
        );

        boolean isPlayerModel = model instanceof PlayerModel<?>;

        LOGGER.info(
                "[SOFA MODEL TYPE] class={}, isPlayerModel={}, riding={}, young={}",
                model.getClass().getName(),
                isPlayerModel,
                model.riding,
                model.young
        );

        LOGGER.info(
                "[SOFA MODEL ROTATION] " +
                        "head=({}, {}, {}) " +
                        "body=({}, {}, {}) " +
                        "rightArm=({}, {}, {}) " +
                        "leftArm=({}, {}, {}) " +
                        "rightLeg=({}, {}, {}) " +
                        "leftLeg=({}, {}, {})",
                model.head.xRot, model.head.yRot, model.head.zRot,
                model.body.xRot, model.body.yRot, model.body.zRot,
                model.rightArm.xRot, model.rightArm.yRot, model.rightArm.zRot,
                model.leftArm.xRot, model.leftArm.yRot, model.leftArm.zRot,
                model.rightLeg.xRot, model.rightLeg.yRot, model.rightLeg.zRot,
                model.leftLeg.xRot, model.leftLeg.yRot, model.leftLeg.zRot
        );
    }
}