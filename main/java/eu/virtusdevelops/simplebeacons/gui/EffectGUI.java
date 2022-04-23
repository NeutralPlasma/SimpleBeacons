package eu.virtusdevelops.simplebeacons.gui;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.Paginator;
import eu.virtusdevelops.virtuscore.utils.ItemUtils;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EffectGUI {
    private Paginator paginator;
    private Player player;
    private BeaconData beaconData;

    private SimpleBeacons plugin;
    private BeaconHandler beaconHandler;
    private MessagesHandler message;


    public EffectGUI(Player player, BeaconData beaconData, SimpleBeacons plugin, BeaconHandler beaconHandler, MessagesHandler message){
        this.player = player;
        this.beaconData = beaconData;
        this.plugin = plugin;
        this.beaconHandler = beaconHandler;
        this.message = message;


        paginator = new Paginator(player, Collections.emptyList(), List.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34), TextUtils.colorFormat("&8[&bEffects menu&8]"), 45);

        load();
    }

    private void load(){
        refresh();
        paginator.page();
    }

    private void refresh(){
        List<Icon> icons = new ArrayList<>();

        for(String effect : beaconHandler.getEffects(beaconData)){
            String[] splitedEffect = effect.split(":");
            if(!player.hasPermission("simplebeacons.effect." + splitedEffect[0])){
                continue;
            }

            ItemStack item = new ItemStack(Material.POTION);
            String[] color = message.getRawMessage("effects_colors." + splitedEffect[0]).split(":");
            String level = "1";
            if(splitedEffect.length > 1){
                level = splitedEffect[1];
            }

            String name = message.formatString(message.getRawMessage("effects.format"),
                    new String[]{"{effect}", "{level}"}, new String[]{message.getRawMessage("effects." + splitedEffect[0]), level});

            item = ItemUtils.setName(item, TextUtils.colorFormat(name));
            List<String> lore = new ArrayList<>();
            lore.add("&7Click to select.");
            item = ItemUtils.setLore(item, TextUtils.colorFormatList(lore));

            ItemMeta meta = item.getItemMeta();
            if(meta instanceof PotionMeta){
                ((PotionMeta) meta).setColor(Color.fromRGB(Integer.valueOf(color[0]), Integer.valueOf(color[1]), Integer.valueOf(color[2])));

            }

            if(beaconData.getSelectedEffect().equals(effect)){
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
            }
            item.setItemMeta(meta);

            Icon icon = new Icon(item);
            icon.addClickAction((player1) -> {
                beaconData.setSelectedEffect(effect);
                beaconHandler.updateBeacon(beaconData);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);

                load();

            });
            icons.add(icon);

        }

        paginator.setIcons(icons);
    }

}
