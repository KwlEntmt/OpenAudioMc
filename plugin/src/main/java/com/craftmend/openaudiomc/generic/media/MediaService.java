package com.craftmend.openaudiomc.generic.media;

import com.craftmend.openaudiomc.generic.media.interfaces.ForcedUrlMutation;
import com.craftmend.openaudiomc.generic.media.interfaces.UrlMutation;
import com.craftmend.openaudiomc.generic.media.middleware.DropBoxMiddleware;
import com.craftmend.openaudiomc.generic.service.Service;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class MediaService extends Service {

    private Map<String, List<UrlMutation>> urlMutations = new HashMap<>();
    @Getter private List<Runnable> resetTriggers = new ArrayList<>();

    @Override
    public void onEnable() {
        // register default mutations
        registerMutation("https://www.dropbox.com", new DropBoxMiddleware());
        // note that google drive, soundcloud, ssl-proxy and youtube are handled client side
    }

    public void registerMutation(String host, UrlMutation urlMutation) {
        List<UrlMutation> list = urlMutations.getOrDefault(host, new ArrayList<>());
        list.add(urlMutation);
        urlMutations.put(host, list);
        resetTriggers.forEach(Runnable::run);
    }

    /**
     * Process the url trough the mutation api
     *
     * @param original the original url
     * @return the altered url
     */
    public String process(String original) {
        for (String selector : urlMutations.keySet()) {
            if (original.startsWith(selector)) {
                for (UrlMutation urlMutation : urlMutations.get(selector)) {
                    if (urlMutation instanceof ForcedUrlMutation) {
                        original = urlMutation.onRequest(original);
                    }
                }
            }
        }

        for (String selector : urlMutations.keySet()) {
            if (original.startsWith(selector)) {
                for (UrlMutation urlMutation : urlMutations.get(selector)) {
                    if (!(urlMutation instanceof ForcedUrlMutation))
                    original = urlMutation.onRequest(original);
                }
            }
        }

        return original;
    }

}
