package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(value = top.theillusivec4.curios.client.ClientEventHandler.class, remap = false)
public class ClientEventHandlerMixinCuriosAPI {

    // 临时存储捕获到的 entry 变量
    private Map.Entry<Attribute, AttributeModifier> capturedEntry;
    @Redirect(
            method = "onTooltip(Lnet/minecraftforge/event/entity/player/ItemTooltipEvent;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;",
                    ordinal = 0  // 先试0，不行就改1/2（后面说怎么找）
            ),
            require = 0  // 改为0，先确保Mixin能加载，再调ordinal
    )
    private AttributeModifier.Operation brutality$fakeOperationForCurios(AttributeModifier instance) {
        // 第二步：使用捕获到的 entry 变量
        AttributeModifier.Operation original = instance.getOperation();
        if (this.capturedEntry != null && this.capturedEntry.getKey() instanceof BrutalityAttributes.RangedPercentageAttribute) {
            if (original == AttributeModifier.Operation.ADDITION) {
                return AttributeModifier.Operation.MULTIPLY_BASE;
            }
        }
        return original;
    }

    /**
     * 关键：在 entry 赋值的位置捕获变量
     * ordinal = 0：先试0，不行就改1（对应循环内 entry 的赋值位置）
     */
    @Inject(
            method = "onTooltip(Lnet/minecraftforge/event/entity/player/ItemTooltipEvent;)V",
            at = @At(
                    value = "INVOKE",
                    // 匹配 multimap.entries().iterator().next() 调用
                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;",
                    ordinal = 0  // 先试0，不行改1/2（循环内第1/2/3次next()调用）
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,  // 捕获失败不抛错
            require = 0
    )
    private void brutality$captureEntry(
            ItemTooltipEvent evt,
            CallbackInfo ci,
            // 捕获 iterator.next() 返回的 entry 变量（原生 @Local 注解）
            @Local(ordinal = 0)
            Map.Entry<Attribute, AttributeModifier> entry
    ) {
        // 存储到字段供 Redirect 使用
        this.capturedEntry = entry;
    }
}