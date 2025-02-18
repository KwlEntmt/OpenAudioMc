package com.craftmend.openaudiomc.generic.resources;

import com.craftmend.openaudiomc.OpenAudioMc;
import com.craftmend.openaudiomc.generic.resources.storage.SavedRoot;
import com.craftmend.openaudiomc.generic.service.Service;
import com.craftmend.openaudiomc.spigot.OpenAudioMcSpigot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@NoArgsConstructor
public class ResourceService extends Service {

    @Getter
    private SavedRoot savedRoot;

    @Override
    public void onEnable() {
        loadData();
    }

    private void loadData() {
        File f = new File("plugins" + File.separator  + "OpenAudioMc" + File.separator + "persistent.json");
        if (f.exists() && !f.isDirectory()) {
            try {
                savedRoot = OpenAudioMc.getGson().fromJson(
                        new String(Files.readAllBytes(f.toPath())),
                        SavedRoot.class
                );
                return;
            } catch (IOException e) {
                e.printStackTrace();
                createNew();
                return;
            }
        }
        createNew();
    }

    private void createNew() {
        savedRoot = new SavedRoot();
        savedRoot.seed();
        saveData();
    }

    public void saveData() {
        Charset charset = Charset.forName(StandardCharsets.UTF_8.name());
        try (BufferedWriter writer = Files.newBufferedWriter(new File("plugins" + File.separator  + "OpenAudioMc" + File.separator + "persistent.json").toPath(), charset)) {
            String input = OpenAudioMc.getGson().toJson(savedRoot);
            writer.write(input);
            writer.flush();
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

}
