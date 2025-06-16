package sad.ami.postalis.api.system.geo.funcial;

import com.mojang.blaze3d.vertex.PoseStack;
import sad.ami.postalis.api.system.geo.manage.GeoModel;

@FunctionalInterface
public interface IBoneFunctional {
    void modifyGlobalRender(PoseStack pose, GeoModel.Bone bone);

}
