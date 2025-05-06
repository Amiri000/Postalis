package sad.ami.postalis.api.event;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class RendererItemInHandEvent extends Event implements ICancellableEvent {
    private final ItemRenderer renderer;
    private final LivingEntity entity;
    private ItemStack stack;
    private ItemDisplayContext context;
    private PoseStack poseStack;
    private MultiBufferSource buffer;
    private int light;
}
