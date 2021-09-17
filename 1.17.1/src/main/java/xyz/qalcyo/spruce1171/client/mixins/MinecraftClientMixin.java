package xyz.qalcyo.spruce1171.client.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qalcyo.spruce1171.client.SpruceClient;

@Mixin({MinecraftClient.class})
public class MinecraftClientMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initialize(RunArgs args, CallbackInfo ci) {
        if (SpruceClient.getInstance().initialize()) {
            LogManager.getLogger("Spruce").info("Loaded Spruce successfully!");
        }
    }

}