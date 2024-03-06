package santa.ping;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.FMLLog;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PingEventHandler {
    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        String name = player.getName().toLowerCase();
        IChatComponent component = event.message;
        String text = event.message.getUnformattedText().toLowerCase();
        text = text.replaceFirst("<.+>", "");
        
        Pattern p = Pattern.compile("(:|>|Alert]|by)(.*(" + name + "))");
        Matcher m = p.matcher(text);
        
        if (m.find()) {
            playSoundSendMessage(component);
            FMLLog.warning("[Ping!] You have been mentioned (username)");
        } else if (Ping.customNames != null) {
            for (int i = 0; i < Ping.customNames.length; i++) {
                Pattern q = Pattern.compile("(:|>|Alert]|by)(.*(" + Ping.customNames[i] + "))");
                Matcher n = q.matcher(text);

                if (n.find()) {
                    playSoundSendMessage(component);
                    FMLLog.warning("[Ping!] You have been mentioned (custom name - " + Ping.customNames[i] + ")");
                    break;
                }
            }
        }
    }

    /**
     * Plays the sound defined in the config, and alters the chat message based on the config.
     *
     * @param component The ChatComponent to change.
     */
    private void playSoundSendMessage(IChatComponent component) {
        Minecraft.getMinecraft().getSoundHandler().playSound(new PingSound(Config.soundType));
        if (Ping.customColor != null) {
            component.getChatStyle().setColor(Ping.customColor);
        }

        component.getChatStyle().setBold(Config.bold);
        component.getChatStyle().setItalic(Config.italic);
        component.getChatStyle().setStrikethrough(Config.strikethrough);
        component.getChatStyle().setUnderlined(Config.underline);
    }
}