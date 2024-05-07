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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheatListener implements Listener {

    /**
     * We want to run the Reach check asynchronously as brute-forcing interpolation might take some time.
     * We also want to make sure to use enough threads.
     * This is also how Verus handles their checks which is why they never impact performance.
     */
    private static final ExecutorService ASYNC_CHECK_EXECUTOR = Executors.newFixedThreadPool(1337);

    private final double MAX_REACH = 3.000000001D;

    @EventHandler
    public void onReach(final EntityDamageByEntityEvent e) {

        // Make sure to run the reach check asynchronously
        ASYNC_CHECK_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
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
        });
    }

    private void flag(Player player, String message) {
        Bukkit.broadcastMessage(player.getName() + " IS CHEATING: " + message);
    }
}