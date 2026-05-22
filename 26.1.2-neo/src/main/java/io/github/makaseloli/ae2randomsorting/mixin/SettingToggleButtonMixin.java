package io.github.makaseloli.ae2randomsorting.mixin;

import java.util.List;

import appeng.api.config.Settings;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.util.Icon;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingToggleButton.class)
public abstract class SettingToggleButtonMixin<T extends Enum<T>> {
    @Shadow(remap = false)
    public abstract appeng.api.config.Setting<T> getSetting();

    @Shadow(remap = false)
    public abstract T getCurrentValue();

    @Inject(method = "getIcon", remap = false, at = @At("HEAD"), cancellable = true)
    private void ae2randomsorting$getIcon(CallbackInfoReturnable<Icon> callbackInfo) {
        if (getSetting() == Settings.SORT_BY && "RANDOM".equals(getCurrentValue().name())) {
            callbackInfo.setReturnValue(Icon.SCHEDULING_RANDOM);
        }
    }

    @Inject(method = "getTooltipMessage", remap = false, at = @At("HEAD"), cancellable = true)
    private void ae2randomsorting$getTooltipMessage(CallbackInfoReturnable<List<Component>> callbackInfo) {
        if (getSetting() == Settings.SORT_BY && "RANDOM".equals(getCurrentValue().name())) {
            callbackInfo.setReturnValue(List.of(Component.literal("Random")));
        }
    }
}
