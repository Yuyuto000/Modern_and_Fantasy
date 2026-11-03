package aaaminecraft.modern_fantasy.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;

public class SofaEntity extends Entity {

    private static final EntityDataAccessor<BlockPos> HEAD_POS = SynchedEntityData.defineId(SofaEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<BlockPos> FOOT_POS = SynchedEntityData.defineId(SofaEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Float> SLEEP_HEIGHT = SynchedEntityData.defineId(SofaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> FOOT_DIRECTION = SynchedEntityData.defineId(SofaEntity.class, EntityDataSerializers.BYTE);

    public SofaEntity(EntityType<? extends SofaEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HEAD_POS, BlockPos.ZERO);
        this.entityData.define(FOOT_POS, BlockPos.ZERO);
        this.entityData.define(SLEEP_HEIGHT, 0.25F);
        this.entityData.define(FOOT_DIRECTION, (byte) Direction.SOUTH.get3DDataValue());
    }

    public void setLayout(BlockPos headPos, BlockPos footPos, double height) {
        this.entityData.set(HEAD_POS, headPos);
        this.entityData.set(FOOT_POS, footPos);
        this.entityData.set(SLEEP_HEIGHT, (float) height);

        Direction direction = Direction.getNearest(footPos.getX() - headPos.getX(), 0, footPos.getZ() - headPos.getZ());

        this.entityData.set(FOOT_DIRECTION, (byte) direction.get3DDataValue());

        double centerX = (headPos.getX() + footPos.getX()) / 2.0D + 0.5D;
        double centerZ = (headPos.getZ() + footPos.getZ()) / 2.0D + 0.5D;

        this.setPos(centerX, headPos.getY(), centerZ);

        // 頭側の方向をプレイヤーの正面にする
        Direction headDirection = direction.getOpposite();
        float yaw = headDirection.toYRot();

        this.setYRot(yaw);
    }

    public BlockPos getHeadPos() {
        return this.entityData.get(HEAD_POS);
    }

    public BlockPos getFootPos() {
        return this.entityData.get(FOOT_POS);
    }

    public Direction getFootDirection() {
        return Direction.from3DDataValue(this.entityData.get(FOOT_DIRECTION));
    }

    public double getSleepHeight() {
        return this.entityData.get(SLEEP_HEIGHT);
    }

    public boolean occupies(BlockPos pos) {
        return this.getHeadPos().equals(pos) || this.getFootPos().equals(pos);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide && !this.isVehicle()) {
            this.discard();
        }
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (!(passenger instanceof LivingEntity living)) return;

        Direction headDirection = getFootDirection().getOpposite();

        double offset = 0.45D;

        double x = this.getX() + headDirection.getStepX() * offset;
        double z = this.getZ() + headDirection.getStepZ() * offset;
        double y = this.getY() + this.getSleepHeight();

        float yaw = headDirection.toYRot();

        // ソファと乗客の向きを統一
        this.setYRot(yaw);
        living.setYRot(yaw);
        living.setYHeadRot(yaw);
        living.setYBodyRot(yaw);

        moveFunction.accept(passenger, x, y, z);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);

        if (passenger instanceof LivingEntity living) {
            // 睡眠位置は設定せず、モデル姿勢だけ変更する。
            living.setPose(Pose.SLEEPING);
        }
    }

    @Override
    public void removePassenger(Entity passenger) {
        super.removePassenger(passenger);

        if (passenger instanceof LivingEntity living) {
            living.setPose(Pose.STANDING);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("HeadPos")) {
            this.entityData.set(HEAD_POS, BlockPos.of(tag.getLong("HeadPos")));
        }

        if (tag.contains("FootPos")) {
            this.entityData.set(FOOT_POS, BlockPos.of(tag.getLong("FootPos")));
        }

        if (tag.contains("SleepHeight")) {
            this.entityData.set(SLEEP_HEIGHT, tag.getFloat("SleepHeight"));
        }

        if (tag.contains("FootDirection")) {
            this.entityData.set(FOOT_DIRECTION, tag.getByte("FootDirection"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putLong("HeadPos", this.getHeadPos().asLong());
        tag.putLong("FootPos", this.getFootPos().asLong());
        tag.putFloat("SleepHeight", this.entityData.get(SLEEP_HEIGHT));
        tag.putByte("FootDirection", this.entityData.get(FOOT_DIRECTION));
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }
}