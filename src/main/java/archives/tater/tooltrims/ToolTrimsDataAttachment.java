package archives.tater.tooltrims;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.item.equipment.trim.ArmorTrim;

@SuppressWarnings("UnstableApiUsage")
public class ToolTrimsDataAttachment {
    public static final AttachmentType<ArmorTrim> TRIDENT_TRIM = AttachmentRegistry.create(ToolTrims.id("trident_trim"), builder -> builder
            .syncWith(ArmorTrim.PACKET_CODEC, AttachmentSyncPredicate.all())
    );

    public static void register() {}
}
