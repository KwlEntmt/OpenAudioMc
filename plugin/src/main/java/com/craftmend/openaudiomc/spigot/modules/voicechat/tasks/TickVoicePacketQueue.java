package com.craftmend.openaudiomc.spigot.modules.voicechat.tasks;

import com.craftmend.openaudiomc.OpenAudioMc;
import com.craftmend.openaudiomc.generic.networking.client.objects.player.ClientConnection;
import com.craftmend.openaudiomc.generic.networking.interfaces.NetworkingService;
import com.craftmend.openaudiomc.generic.networking.packets.client.voice.PacketClientUpdateVoiceLocations;
import com.craftmend.openaudiomc.generic.networking.payloads.client.voice.ClientVoiceUpdatePeerLocationsPayload;

import java.util.HashSet;

public class TickVoicePacketQueue implements Runnable {

    @Override
    public void run() {
        for (ClientConnection client : OpenAudioMc.getService(NetworkingService.class).getClients()) {
            if (!client.getClientRtcManager().getLocationUpdateQueue().isEmpty()) {
                client.sendPacket(new PacketClientUpdateVoiceLocations(
                        new ClientVoiceUpdatePeerLocationsPayload(
                                new HashSet<>(client.getClientRtcManager().getLocationUpdateQueue())
                        )
                ));
                client.getClientRtcManager().getLocationUpdateQueue().clear();
            }
        }
    }
}
