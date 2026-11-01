package aaaminecraft.modern_fantasy.block.customs;

import aaaminecraft.modern_fantasy.entity.ModEntities;
import aaaminecraft.modern_fantasy.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModernChairBlock extends HorizontalDirectionalBlock {
    // コンストラクタ
    public ModernChairBlock(Properties properties) {
        super(properties);
    }

    // BlockState系をいじいじするためのインジェクションメソッド
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    /*
     * 当たり判定をカスタマイズするstatic変数
     * NORTH向きをデフォルトとして記述する
     * EAST/SOUTH/WESTは下のrotateShape()で自動生成する
     */
    private static final VoxelShape SHAPE_NORTH = Shapes.or(

            // 脚
            Block.box(0, 0, 14, 2, 12, 16),
            Block.box(14, 0, 14, 16, 12, 16),
            Block.box(0, 0, 0, 2, 12, 2),
            Block.box(14, 0, 0, 16, 12, 2),

            // 座面周辺フレーム
            Block.box(2, 10, 1, 14, 11, 3),   // 前側
            Block.box(2, 11, 14, 14, 13, 16), // 後ろ側
            Block.box(0, 10, 3, 2, 12, 14),   // 左側
            Block.box(14, 10, 3, 16, 12, 14), // 右側

            // 座面
            Block.box(2, 11, 1, 14, 12.5, 14),

            // 背もたれ：北側（Z=14～16）
            Block.box(0, 12, 14, 2, 24, 16),
            Block.box(14, 12, 14, 16, 24, 16),
            Block.box(0, 24, 14, 16, 26, 16),
            Block.box(2, 18, 14, 14, 22, 16)
    );

    // 4方向の形状
    private static final VoxelShape SHAPE_EAST = rotateShape(SHAPE_NORTH, Direction.EAST);
    private static final VoxelShape SHAPE_SOUTH = rotateShape(SHAPE_NORTH, Direction.SOUTH);
    private static final VoxelShape SHAPE_WEST = rotateShape(SHAPE_NORTH, Direction.WEST);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
        return switch (state.getValue(FACING)) {
            case EAST  -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST  -> SHAPE_WEST;
            case NORTH, UP,DOWN -> SHAPE_NORTH;
        };
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            // この椅子に座っているEntityを取得
            for (SeatEntity seat : level.getEntitiesOfClass(
                    SeatEntity.class,
                    new AABB(pos)
            )) {
                // Entityを削除すると騎乗中のプレイヤーも降りる
                seat.discard();
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    /*
     * 回転関数
     *
     * AABBを一個ずつ取り出してX/Zを回転させて新しいVoxelShapeを生成するというもの。
     * AABB？俺は知らないね！ggrk((((((((殴
     */
    private static VoxelShape rotateShape(VoxelShape shape, Direction rotation) {
        VoxelShape result = Shapes.empty();

        for (AABB box : shape.toAabbs()) {
            double minX = box.minX;
            double minY = box.minY;
            double minZ = box.minZ;
            double maxX = box.maxX;
            double maxY = box.maxY;
            double maxZ = box.maxZ;

            AABB rotated;

            switch (rotation) {
                case EAST -> rotated = new AABB(
                        1.0 - maxZ, minY, minX,
                        1.0 - minZ, maxY, maxX
                );

                case SOUTH -> rotated = new AABB(
                        1.0 - maxX, minY, 1.0 - maxZ,
                        1.0 - minX, maxY, 1.0 - minZ
                );

                case WEST -> rotated = new AABB(
                        minZ, minY, 1.0 - maxX,
                        maxZ, maxY, 1.0 - minX
                );

                default -> rotated = box;
            }

            result = Shapes.or(result, Shapes.create(rotated));
        }

        return result;
    }

    private static VoxelShape boxToShape(AABB box) {
        return Shapes.box(box.minX,
                box.minY,
                box.minZ,
                box.maxX,
                box.maxY,
                box.maxZ);
    }

    // インタラクション処理
    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // 誰かー座ってるかー
        if (!level.getEntitiesOfClass(
                SeatEntity.class,
                new AABB(pos)
        ).isEmpty()) {
            return InteractionResult.PASS;
        }

        // 座面Entity、仕事だ、出ろ
        SeatEntity seat = new SeatEntity(
                ModEntities.SEAT.get(),
                level
        );

        //座る位置
        seat.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
        level.addFreshEntity(seat);
        player.startRiding(seat);
        return InteractionResult.CONSUME;
    }
}
