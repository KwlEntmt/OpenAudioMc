package com.craftmend.openaudiomc.generic.commands.interfaces;

import com.craftmend.openaudiomc.OpenAudioMc;
import com.craftmend.openaudiomc.generic.commands.CommandService;
import com.craftmend.openaudiomc.generic.platform.Platform;
import com.craftmend.openaudiomc.generic.commands.objects.Argument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {

    @Getter private String command;
    @Getter private List<String> aliases = new ArrayList<>();
    @Getter private List<Argument> arguments = new ArrayList<>();

    /**
     * @param argument Your command name. For example "select"
     */
    public SubCommand(String argument) {
        this.command = argument;
        if (OpenAudioMc.getInstance().getPlatform() == Platform.SPIGOT) {
            // try, could already be registered
            try {
                Bukkit.getPluginManager().addPermission(new Permission("openaudiomc.commands." + command));
            } catch (IllegalArgumentException e) {
                // ignored
            }
        }
    }

    public SubCommand(String argument, String... aliases) {
        this(argument);
        this.aliases = Arrays.asList(aliases);
    }

    /**
     * send a openaudiomc styled message
     *
     * @param sender Command sender
     * @param message Your message
     */
    protected void message(GenericExecutor sender, String message) {
        sender.sendMessage(OpenAudioMc.getService(CommandService.class).getCommandPrefix() + message);
    }

    /**
     * check if the sender has permissions to execute this command.
     * you do not need to run this check itself, its used by the framework.
     *
     * @param commandSender Command sender
     * @return true if the player is allowed to execute a command
     */
    public boolean isAllowed(GenericExecutor commandSender) {
        return commandSender.hasPermission("openaudiomc.commands." + command)
                || commandSender.hasPermission("openaudiomc.commands.*")
                || commandSender.hasPermission("openaudiomc.*");
    }

    protected String getColor(String color) {
        return Platform.makeColor(color);
    }

    /**
     * Register one or more arguments.
     * used for auto complete and the help menu
     *
     * @param args one or more arguments
     */
    protected void registerArguments(Argument... args) {
        arguments.addAll(Arrays.asList(args));
    }

    /**
     * @param sender the sender that executed the commands
     * @param args the arguments after your command, starting at index 0
     */
    public abstract void onExecute(GenericExecutor sender, String[] args);

    protected boolean isInteger(String s) {
        return isInteger(s,10);
    }

    private boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    @AllArgsConstructor
    public static class CommandArguments {
        private String[] args;

        public String getSaveString(int index) {
            if (args.length >= (index + 1)) {
                return args[index];
            }
            return "";
        }
    }
}
