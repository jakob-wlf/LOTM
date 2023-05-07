package de.firecreeper82.pathways.impl.fool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlasphemyFool extends FoolPotions {
    String name = "fool";
    String stringColor = "§5";
    ItemStack book = new ItemStack(Material.WRITABLE_BOOK, 1);
    BookMeta meta = (BookMeta) book.getItemMeta();
    HashMap<Integer, ItemStack[]> MainIngredients = new HashMap();
    HashMap<Integer, ItemStack[]> SecondaryIngredients = new HashMap();

    public void setFoolIngr() {
        MainIngredients.put(9, new FoolPotions().getMainIngredients(9));
        SecondaryIngredients.put(9, new FoolPotions().getSupplIngredients(9));
        MainIngredients.put(8, new FoolPotions().getMainIngredients(8));
        SecondaryIngredients.put(8, new FoolPotions().getSupplIngredients(8));
        MainIngredients.put(7, new FoolPotions().getMainIngredients(7));
        SecondaryIngredients.put(7, new FoolPotions().getSupplIngredients(7));
        MainIngredients.put(6, new FoolPotions().getMainIngredients(6));
        SecondaryIngredients.put(6, new FoolPotions().getSupplIngredients(6));
        MainIngredients.put(5, new FoolPotions().getMainIngredients(5));
        SecondaryIngredients.put(5, new FoolPotions().getSupplIngredients(5));
        MainIngredients.put(4, new FoolPotions().getMainIngredients(4));
        SecondaryIngredients.put(4, new FoolPotions().getSupplIngredients(4));
        MainIngredients.put(3, new FoolPotions().getMainIngredients(3));
        SecondaryIngredients.put(3, new FoolPotions().getSupplIngredients(3));
        MainIngredients.put(2, new FoolPotions().getMainIngredients(2));
        SecondaryIngredients.put(2, new FoolPotions().getSupplIngredients(2));
        MainIngredients.put(1, new FoolPotions().getMainIngredients(1));
        SecondaryIngredients.put(1, new FoolPotions().getSupplIngredients(1));


    }

    public void returnFoolBlasphemy() {
        int seq = 9;

        meta.setTitle(stringColor + name);
        HashMap fool_name = de.firecreeper82.pathways.impl.fool.FoolPathway.getNames();

        List<String> main_name = new ArrayList<>();
        List<String> supl_name = new ArrayList<>();

        while (seq != 1) {

            //counter and puts all main ingredients int a var
            ItemStack[] main_ingredients = mainIngredients.get(seq);
            ItemStack[] supl_ingredients = supplementaryIngredients.get(seq);
            for (ItemStack it : main_ingredients) {
                if (it != null) {
                    main_name.add(it.getItemMeta().getDisplayName());
                }
            }
            for (ItemStack it : supl_ingredients) {
                if (it != null) {
                    supl_name.add(it.getItemMeta().getDisplayName());
                }
            }
            StringBuilder pageBuilder = new StringBuilder();

            pageBuilder.append(stringColor + name + fool_name.get(seq) + "\n");
            for (String i : main_name) {
                pageBuilder.append(stringColor + "\n - " + i);
            }
            pageBuilder.append("\n\n");
            for (String i : supl_name) {
                pageBuilder.append(stringColor + "\n - " + i);
            }
            meta.setPage(seq,pageBuilder.toString());
            seq--;
        }
        book.setItemMeta(meta);
    }
}