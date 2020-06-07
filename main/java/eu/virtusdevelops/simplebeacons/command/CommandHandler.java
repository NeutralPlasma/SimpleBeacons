package eu.virtusdevelops.simplebeacons.command;

import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {
    private MessagesHandler messagesHandler;

    public CommandHandler(MessagesHandler messagesHandler){
        this.messagesHandler = messagesHandler;
    }

    //This is where we will store the commands
    private static HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();

    //Register method. When we register commands in our onEnable() we will use this.
    public void register(String name, CommandInterface cmd) {
        //When we register the command, this is what actually will put the command in the hashmap.
        commands.put(name, cmd);
    }

    //This will be used to check if a string exists or not.
    public boolean exists(String name) {
        //To actually check if the string exists, we will return the hashmap
        return commands.containsKey(name);
    }

    //Getter method for the Executor.
    public CommandInterface getExecutor(String name) {
        //Returns a command in the hashmap of the same name.
        return commands.get(name);
    }

    //This will be a template. All commands will have this in common.
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        //If there aren't any arguments, what is the command name going to be? For this example, we are going to call it /example.
        //This means that all commands will have the base of /example.
        if(args.length == 0) {
            getExecutor("simplebeacons").onCommand(sender, cmd, commandLabel, args);
            return true;
        }

        if(exists(args[0])){

            //the command /example args, args is our args[0].
            getExecutor(args[0]).onCommand(sender, cmd, commandLabel, args);
            return true;
        } else {

            //We want to send a message to the sender if the command doesn't exist.
            sender.sendMessage(messagesHandler.getMessage("commands.unknowncommand").replace("{command}", args[0]));
            return true;
        }
    }
}
