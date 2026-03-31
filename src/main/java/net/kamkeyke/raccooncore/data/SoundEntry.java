package net.kamkeyke.raccooncore.data;

public record SoundEntry(String soundName, String basePathAndName, int variations, String[] customPathsAndNames) {

    public static SoundEntry single(String soundName, String basePathAndName){
        return varied(soundName, basePathAndName, 0);
    }

    public static SoundEntry varied(String soundName, String basePathAndName, int count){
        return new SoundEntry(soundName, basePathAndName, count, null);
    }

    public static SoundEntry customPaths(String soundName, String... customPathsAndNames){
        return new SoundEntry(soundName, null, 0, customPathsAndNames);
    }
}
