package net.charonus2012.orrery.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class OrrerySand extends FallingBlock {

    public static final MapCodec<OrrerySand> CODEC =
            simpleCodec(OrrerySand::new);

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    public OrrerySand(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
