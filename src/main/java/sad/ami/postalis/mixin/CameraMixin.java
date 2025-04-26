package sad.ami.postalis.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;
import sad.ami.postalis.utils.PlayerUtils;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    protected abstract void setPosition(Vec3 pos);

    @Shadow
    protected abstract void setRotation(float yRot, float xRot, float roll);

    @Unique
    private Vec3 currentPos = null;

    @Unique
    private float currentXRot = 0f;

    @Unique
    private float currentYRot = 0f;

    @Inject(method = "setup", at = @At("RETURN"), cancellable = true)
    private void setupCustomCamera(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!(entity instanceof LocalPlayer player) || !PlayerUtils.isChecklistScreen())
            return;

        double ix = player.xo + (player.getX() - player.xo) * partialTick;
        double iy = player.yo + (player.getY() - player.yo) * partialTick;
        double iz = player.zo + (player.getZ() - player.zo) * partialTick;

        float bodyYaw = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);

        var center = new Vec3(ix, iy + player.getBbHeight() * 0.9, iz);

        var forward = new Vec3(Math.sin(Math.toRadians(bodyYaw)), 0.5F, -Math.cos(Math.toRadians(bodyYaw)));
        var left = forward.cross(new Vec3(0, 1, 0)).normalize();

        var targetPos = center.add(forward.normalize().scale(-2F)).add(left.scale(0.5));

        currentPos = currentPos == null ? targetPos : currentPos.lerp(targetPos, 0.1);

        setPosition(targetPos);

        var toRightHand = center.add(left.scale(-0.5)).subtract(targetPos).normalize();

        currentXRot = postalis$lerpAngle(currentXRot, (float) (-Math.toDegrees(Math.asin(toRightHand.y))));
        currentYRot = postalis$lerpAngle(currentYRot, (float) (Math.toDegrees(Math.atan2(toRightHand.z, toRightHand.x)) - 100));

        setRotation(currentYRot, currentXRot, 0f);

        ci.cancel();

    }

    @Unique
    private float postalis$lerpAngle(float start, float end) {
        float delta = ((end - start + 180 + 360) % 360) - 180;

        return start + delta * (float) 0.1;
    }
}
