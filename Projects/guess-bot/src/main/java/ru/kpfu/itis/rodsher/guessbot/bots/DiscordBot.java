package ru.kpfu.itis.rodsher.guessbot.bots;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.kpfu.itis.rodsher.guessbot.models.ChannelType;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.services.MessageHandler;

import javax.annotation.Nonnull;

public class DiscordBot extends ListenerAdapter {

    private final MessageHandler messageHandler;

    public DiscordBot(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        ChannelType channelType = ChannelType.GROUP;
        if(event.getChannelType().equals(net.dv8tion.jda.api.entities.ChannelType.PRIVATE)) {
            channelType = ChannelType.PRIVATE;
        }
        messageHandler.handleMessage(ClientType.DISCORD, event.getChannel().getId(), channelType, event.getAuthor().getId(), event.getMessage().getContentRaw());
    }
}
