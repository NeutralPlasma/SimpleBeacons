package eu.virtusdevelops.simplebeacons.gui;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.InventoryCreator;
import eu.virtusdevelops.virtuscore.gui.actions.ClickAction;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MainGUI {
    private Player player;
    private SimpleBeacons simpleBeacons;
    private BeaconHandler beaconHandler;
    private MessagesHandler message;
    private InventoryCreator inventory;
    private BeaconData data;

    public MainGUI(Player player, BeaconData data, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler, BeaconHandler beaconHandler){
        this.simpleBeacons = simpleBeacons;
        this.message = messagesHandler;
        this.beaconHandler = beaconHandler;
        this.player = player;
        this.data = data;

        load();
    }

    private void load(){
        inventory = new InventoryCreator(27,
                TextUtils.colorFormat(message.formatString(message.getMessage("GUI.main_label"), "{beacon}:" +
                        simpleBeacons.getFileManager().getConfiguration("beacons").getString("beacons." + data.getLevel() + ".name"))));
        refresh();

        player.openInventory(inventory.getInventory());
    }

    private void refresh(){
        // Stats
        ItemStack statsItem = new ItemStack(Material.BEACON);
        String[] effectdata = data.getSelectedEffect().split(":");
        String leveldata = effectdata.length > 1 ? effectdata[1] : "0";
        statsItem = message.getItem(statsItem, "GUI.icons.beacon_stats", "{effect}:"
                + message.formatString(message.getRawMessage("effects.format"),
                "{effect}:" + message.getRawMessage("effects." + effectdata[0]), "{level}:" + leveldata), "{level}:" + data.getLevel());
        Icon statsIcon = new Icon(statsItem);
        statsIcon.addClickAction((player) -> {});
        inventory.setIcon(13, statsIcon);

        // Effect
        if(player.hasPermission("simplebeacons.select.effect")){
            ItemStack effect = new ItemStack(Material.POTION);
            effect = message.getItem(effect, "GUI.icons.effect_select", "{effect}:"
                    + message.formatString(message.getRawMessage("effects.format"),
                    "{effect}:" + message.getRawMessage("effects." + effectdata[0]), "{level}:" + leveldata));
            PotionMeta effectMeta = (PotionMeta) effect.getItemMeta();
            int tickrate = simpleBeacons.getFileManager().getConfiguration("beacons").getInt("BEACON_TICK_RATE");
            if (effectdata.length > 1) {
                effectMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(effectdata[0]),
                        tickrate + 20, Integer.parseInt(effectdata[1])), true);
            } else {
                effectMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(effectdata[0]),
                        tickrate + 20, 0), true);
            }
            effect.setItemMeta(effectMeta);
            Icon effectIcon = new Icon(effect);
            effectIcon.addClickAction(player -> {
                new EffectGUI(player, data, simpleBeacons, beaconHandler, message);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
            });
            inventory.setIcon( 4, effectIcon);


        }

        if(player.hasPermission("simplebeacons.select.module")) {
            ItemStack module = new ItemStack(Material.BOOK);
            module = message.getItem(module, "GUI.icons.module_select");
            Icon moduleIcon = new Icon(module);
            moduleIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    new ModuleGUI(player, data, simpleBeacons, beaconHandler, message);
                    player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                }
            });
            inventory.setIcon( 11, moduleIcon);
        }

        // LINK BEACON
        int links = simpleBeacons.getFileManager().getConfiguration("beacons").getInt("beacons." + data.getLevel() + ".linkamount");
        if(player.hasPermission("simplebeacons.linkbeacon") && links > 0) {
            ItemStack link = new ItemStack(Material.CHEST);
            link = message.getItem(link, "GUI.icons.link",
                    "{current}:"+data.getLinkedLocations().size(), "{max}:" + links);

            Icon moduleIcon = new Icon(link);
            moduleIcon.addClickAction(new ClickAction() {
                @Override
                public void execute(Player player) {
                    simpleBeacons.updateLinking(player, data);
                    player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);
                    player.closeInventory();
                    player.sendMessage("Max links amount: " + links);
                }
            });

            inventory.setIcon(22, moduleIcon);
        }

        // UPGRADE BEACON
        if(player.hasPermission("simplebeacons.upgrade.eco")) {
            boolean canlevel = simpleBeacons.getFileManager().getConfiguration("beacons").contains("beacons." + (data.getLevel() + 1));

            ItemStack upgradeMoney = new ItemStack(Material.EMERALD);

            double price = simpleBeacons.getFileManager().getConfiguration("beacons").getDouble("beacons." + (data.getLevel() + 1) + ".price");
            if(canlevel) {
                upgradeMoney = message.getItem(upgradeMoney, "GUI.icons.upgrade_eco",
                        "{price}:" + TextUtils.formatNumbers(price));
                
            }else{
                upgradeMoney = message.getItem(upgradeMoney, "GUI.icons.upgrade_maxed");
            }


            Icon upgradeMoneyIcon = new Icon(upgradeMoney);
            if(canlevel) {
                upgradeMoneyIcon.addClickAction(player1 -> {
                    double balance = simpleBeacons.getEconomy().getBalance(player1);
                    if (balance >= price) {
                        simpleBeacons.getEconomy().withdrawPlayer(player1, price);
                        data.setLevel(data.getLevel() + 1);
                        beaconHandler.updateBeacon(data);
                        player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
                        player1.sendMessage(message.getMessage("economy.success"));

                        load();
                    } else {
                        player1.playSound(player1.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                        player1.sendMessage(message.getMessage("economy.nomoney"));
                        load();
                    }
                });
            }else{
                upgradeMoneyIcon.addClickAction(player -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1));
            }
            inventory.setIcon(15, upgradeMoneyIcon);
        }
    }
}
