package eu.virtusdevelops.simplebeacons.gui.guis;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.gui.Handler;
import eu.virtusdevelops.simplebeacons.gui.Icon;
import eu.virtusdevelops.simplebeacons.gui.InventoryCreator;
import eu.virtusdevelops.simplebeacons.gui.actions.ClickAction;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.TextFormater;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModulesSelectGUI {
    private Handler handler;
    private SimpleBeacons simpleBeacons;
    private BeaconHandler beaconHandler;
    private MessagesHandler message;

    public ModulesSelectGUI(SimpleBeacons simpleBeacons, MessagesHandler messagesHandler, BeaconHandler beaconHandler, Handler handler){
        this.simpleBeacons = simpleBeacons;
        this.message = messagesHandler;
        this.beaconHandler = beaconHandler;
        this.handler = handler;
    }

    public void construct(Player player, BeaconData beaconData, int page){
        ArrayList<Integer> slots = new ArrayList<>();
        ArrayList<String> lore = new ArrayList<>();
        // Initialize inventory
        InventoryCreator inventoryCreator = new InventoryCreator(45, message.getMessage("GUI.modules_label"));
        // Get all effects
        List<String> modules = beaconData.enabledModules;
        List<String> modules2 = new ArrayList<>(beaconHandler.getModules(beaconData));
        if(modules.size() < modules2.size()){
            modules = modules2;
            beaconData.enabledModules = modules;
        }

        // Inventory Icons
        int position = 9;
        int start = 0, max;
        if(modules.size() > 27){
            start = page * 27;
        }
        if(modules.size() - start > 27){
            max = 27;
        }else{
            max = modules.size() - start;
        }

        for(int i = start; i < max + start; i++){
            String module = modules.get(i);
            String[] splitedModule = module.split(":");
            if(player.hasPermission("simplebeacons.module." + splitedModule[0])) {
                lore.clear();
                ItemStack effectPotion = new ItemStack(Material.BOOK);
                if(splitedModule[0].equalsIgnoreCase("farm")){
                    effectPotion.setType(Material.DIAMOND_HOE);
                }else if(splitedModule[0].equalsIgnoreCase("effects")){
                    effectPotion.setType(Material.POTION);
                }else if(splitedModule[0].equalsIgnoreCase("protect")){
                    effectPotion.setType(Material.IRON_SWORD);
                }else if(splitedModule[0].equalsIgnoreCase("breed")){
                    effectPotion.setType(Material.WHEAT);
                }else if(splitedModule[0].equalsIgnoreCase("ore")){
                    effectPotion.setType(Material.DIAMOND_PICKAXE);
                }else if(splitedModule[0].equalsIgnoreCase("item")){
                    effectPotion.setType(Material.HOPPER);
                }

                ItemMeta meta = effectPotion.getItemMeta();

                String enabled = "disabled";
                if(splitedModule.length > 1){
                    enabled = splitedModule[1];
                }
                String name = message.formatString(message.getRawMessage("modules.format"),
                        new String[]{"{module}", "{status}"}, new String[]{message.getRawMessage("modules." + splitedModule[0]), enabled});

                meta.setDisplayName(TextFormater.sFormatText(name));
                lore.add("&7Click to enable/disable.");
                meta.setLore(TextFormater.colorFormatList(lore));
                effectPotion.setItemMeta(meta);


                Icon effectIcon = new Icon(effectPotion);
                List<String> finalEffects = modules;
                effectIcon.addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {
                        finalEffects.remove(module);
                        String newStatus = "disabled";
                        if(splitedModule[1].equalsIgnoreCase("disabled")){
                            newStatus = "enabled";
                        }
                        if(beaconHandler.getModulesRaw(beaconData).contains(splitedModule[0])) {
                            finalEffects.add(splitedModule[0] + ":" + newStatus);
                        }
                        Collections.sort(finalEffects);
                        beaconData.enabledModules = finalEffects;
                        beaconHandler.updateBeacon(beaconData);
                        handler.openModulesGUI(player, beaconData, page);
                    }
                });
                inventoryCreator.setIcon(position, effectIcon);
                slots.add(position);
                position++;
            }
        }

        // NEXT PAGE ITEM
        if(modules.size() > start+max){
            ItemStack nextPage = new ItemStack(Material.PAPER);
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            nextPageMeta.setDisplayName(TextFormater.sFormatText("&bNext page"));
            nextPage.setItemMeta(nextPageMeta);
            Icon nextpageIcon = new Icon(nextPage);
            nextpageIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    construct(player, beaconData, page+1);
                }
            });
            inventoryCreator.setIcon(44, nextpageIcon);
            slots.add(44);
        }
        if(page > 0){
            ItemStack prevPage = new ItemStack(Material.PAPER);
            ItemMeta prevPageMeta = prevPage.getItemMeta();
            prevPageMeta.setDisplayName(TextFormater.sFormatText("&bPrevious page"));
            prevPage.setItemMeta(prevPageMeta);
            Icon prevpageIcon = new Icon(prevPage);
            prevpageIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    construct(player, beaconData, page-1);
                }
            });
            inventoryCreator.setIcon(36, prevpageIcon);
            slots.add(36);
        }

        // Background
        ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta = background.getItemMeta();
        backgroundMeta.setDisplayName(TextFormater.sFormatText("&7"));
        background.setItemMeta(backgroundMeta);
        Icon backgroundIcon = new Icon(background);
        backgroundIcon.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {

            }
        });


        ItemStack background2 = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta backgroundMeta2 = background2.getItemMeta();
        backgroundMeta2.setDisplayName(TextFormater.sFormatText("&7"));
        background2.setItemMeta(backgroundMeta2);
        Icon backgroundIcon2 = new Icon(background2);
        backgroundIcon2.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {

            }
        });

        for(int i = 0; i < 45; i++){
            if (!slots.contains(i)){
                if(i == 0 || i == 1 || i == 7 || i == 8 || i == 36 || i == 44 || i == 43){
                    inventoryCreator.setIcon(i, backgroundIcon2);
                }else {
                    inventoryCreator.setIcon(i, backgroundIcon);
                }
            }
        }

        // Open inventory
        player.openInventory(inventoryCreator.getInventory());
    }
}
