package aaaminecraft.modern_fantasy.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class SeatEntity extends Entity {

    public SeatEntity(EntityType<? extends SeatEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void tick() {
        super.tick();

        // 誰も乗っていなければ消す
        if (!this.level().isClientSide && !this.isVehicle()) {
            this.discard();
        }
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        if (passenger instanceof LivingEntity) {
            double y = this.getY() + 0.2D;
            moveFunction.accept(passenger, this.getX(), y, this.getZ());
        }
    }

    @Override
    public void push(Entity entity) {
        // 座席Entity同士などが押し合わないようにする
    }

    @Override
    public void removePassenger(net.minecraft.world.entity.Entity passenger) {
        super.removePassenger(passenger);

        // 降車位置を背もたれの反対側にする
        if (!this.level().isClientSide) {
            Direction facing = this.level()
                    .getBlockState(this.blockPosition())
                    .getValue(HorizontalDirectionalBlock.FACING);

            BlockPos exitPos = this.blockPosition().relative(facing.getOpposite());

            passenger.teleportTo(
                    exitPos.getX() + 0.5D,
                    exitPos.getY(),
                    exitPos.getZ() + 0.5D
            );
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
