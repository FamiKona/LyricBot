package lyric.commands;

import java.util.Random;

public class RanditList {
    private final static Random rand = new Random();

    public static String getRandomSub() {
        return subs[rand.nextInt(subs.length)];
    }

    private final static String[] subs = {
            "forwardsfromgrandma",
            "mememan1440p",
            "deepfriedmemes",
            "facepalm",
            "surrealmemes",
            "4PanelCringe",
            "templeofthephil",
            "FloridaMan",
            "BlackPeopleTwitter",
            "ChurchofGoomy",
            "lewronggeneration",
            "ledootgeneration",
            "obamacam"
    };
}
