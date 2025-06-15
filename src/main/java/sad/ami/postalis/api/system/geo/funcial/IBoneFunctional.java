package sad.ami.postalis.api.system.geo.funcial;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import sad.ami.postalis.api.system.geo.manage.GeoModel;

@FunctionalInterface
public interface IBoneFunctional {
    void boneHandler(PoseStack pose, GeoModel.Bone bone);

}
