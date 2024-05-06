package me.nik.hackprotectorx.listeners;

import me.nik.hackprotectorx.utils.InterpolationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CheatListener implements Listener {

    private final double MAX_REACH = 3.000000001D;

    @EventHandler
    public void onReach(EntityDamageByEntityEvent e) {

        Player player = (Player) e.getDamager();

        /*
        Before running the check, ensure that the player is not taking a shit,
        as this could easily false flag our advanced prediction methods
        https://bugs.mojang.com/browse/MC-225268
         */
        try {
            final Method takingAShitField = Player.class.getMethod("isTakingAShit");
            takingAShitField.setAccessible(true);

            final boolean isTakingAShit = (boolean) takingAShitField.invoke(player);
            if (isTakingAShit) return;
        } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            // The player probably isn't taking a shit, continue freely.
        }

        Entity target = e.getEntity();

        final double distance = player.getLocation().distance(target.getLocation());

        final byte[] bruteforcedDistance = InterpolationUtil.bruteforceInterpolation(distance);

        if (bruteforcedDistance.length > MAX_REACH) {
            for (int i = 0; i < 5000; i++) {
                flag(player, " REACH = " + Arrays.toString(bruteforcedDistance));
            }
        }
    }

    private void flag(Player player, String message) {
        Bukkit.broadcastMessage(player.getName() + " IS CHEATING: " + message);
    }
}