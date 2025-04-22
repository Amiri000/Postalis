package sad.ami.postalis.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sad.ami.postalis.client.screen.ChecklistAbilityScreen;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected void setPosition(Vec3 position) {
    }

    @Shadow
    protected abstract void setRotation(float yRot, float xRot, float roll);

    @Inject(method = "setup", at = @At("RETURN"), cancellable = true)
    private void setupCustomCamera(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (!(entity instanceof LocalPlayer player) || !ChecklistAbilityScreen.isChecklistScreen())
            return;

        Vec3 eyePos = player.getEyePosition(partialTick);

        Vec3 forward = player.getLookAngle().normalize();

        Vec3 newCameraPos = eyePos.add(forward.scale(2));

        setPosition(newCameraPos);

        Vec3 toPlayer = eyePos.subtract(newCameraPos).normalize();

        float xRot = (float) (-Math.toDegrees(Math.asin(toPlayer.y)));
        float yRot = (float) (Math.toDegrees(Math.atan2(toPlayer.z, toPlayer.x))) - 90f;

        setRotation(yRot, xRot, 0f);

        ci.cancel();
    }
}
