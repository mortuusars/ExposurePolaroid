package io.github.mortuusars.exposure_polaroid.client.animation;

import io.github.mortuusars.exposure.client.animation.CameraPoses;
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
        cameraArm.xRot = Mth.clamp(model.head.xRot - 2F, -3.3f, -0.35f);
        cameraArm.yRot = model.head.yRot;
        float xVal = Mth.map(cameraArm.xRot, -3.3f, -0.35f, -0.3f, 0.3f);
        if (!mirror) {
            xVal *= -1;
        }
        cameraArm.zRot += xVal;

        ModelPart supportingArm = mirror ? model.rightArm : model.leftArm;
        supportingArm.xRot = Mth.clamp(model.head.xRot - 1.65F, -2.95f, 0);
        supportingArm.yRot = model.head.yRot - 0.4f * (!mirror ? -1 : 1);
        float supXVal = Mth.map(cameraArm.xRot, -3.3f, -0.35f, -0.3f, 0.3f);
        if (mirror) {
            supXVal *= -1;
        }
        supportingArm.zRot += supXVal;

        float actionAnim = getCameraActionAnim(entity);

        supportingArm.xRot += (actionAnim * 0.1F) * (mirror ? -1 : 1);
        supportingArm.yRot += (actionAnim * 0.1F) * (mirror ? -1 : 1);
    }
}
