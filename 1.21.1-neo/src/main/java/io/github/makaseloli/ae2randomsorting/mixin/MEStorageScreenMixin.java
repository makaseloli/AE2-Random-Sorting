package io.github.makaseloli.ae2randomsorting.mixin;

import appeng.api.config.Settings;
import appeng.api.config.SortOrder;
import appeng.client.gui.me.common.Repo;
import appeng.client.gui.widgets.SettingToggleButton;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "appeng.client.gui.me.common.MEStorageScreen")
public abstract class MEStorageScreenMixin {
    @Shadow(remap = false)
    @Final
    private Repo repo;

    @Unique
    private static boolean ae2randomsorting$rememberRandomSortActive;

    @Unique
    private boolean ae2randomsorting$randomSortActive = ae2randomsorting$rememberRandomSortActive;

    @Inject(method = "toggleServerSetting", remap = false, at = @At("HEAD"), cancellable = true)
    private <SE extends Enum<SE>> void ae2randomsorting$toggleServerSetting(
            SettingToggleButton<SE> button,
            boolean backwards,
            CallbackInfo callbackInfo) {
        if (button.getSetting() != Settings.SORT_BY) {
            return;
        }

        var nextValue = button.getNextValue(backwards);
        if ("RANDOM".equals(nextValue.name())) {
            ae2randomsorting$randomSortActive = true;
            ae2randomsorting$rememberRandomSortActive = true;
            button.set(nextValue);
            repo.updateView();
            callbackInfo.cancel();
            return;
        }

        if (ae2randomsorting$randomSortActive) {
            ae2randomsorting$randomSortActive = false;
            ae2randomsorting$rememberRandomSortActive = false;
        }
    }

    @Inject(method = "getSortBy", remap = false, at = @At("HEAD"), cancellable = true)
    private void ae2randomsorting$getSortBy(CallbackInfoReturnable<SortOrder> callbackInfo) {
        if (ae2randomsorting$randomSortActive) {
            callbackInfo.setReturnValue(SortOrder.valueOf("RANDOM"));
        }
    }
}
