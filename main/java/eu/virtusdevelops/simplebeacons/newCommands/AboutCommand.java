package eu.virtusdevelops.simplebeacons.newCommands;

import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AboutCommand extends AbstractCommand {

    public AboutCommand(){
        super(CommandType.BOTH, false, "about");
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        String message = "&8====================== \n" +
                "&7Plugin link: &ehttps://songoda.com/marketplace/product/%%__PLUGIN__%% \n" +
                "&7Bought by: &ehttps://songoda.com/profile/%%__USERNAME__%% \n" +
                "&8====================== ";
        sender.sendMessage(TextUtils.colorFormat(message));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        return null;
    }


    @Override
    public String getPermissionNode() {
        return "simplebeacons.command.about";
    }

    @Override
    public String getSyntax() {
        return "about";
    }

    @Override
    public String getDescription() {
        return "Information about plugin.";
    }

}
