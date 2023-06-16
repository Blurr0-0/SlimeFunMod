package net.blurr.slimefun.item;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FortuneCookie extends Item {

    private static final @NotNull String[] cookieMessages = new String[] {
            "Help me, I am trapped in a Fortune Cookie Factory!",
            "You will die tomorrow... by a Creeper",
            "At some point in your Life something bad will happen!!!",
            "Next week you will notice that this is not the real world, you are in a computer game",
            "This cookie will taste good in a few seconds",
            "The last word you will hear is gonna be \"EXTERMINATE!!!\"",
            "Whatever you do, do not hug a Creeper... I tried it. It feels good, but it''s not worth it.",
            "42. The answer is 42.",
            "A Walshy a day will keep the troubles away.",
            "Never dig straight down!",
            "Tis but a flesh wound!",
            "Always look on the bright side of life!",
            "This one was actually a Biscuit and not a Cookie",
            "Neon signs are LIT!"
    };
    public FortuneCookie(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        int index = ThreadLocalRandom.current().nextInt(cookieMessages.length);
        if (!pLevel.isClientSide()) {
            pLivingEntity.sendMessage(new TextComponent(cookieMessages[index]), pLivingEntity.getUUID());
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}
