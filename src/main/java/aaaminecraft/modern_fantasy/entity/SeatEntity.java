package aaaminecraft.modern_fantasy.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

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
        if (passenger instanceof LivingEntity living) {

            double y = this.getY() + 0.4D;

            moveFunction.accept(
                    passenger,
                    this.getX(),
                    y,
                    this.getZ()
            );
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
    }

    @Override
    public void push(Entity entity) {
        // 座席Entity同士などが押し合わないようにする
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
