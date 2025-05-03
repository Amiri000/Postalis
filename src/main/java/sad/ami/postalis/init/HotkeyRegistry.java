package sad.ami.postalis.init;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import sad.ami.postalis.Postalis;

@EventBusSubscriber(modid = Postalis.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HotkeyRegistry {
    public static final KeyMapping CHECKLIST_MENU = new KeyMapping("key.postalis.checklist_menu", GLFW.GLFW_KEY_Z, "Postalis");

    @SubscribeEvent
    public static void onKeybindingRegistry(RegisterKeyMappingsEvent event) {
        event.register(CHECKLIST_MENU);
    }
}
