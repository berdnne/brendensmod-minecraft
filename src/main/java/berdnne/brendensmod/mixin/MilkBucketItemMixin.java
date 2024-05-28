package berdnne.brendensmod.mixin;

import berdnne.brendensmod.fluid.ModFluids;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketItemMixin extends Item implements FluidModificationItem {

    public MilkBucketItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){

        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = BucketItem.raycast(world, user, RaycastContext.FluidHandling.NONE);

        if (blockHitResult.getType() == HitResult.Type.MISS) {
            cir.setReturnValue(ItemUsage.consumeHeldItem(world, user, hand)); // if no block is hit, the player will start drinking the milk
            cir.cancel();
        }
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {

            BlockPos blockPos3;
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);
            if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos2, direction, itemStack)) {
                cir.setReturnValue(ItemUsage.consumeHeldItem(world, user, hand));
                cir.cancel();
            }
            BlockState blockState = world.getBlockState(blockPos);
            BlockPos blockPos4 = blockPos3 = blockPos2;

            if (this.placeFluid(user, world, blockPos3, blockHitResult)) {
                user.setStackInHand(hand, ItemUsage.exchangeStack(itemStack, user, new ItemStack(Items.BUCKET, 1)));
                if (user instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)user, blockPos3, itemStack);
                }
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                cir.setReturnValue(TypedActionResult.success(BucketItem.getEmptiedStack(itemStack, user), world.isClient()));
                cir.cancel();
            }
            cir.setReturnValue(ItemUsage.consumeHeldItem(world, user, hand));
            cir.cancel();
        }

    }

    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {

        FluidFillable fluidFillable;
        boolean bl2;
        Fluid fluid = ModFluids.MILK;
        if (!(fluid instanceof FlowableFluid)) {
            return false;
        }
        FlowableFluid flowableFluid = (FlowableFluid)fluid;
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        boolean bl = blockState.canBucketPlace(ModFluids.MILK);
        boolean bl3 = bl2 = blockState.isAir() || bl || block instanceof FluidFillable && (fluidFillable = (FluidFillable)((Object)block)).canFillWithFluid(player, world, pos, blockState, ModFluids.MILK);
        if (!bl2) {
            return hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), null);
        }
        if (world.getDimension().ultrawarm()) { // also checks for milk to be in the water tag, which it is, so the code was redundant
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.random.nextFloat() - world.random.nextFloat()) * 0.8f);
            for (int l = 0; l < 8; ++l) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0, 0.0, 0.0);
            }
            return true;
        }
        if (!world.isClient && bl && !blockState.isLiquid()) {
            world.breakBlock(pos, true);
        }
        if (world.setBlockState(pos, ModFluids.MILK.getDefaultState().getBlockState(), Block.NOTIFY_ALL_AND_REDRAW) || blockState.getFluidState().isStill()) {
            playEmptyingSound(player, world, pos);
            return true;
        }
        return false;

    }

    private void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
        SoundEvent soundEvent = SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent((Entity)player, GameEvent.FLUID_PLACE, pos);
    }
}