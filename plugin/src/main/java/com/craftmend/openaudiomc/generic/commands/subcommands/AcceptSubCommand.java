package com.craftmend.openaudiomc.generic.commands.subcommands;

import com.craftmend.openaudiomc.OpenAudioMc;
import com.craftmend.openaudiomc.generic.commands.CommandService;
import com.craftmend.openaudiomc.generic.commands.interfaces.GenericExecutor;
import com.craftmend.openaudiomc.generic.commands.interfaces.SubCommand;
import com.craftmend.openaudiomc.generic.craftmend.CraftmendService;
import com.craftmend.openaudiomc.generic.storage.interfaces.Configuration;
import com.craftmend.openaudiomc.generic.storage.enums.StorageKey;
import com.craftmend.openaudiomc.generic.networking.client.objects.player.ClientConnection;
import com.craftmend.openaudiomc.generic.networking.interfaces.NetworkingService;

public class AcceptSubCommand extends SubCommand {

    public AcceptSubCommand() {
        super("accept");
    }

    @Override
    public void onExecute(GenericExecutor sender, String[] args) {
        // set the value to true
        Configuration config = OpenAudioMc.getInstance().getConfiguration();
        NetworkingService service = OpenAudioMc.getService(NetworkingService.class);

        config.setBoolean(StorageKey.LEGAL_ACCEPTED_TOS_AND_PRIVACY, true);
        config.saveAll();

        sender.sendMessage(OpenAudioMc.getService(CommandService.class).getCommandPrefix() + "Welcome to OpenAudioMc! you accepted the terms, enjoy the service!");

        OpenAudioMc.getService(CraftmendService.class).startSyncronizer();
        service.connectIfDown();

        for (ClientConnection client : service.getClients()) {
            client.publishUrl();
        }
    }
}
