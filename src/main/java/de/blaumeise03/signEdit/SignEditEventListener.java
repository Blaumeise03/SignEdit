/*
 *     Copyright (C) 2019  Blaumeise03 - bluegame61@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.blaumeise03.signEdit;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_14_R1.TileEntitySign;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Field;

public class SignEditEventListener implements Listener {

    @EventHandler
    public void onSneakClick(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().isSneaking() && e.getClickedBlock() != null)
            if(e.getClickedBlock().getState() instanceof Sign){
                //Sign si = (Sign) e.getClickedBlock().getState();
                //si.setEditable(true);
                //si.update();
                Player pl = e.getPlayer();
                CraftPlayer p = (CraftPlayer) pl;

                SignEdit.plugin.getLogger().info("Player: " + p.getName() + " is starting to edit a sign at " + e.getClickedBlock().getX() + "|" + e.getClickedBlock().getY() + "|" + e.getClickedBlock().getZ() + " in world " + e.getClickedBlock().getWorld().getName() + "!");

                Location l = e.getClickedBlock().getLocation();
                TileEntitySign t = (TileEntitySign) ((CraftWorld) l.getWorld()).getHandle().getTileEntity(new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                //t.a(((CraftPlayer)e.getPlayer()).getHandle());
                t.isEditable = true;
                try {
                    assert t != null;
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
