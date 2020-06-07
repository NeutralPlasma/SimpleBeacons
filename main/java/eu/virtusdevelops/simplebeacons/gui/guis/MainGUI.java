package eu.virtusdevelops.simplebeacons.gui.guis;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconInteract;
import eu.virtusdevelops.simplebeacons.gui.Handler;
import eu.virtusdevelops.simplebeacons.gui.Icon;
import eu.virtusdevelops.simplebeacons.gui.InventoryCreator;
import eu.virtusdevelops.simplebeacons.gui.actions.ClickAction;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.TextFormater;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MainGUI {
    private Handler handler;
    private SimpleBeacons simpleBeacons;
    private BeaconHandler beaconHandler;
    private MessagesHandler message;

    public MainGUI(SimpleBeacons simpleBeacons, MessagesHandler messagesHandler, BeaconHandler beaconHandler, Handler handler){
        this.simpleBeacons = simpleBeacons;
        this.message = messagesHandler;
        this.beaconHandler = beaconHandler;
        this.handler = handler;
    }

    public void construct(Player player, BeaconData beaconData){
        ArrayList<Integer> slots = new ArrayList<>();
        // Initialize inventory
        InventoryCreator inventoryCreator = new InventoryCreator(27,
                TextFormater.sFormatText(message.formatString(message.getMessage("GUI.main_label"), "{beacon}:" +
                        simpleBeacons.getConfig().getString("beacons." + beaconData.level + ".name"))));
        // Items in inventory
        // STATS ICON
        ItemStack stats = new ItemStack(Material.BEACON);

        String[] effectdata = beaconData.selectedEffect.split(":");
        String leveldata = effectdata.length > 1 ? effectdata[1] : "0";

        stats = message.getItem(stats, "GUI.icons.beacon_stats", "{effect}:"
                + message.formatString(message.getRawMessage("effects.format"),
                "{effect}:" + message.getRawMessage("effects." + effectdata[0]), "{level}:" + leveldata), "{level}:" + beaconData.level);

        Icon statsIcon = new Icon(stats);
        statsIcon.addClickAction(new ClickAction() {
            @Override
            public void execute(Player player) {

            }
        });
        inventoryCreator.setIcon(13, statsIcon);
        slots.add(13);

        // SELECT EFFECT ICON
        if(player.hasPermission("simplebeacons.selecteffect")) {
            ItemStack effect = new ItemStack(Material.POTION);

            effect = message.getItem(effect, "GUI.icons.effect_select", "{effect}:"
                    + message.formatString(message.getRawMessage("effects.format"),
                    "{effect}:" + message.getRawMessage("effects." + effectdata[0]), "{level}:" + leveldata));


            PotionMeta effectMeta = (PotionMeta) effect.getItemMeta();

            int tickrate = simpleBeacons.getConfig().getInt("BEACON_TICK_RATE");
            if (effectdata.length > 1) {
                effectMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(effectdata[0]),
                        tickrate + 20, Integer.parseInt(effectdata[1])), true);
            } else {
                effectMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(effectdata[0]),
                        tickrate + 20, 0), true);
            }

            effect.setItemMeta(effectMeta);

            Icon effectIcon = new Icon(effect);
            effectIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    handler.openEffectGUI(player, beaconData, 0);
                    //player.sendMessage("Currently selected: " + beaconData.selectedEffect);
                }
            });
            inventoryCreator.setIcon(4, effectIcon);
            slots.add(4);
        }

        // SELECT MODULE ICON
        if(player.hasPermission("simplebeacons.selectmodules")) {
            ItemStack module = new ItemStack(Material.BOOK);

            module = message.getItem(module, "GUI.icons.module_select");




            Icon moduleIcon = new Icon(module);
            moduleIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    handler.openModulesGUI(player, beaconData, 0);
                    //player.sendMessage("Currently selected: " + beaconData.selectedEffect);
                }
            });
            inventoryCreator.setIcon(11, moduleIcon);
            slots.add(11);
        }

        // LINK BEACON
        int links = simpleBeacons.getConfig().getInt("beacons." + beaconData.level + ".linkamount");
        if(player.hasPermission("simplebeacons.linkbeacon") && links > 0) {
            ItemStack link = new ItemStack(Material.CHEST);


            link = message.getItem(link, "GUI.icons.link",
                    "{current}:"+beaconData.linkedLocations.size(), "{max}:" + links);

            Icon moduleIcon = new Icon(link);
            moduleIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    //handler.openModulesGUI(player, beaconData, 0);
                    simpleBeacons.updateLinking(player, beaconData);
                    player.closeInventory();
                    player.sendMessage("Max links amount: " + links);
                }
            });

            inventoryCreator.setIcon(22, moduleIcon);
            slots.add(22);
        }

        // UPGRADE BEACON
        if(player.hasPermission("simplebeacons.upgrade.eco")) {
            boolean canlevel = simpleBeacons.getConfig().contains("beacons." + (beaconData.level + 1));

            ItemStack upgradeMoney = new ItemStack(Material.EMERALD);

            double price = simpleBeacons.getConfig().getDouble("beacons." + (beaconData.level + 1) + ".price");
            if(canlevel) {
                upgradeMoney = message.getItem(upgradeMoney, "GUI.icons.upgrade_eco",
                        "{price}:" + price);

            }else{
                upgradeMoney = message.getItem(upgradeMoney, "GUI.icons.upgrade_maxed");
            }


            Icon upgradeMoneyIcon = new Icon(upgradeMoney);
            if(canlevel) {
                upgradeMoneyIcon.addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {
                        double balance = simpleBeacons.getEconomy().getBalance(player);
                        if (balance >= price) {
                            simpleBeacons.getEconomy().withdrawPlayer(player, price);
                            simpleBeacons.getEconomy().depositPlayer(player, price);
                            beaconData.level = beaconData.level + 1;
                            beaconHandler.updateBeacon(beaconData);

                            handler.openMainGUI(player, beaconData);
                            player.sendMessage(message.getMessage("economy.success"));
                        } else {
                            player.sendMessage(message.getMessage("economy.nomoney"));
                        }
                        //player.sendMessage("Currently selected: " + beaconData.selectedEffect);
                    }
                });
            }else{
                upgradeMoneyIcon.addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {

                    }
                });
            }
            inventoryCreator.setIcon(15, upgradeMoneyIcon);
            slots.add(15);
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

        for(int i = 0; i < 27; i++){
            if (!slots.contains(i)){
                if(i == 0 || i == 1 || i == 7 || i == 8 || i == 9 || i == 17
                        || i == 18 || i == 19 || i == 25 || i == 26){
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
