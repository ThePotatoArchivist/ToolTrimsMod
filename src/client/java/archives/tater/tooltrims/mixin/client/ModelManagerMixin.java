package archives.tater.tooltrims.mixin.client;

import archives.tater.tooltrims.client.ToolTrimsClient;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.cuboid.CuboidModel;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@Mixin(ModelManager.class)
public class ModelManagerMixin {

    @Inject(
            method = "lambda$loadBlockModels$1",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Util;sequence(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;")
    )
    private static void addTrimModels(Executor executor, Map<Identifier, Resource> resources, CallbackInfoReturnable<CompletionStage<Map<Identifier, Resource>>> cir, @Local(name = "result") List<CompletableFuture<Pair<Identifier, CuboidModel>>> result) {
        ToolTrimsClient.getTrimModels().forEach(pair -> result.add(CompletableFuture.completedFuture(pair)));
    }
}
