package com.craftmend.openaudiomc.generic.networking.interfaces;

import com.craftmend.openaudiomc.OpenAudioMc;
import com.craftmend.openaudiomc.generic.networking.client.objects.player.ClientConnection;
import com.craftmend.openaudiomc.generic.networking.client.objects.player.PlayerSession;
import com.craftmend.openaudiomc.generic.networking.enums.MediaError;
import com.craftmend.openaudiomc.generic.networking.packets.PacketSocketKickClient;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface Authenticatable {

    boolean isTokenCorrect(String token);
    void onConnect();
    void onDisconnect();
    boolean getIsConnected();
    String getOwnerName();
    UUID getOwnerUUID();
    PlayerSession getSessionTokens();
    void handleError(MediaError error, String source);

    default void kickConnection() {
        OpenAudioMc.getService(NetworkingService.class).send(this, new PacketSocketKickClient());
    }

    static Authenticatable get(Player player) {
        return get(player.getUniqueId());
    }

    static Authenticatable get(ProxiedPlayer player) {
        return get(player.getUniqueId());
    }

    static Authenticatable get(UUID uuid) {
        ClientConnection clientConnection = OpenAudioMc.getService(NetworkingService.class).getClient(uuid);
        return clientConnection;
    }

}
