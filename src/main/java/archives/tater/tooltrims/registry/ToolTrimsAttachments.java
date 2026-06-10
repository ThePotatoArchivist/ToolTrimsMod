package archives.tater.tooltrims.registry;

import archives.tater.tooltrims.ToolTrims;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

public class ToolTrimsAttachments {
    public static final AttachmentType<ArmorTrim> TRIDENT_TRIM = AttachmentRegistry.create(
            ToolTrims.id("trident_trim"), builder -> builder
            .syncWith(ArmorTrim.STREAM_CODEC, AttachmentSyncPredicate.all())
    );

    public static void init() {}
}
