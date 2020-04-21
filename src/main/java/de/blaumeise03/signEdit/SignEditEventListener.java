/*
 * Copyright (c) 2020 Blaumeise03
 */

package de.blaumeise03.signEdit;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_14_R1.TileEntitySign;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Field;

public class SignEditEventListener implements Listener {

    private static String convertColor(String string) {
        string = string.replaceAll("&4", "§4");
        string = string.replaceAll("&c", "§c");
        string = string.replaceAll("&6", "§6");
        string = string.replaceAll("&e", "§e");
        string = string.replaceAll("&2", "§2");
        string = string.replaceAll("&a", "§a");
        string = string.replaceAll("&b", "§b");
        string = string.replaceAll("&3", "§3");
        string = string.replaceAll("&1", "§1");
        string = string.replaceAll("&9", "§9");
        string = string.replaceAll("&d", "§d");
        string = string.replaceAll("&5", "§5");
        string = string.replaceAll("&f", "§f");
        string = string.replaceAll("&7", "§7");
        string = string.replaceAll("&8", "§8");
        string = string.replaceAll("&0", "§0");
        string = string.replaceAll("&l", "§l");
        string = string.replaceAll("&m", "§m");
        string = string.replaceAll("&n", "§n");
        string = string.replaceAll("&o", "§o");
        string = string.replaceAll("&r", "§r");
        string = string.replaceAll("&k", "§k");
        return string;
    }

    private static String reConvertColor(String string) {
        string = string.replaceAll("§4", "&4");
        string = string.replaceAll("§c", "&c");
        string = string.replaceAll("§6", "&6");
        string = string.replaceAll("§e", "&e");
        string = string.replaceAll("§2", "&2");
        string = string.replaceAll("§a", "&a");
        string = string.replaceAll("§b", "&b");
        string = string.replaceAll("§3", "&3");
        string = string.replaceAll("§1", "&1");
        string = string.replaceAll("§9", "&9");
        string = string.replaceAll("§d", "&d");
        string = string.replaceAll("§5", "&5");
        string = string.replaceAll("§f", "&f");
        string = string.replaceAll("§7", "&7");
        string = string.replaceAll("§8", "&8");
        string = string.replaceAll("§0", "&0");
        string = string.replaceAll("§l", "&l");
        string = string.replaceAll("§m", "&m");
        string = string.replaceAll("§n", "&n");
        string = string.replaceAll("§o", "&o");
        string = string.replaceAll("§r", "&r");
        string = string.replaceAll("§k", "&k");
        return string;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSneakClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().isSneaking() && e.getClickedBlock() != null && e.getPlayer().hasPermission("signEdit.edit"))
            if (e.getClickedBlock().getState() instanceof Sign) {
                //Sign si = (Sign) e.getClickedBlock().getState();
                //si.setEditable(true);
                //si.update();
                Player pl = e.getPlayer();
                CraftPlayer p = (CraftPlayer) pl;

                SignEdit.plugin.getLogger().info("Player: " + p.getName() + " is starting to edit a sign at " + e.getClickedBlock().getX() + "|" + e.getClickedBlock().getY() + "|" + e.getClickedBlock().getZ() + " in world " + e.getClickedBlock().getWorld().getName() + "!");

                Location l = e.getClickedBlock().getLocation();

                //t.a(((CraftPlayer)e.getPlayer()).getHandle());
                Sign sign = (Sign) e.getClickedBlock().getState();
                for (int i = 0; i < sign.getLines().length; i++) {
                    String line = sign.getLine(i);
                    line = ChatColor.stripColor(line);
                    sign.setLine(i, line);
                }
                sign.update();
                TileEntitySign t = (TileEntitySign) ((CraftWorld) l.getWorld()).getHandle().getTileEntity(new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                assert t != null;
                t.isEditable = true;
                try {
                    Field f = t.getClass().getDeclaredField("j");
                    boolean access = f.isAccessible();
                    f.setAccessible(true);
                    t.a((EntityHuman) p.getHandle());
                    t.update();
                    f.setAccessible(access);
                } catch (NoSuchFieldException ex) {
                    ex.printStackTrace();
                }
                PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(BlockPosition.PooledBlockPosition.d(l.getX(),l.getY(),l.getZ()));
                ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
            }
    }
}
