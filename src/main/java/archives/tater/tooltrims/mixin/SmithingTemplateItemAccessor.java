package archives.tater.tooltrims.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public interface SmithingTemplateItemAccessor {
    @Invoker
    static List<Identifier> invokeCreateTrimmableMaterialIconList() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getEMPTY_SLOT_PICKAXE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getEMPTY_SLOT_SPEAR() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getEMPTY_SLOT_SHOVEL() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getEMPTY_SLOT_SWORD() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getEMPTY_SLOT_AXE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Identifier getEMPTY_SLOT_HOE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Component getARMOR_TRIM_ADDITIONS_SLOT_DESCRIPTION() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Component getARMOR_TRIM_INGREDIENTS() {
        throw new UnsupportedOperationException();
    }
}
