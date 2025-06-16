package sad.ami.postalis.api.system.geo.samples;

import lombok.Builder;
import lombok.Data;
import net.minecraft.world.item.ItemDisplayContext;
import sad.ami.postalis.api.system.geo.funcial.IBoneFunctional;

@Data
@Builder(builderMethodName = "toBuild")
public class GeoItemRendererBuilder {
    public static final GeoItemRendererBuilder INSTANCE = GeoItemRendererBuilder.toBuild().build();

    private ItemDisplayContext itemDisplayContext;
    private IBoneFunctional modifyGlobalRender;
    private boolean hasAnimations;
}
