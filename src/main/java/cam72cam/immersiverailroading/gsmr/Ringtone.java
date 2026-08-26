package cam72cam.immersiverailroading.gsmr;

public enum Ringtone {
    AIPONE("aipone.ogg"),
    BING_BONG("bing-bong.ogg"),
    BOMB_VENEZUELA("bomb_venezuea.ogg"),
    COMPUTER_VIRUS("computer.ogg"),
    DISCORD("dc.ogg"),
    FACEBOOK_RING("facebook.ogg"),
    FNAF("fnaf.ogg"),
    FNAF2("fnaf2.ogg"),
    NOKIA_RING("nokia.ogg"),
    PLANE("plane.ogg"),
    TRUMPET("trumpet.ogg"),
    NORMAL_RING("ring-ring.ogg")
    ;

    String soundFile;

    Ringtone(String soundFile) {
        this.soundFile = "sounds/ringtone/" + soundFile;
    }

    public String getSoundFile() {
        return soundFile;
    }
}
