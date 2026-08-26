package cam72cam.immersiverailroading.gsmr;

import cam72cam.mod.text.TextUtil;

public enum GSMRText {
    CALL_MESSAGE("gsmr.call_message"),
    ACCEPT("gsmr.accept"),
    REJECT("gsmr.reject");

    private final String value;

    GSMRText(String value) {
        this.value = value;
    }

    public String getRaw() {
        return "immersiverailroading:" + value;
    }
    public String getText() {
        return TextUtil.translate(getRaw());
    }
}
