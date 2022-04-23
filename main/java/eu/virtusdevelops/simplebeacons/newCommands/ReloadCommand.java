package eu.virtusdevelops.simplebeacons.newCommands;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends AbstractCommand {
    private SimpleBeacons plugin;
    private MessagesHandler messagesHandler;

    public ReloadCommand(SimpleBeacons plugin, MessagesHandler messagesHandler){
        super(CommandType.BOTH, false, "reload");
        this.plugin = plugin;
        this.messagesHandler = messagesHandler;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        long time = System.currentTimeMillis();
        plugin.reload();
        time = System.currentTimeMillis() - time;
        sender.sendMessage(messagesHandler.getMessage("commands.reloadsuccess").replace("{time}" , String.valueOf(time)));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
       return null;
    }


    @Override
    public String getPermissionNode() {
        return "simplebeacons.command.reload";
    }

    @Override
    public String getSyntax() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads configuration";
    }
}
