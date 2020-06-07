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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EffectSelectGUI {
    private Handler handler;
    private SimpleBeacons simpleBeacons;
    private BeaconHandler beaconHandler;
    private MessagesHandler message;

    public EffectSelectGUI(SimpleBeacons simpleBeacons, MessagesHandler messagesHandler,BeaconHandler beaconHandler, Handler handler){
        this.simpleBeacons = simpleBeacons;
        this.message = messagesHandler;
        this.beaconHandler = beaconHandler;
        this.handler = handler;
    }

    public void construct(Player player, BeaconData beaconData, int page){
        ArrayList<Integer> slots = new ArrayList<>();
        ArrayList<String> lore = new ArrayList<>();
        // Initialize inventory
        InventoryCreator inventoryCreator = new InventoryCreator(45, message.getMessage("GUI.effects_label"));
        // Get all effects
        List<String> modules = simpleBeacons.getConfig().getStringList("beacons." + beaconData.level + ".effects");
        List<String> modules2 = simpleBeacons.getConfig().getStringList("global.effects");
        TreeSet<String> set = new TreeSet<>(modules);
        set.addAll(modules2);
        ArrayList<String> effects = new ArrayList<>(set);

        // Inventory Icons
        int position = 9;
        int start = 0, max;
        if(effects.size() > 27){
            start = page * 27;
        }
        if(effects.size() - start > 27){
            max = 27;
        }else{
            max = effects.size() - start;
        }

        for(int i = start; i < max + start; i++){
            String effect = effects.get(i);
            String[] splitedEffect = effect.split(":");
            if(player.hasPermission("simplebeacons.effect." + splitedEffect[0])) {
                lore.clear();
                ItemStack effectPotion = new ItemStack(Material.POTION);
                PotionMeta potionMeta = (PotionMeta) effectPotion.getItemMeta();
                String[] color = message.getRawMessage("effects_colors." + splitedEffect[0]).split(":");
                potionMeta.setColor(Color.fromRGB(Integer.valueOf(color[0]),Integer.valueOf(color[1]),Integer.valueOf(color[2])));
                String level = "1";
                if(splitedEffect.length > 1){
                    level = splitedEffect[1];
                }
                String name = message.formatString(message.getRawMessage("effects.format"),
                        new String[]{"{effect}", "{level}"}, new String[]{message.getRawMessage("effects." + splitedEffect[0]), level});

                potionMeta.setDisplayName(TextFormater.sFormatText(name));
                lore.add("&7Click to select.");
                potionMeta.setLore(TextFormater.colorFormatList(lore));
                effectPotion.setItemMeta(potionMeta);
                Icon effectIcon = new Icon(effectPotion);
                effectIcon.addClickAction(new ClickAction() {
                    @Override
                    public void execute(Player player) {
                        beaconData.selectedEffect = effect;
                        beaconHandler.removeBeacon(beaconData.beaconLocation);
                        beaconHandler.addBeacon(beaconData);
                        handler.openMainGUI(player, beaconData);
                    }
                });
                inventoryCreator.setIcon(position, effectIcon);
                slots.add(position);
                position++;
            }
        }

        // NEXT PAGE ITEM
        if(effects.size() > start+max){
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
