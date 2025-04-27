package archives.tater.tooltrims.networking;

import archives.tater.tooltrims.ToolTrims;
import archives.tater.tooltrims.mixin.TridentEntityAccessor;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public record TridentTrimPacket(
        int tridentId,
        RegistryKey<ArmorTrimPattern> pattern,
        RegistryKey<ArmorTrimMaterial> material
) implements FabricPacket {
    public static PacketType<TridentTrimPacket> TYPE = PacketType.create(ToolTrims.id("trim"), TridentTrimPacket::read);

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(tridentId);
        packetByteBuf.writeRegistryKey(pattern);
        packetByteBuf.writeRegistryKey(material);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public static @Nullable TridentTrimPacket of(TridentEntity tridentEntity) {
        var trim = ArmorTrim.getTrim(tridentEntity.getWorld().getRegistryManager(), ((TridentEntityAccessor) tridentEntity).getTridentStack());
        return trim.map(armorTrim -> new TridentTrimPacket(
                tridentEntity.getId(),
                armorTrim.getPattern().getKey().orElseThrow(),
                armorTrim.getMaterial().getKey().orElseThrow()
        )).orElse(null);
    }

    public static TridentTrimPacket read(PacketByteBuf packetByteBuf) {
        return new TridentTrimPacket(
                packetByteBuf.readInt(),
                packetByteBuf.readRegistryKey(RegistryKeys.TRIM_PATTERN),
                packetByteBuf.readRegistryKey(RegistryKeys.TRIM_MATERIAL)
        );
    }

    public static void trySend(Entity trackedEntity, ServerPlayerEntity player) {
        if (!(trackedEntity instanceof TridentEntity tridentEntity)) return;
        var packet = of(tridentEntity);
        if (packet == null) return;
        ServerPlayNetworking.send(player, packet);
    }
}
