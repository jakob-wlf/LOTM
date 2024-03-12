package dev.ua.ikeepcalm.mystical.pathways.sun;

import cz.foresttech.api.ColorAPI;
import dev.ua.ikeepcalm.mystical.parents.Pathway;
import dev.ua.ikeepcalm.utils.LocalizationUtil;

import java.util.HashMap;
import java.util.UUID;

public class SunPathway extends Pathway {

    public SunPathway(UUID uuid, int optionalSequence, int pathwayInt) {
        super(uuid, optionalSequence, pathwayInt);
    }

    @Override
    public void init() {
        sequence = new SunSequence(this, optionalSequence);
        name = ColorAPI.colorize(LocalizationUtil.getLocalizedString("sun", "color") + LocalizationUtil.getLocalizedString("sun", "name"));
        stringColor = LocalizationUtil.getLocalizedString("sun", "color");
        nameNormalized = "sun";
    }

    @Override
    public void initItems() {
        items = new SunItems(getPathway());
    }

    public static HashMap<Integer, String> getNames() {
        HashMap<Integer, String> names;
        names = new HashMap<>();
        names.put(9, LocalizationUtil.getLocalizedString("sun", "sequences","9"));
        names.put(8, LocalizationUtil.getLocalizedString("sun", "sequences","8"));
        names.put(7, LocalizationUtil.getLocalizedString("sun", "sequences","7"));
        names.put(6, LocalizationUtil.getLocalizedString("sun", "sequences","6"));
        names.put(5, LocalizationUtil.getLocalizedString("sun", "sequences","5"));
        names.put(4, LocalizationUtil.getLocalizedString("sun", "sequences","4"));
        names.put(3, LocalizationUtil.getLocalizedString("sun", "sequences","3"));
        names.put(2, LocalizationUtil.getLocalizedString("sun", "sequences","2"));
        names.put(1, LocalizationUtil.getLocalizedString("sun", "sequences","1"));
        return names;
    }

}
