package sad.ami.postalis.networking.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.entities.MagicSealEntity;
import sad.ami.postalis.init.PDataComponentRegistry;
import sad.ami.postalis.items.BewitchedGauntletItem;
import sad.ami.postalis.networking.StreamCodecs;

public record CreateSealEntityPacket(ItemStack stack, Vec3 pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CreateSealEntityPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "spawn_entity"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CreateSealEntityPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, CreateSealEntityPacket::stack,
            StreamCodecs.VEC3_STREAM_CODEC, CreateSealEntityPacket::pos,
            CreateSealEntityPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.player();

            if (!(player.getUseItem().getItem() instanceof BewitchedGauntletItem gauntletItem))
                return;

            var serverLevel = player.getCommandSenderWorld();

            var magicSeal = new MagicSealEntity(player.level());

            magicSeal.setPos(pos);

            serverLevel.addFreshEntity(magicSeal);

            gauntletItem.saveUUIDSeal(stack, magicSeal.getUUID());
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
