package net.expvp.core.commands.general;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.google.common.collect.Sets;

import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;

/**
 * Class used for handling command /invsee
 * 
 * @author NullUser
 * @see AccountCommand
 * @see Listener
 */
public class CommandInvsee extends AccountCommand implements Listener {

	private final Set<UUID> invseeing;

	public CommandInvsee(NullContainer container) {
		super(container, "invsee", "core.invsee", "/invsee <player>", 1);
		invseeing = Sets.newHashSet();
		Bukkit.getPluginManager().registerEvents(this, container.getPlugin());
	}

	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		OnlinePlayerAccount target = checkAccount(account.getPlayer(), args[0], true);
		if (target == null || target instanceof WildcardPlayerAccount || target instanceof ConsoleAccount) {
			getContainer().getMessages().sendMessage(account.getPlayer(), null, "gen.account-offline", args[0]);
			return;
		}
		invseeing.add(account.getUUID());
		account.getPlayer().openInventory(target.getPlayer().getInventory());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		UUID id = event.getWhoClicked().getUniqueId();
		if (invseeing.contains(id) && !event.getWhoClicked().hasPermission("core.invsee.modify")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		UUID id = event.getWhoClicked().getUniqueId();
		if (invseeing.contains(id) && !event.getWhoClicked().hasPermission("core.invsee.modify")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		UUID id = event.getPlayer().getUniqueId();
		if (invseeing.contains(id)) {
			invseeing.remove(id);
		}
	}

}
