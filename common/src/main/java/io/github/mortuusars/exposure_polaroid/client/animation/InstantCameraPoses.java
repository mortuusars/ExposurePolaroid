package io.github.mortuusars.exposure_polaroid.client.animation;

import io.github.mortuusars.exposure.client.animation.CameraPoses;
import io.github.mortuusars.exposure.client.animation.EasingFunction;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class InstantCameraPoses extends CameraPoses {
    @Override
    public void applyHolding(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) {
        boolean mirror = arm == HumanoidArm.LEFT;

        ModelPart cameraArm = mirror ? model.leftArm : model.rightArm;
        cameraArm.xRot = Mth.clamp(model.head.xRot - 1.95F, -3.3f, -0.35f);
        cameraArm.yRot = model.head.yRot - ((float) (Math.PI / 12) * (mirror ? -1 : 1));
        float xVal = Mth.map(cameraArm.xRot, -3.3f, -0.35f, -0.3f, 0.3f);
        if (!mirror) {
            xVal *= -1;
        }
        cameraArm.zRot += xVal;

        ModelPart supportingArm = mirror ? model.rightArm : model.leftArm;
        supportingArm.xRot = Mth.clamp(model.head.xRot - 1.6F, -2.95f, 0);
        supportingArm.yRot = model.head.yRot - ((float) (Math.PI / 6) * (!mirror ? -1 : 1));
        float supXVal = Mth.map(cameraArm.xRot, -3.3f, -0.35f, -0.3f, 0.3f);
        if (mirror) {
            supXVal *= -1;
        }
        supportingArm.zRot += supXVal;

        float actionProgress = getCameraActionAnim(entity);
        actionProgress = (float) EasingFunction.EASE_OUT_CUBIC.ease(actionProgress);
        float actionAnim =  actionProgress > 0.3F ? (1F - actionProgress) : actionProgress;

        supportingArm.xRot += (actionAnim * 0.1F) * (mirror ? -1 : 1);
        supportingArm.yRot += (actionAnim * 0.1F) * (mirror ? -1 : 1);
    }
}
