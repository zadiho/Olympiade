package fr.zadiho.hepickstudio.olympiade.listeners;

import fr.zadiho.hepickstudio.olympiade.game.EGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GeneralEvents implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onFireTick(EntityDamageEvent event){
        if(EGames.getCurrentState().equals(EGames.WAITING)){
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        event.setCancelled(true);
    }
}
