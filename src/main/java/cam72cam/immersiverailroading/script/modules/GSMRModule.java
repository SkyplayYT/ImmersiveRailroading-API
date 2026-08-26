package cam72cam.immersiverailroading.script.modules;

import cam72cam.immersiverailroading.entity.EntityScriptableRollingStock;
import cam72cam.immersiverailroading.gsmr.*;
import cam72cam.immersiverailroading.script.LuaFunction;
import cam72cam.immersiverailroading.script.LuaModule;
import org.luaj.vm2.LuaValue;

public class GSMRModule implements LuaModule {

    private final EntityScriptableRollingStock stock;

    public GSMRModule(EntityScriptableRollingStock stock) {
        this.stock = stock;
    }

    //Register/Unregister

    /**
     * @param number The phone number
     * @return False, if phone number or uuid already exists. Otherwise, it's true
     */
    @LuaFunction(module = "gsmr")
    public LuaValue registerPhoneNumber(LuaValue number) {
        TrainManager.registerTrain(stock.getUUID(), stock);
        return LuaValue.valueOf(PhoneNumberManager.registerPhoneNumber(number.toint(), stock.getUUID(), CallType.TRAIN));
    }

    @LuaFunction(module = "gsmr")
    public LuaValue unregisterPhoneNumber() {
        TrainManager.unregisterTrain(stock.getUUID());
        return LuaValue.valueOf(PhoneNumberManager.unregisterPhoneNumber(stock.getUUID()));
    }

    //Call Methods

    /**
     * @param number The phone number
     * @return Returns the state of the method. See {@link CallErrorType} for the possible outcomes.
     */
    @LuaFunction(module = "gsmr")
    public LuaValue call(LuaValue number) {
        int phoneNumber = number.toint();

        return LuaValue.valueOf(CallManager.call(stock.getUUID(), phoneNumber).name());
    }

    @LuaFunction(module = "gsmr")
    public void acceptCall() {
        CallManager.acceptCall(stock.getUUID());
    }

    @LuaFunction(module = "gsmr")
    public void declineCall() {
        CallManager.declineCall(stock.getUUID());
    }

    @LuaFunction(module = "gsmr")
    public void hangUp() {
        CallManager.hangUp(stock.getUUID());
    }

    @LuaFunction(module = "gsmr")
    public void reset() {
        TrainManager.removeTrainPlayerLink(stock.getUUID());
        PhoneNumberManager.unregisterPhoneNumber(stock.getUUID());
    }

    /*
     *  Events
     */

    public static void ringTrain(EntityScriptableRollingStock stock, int phoneNumber) {
        if(stock.getWorld().isServer)
            stock.triggerEvent("ringTrain", LuaValue.valueOf(phoneNumber));
    }

    public static void callAccepted(EntityScriptableRollingStock stock) {
        if(stock.getWorld().isServer)
            stock.triggerEvent("callAccepted");
    }

    public static void callDeclined(EntityScriptableRollingStock stock) {
        if(stock.getWorld().isServer)
            stock.triggerEvent("callDeclined");
    }

    public static void callEnded(EntityScriptableRollingStock stock) {
        if(stock.getWorld().isServer)
            stock.triggerEvent("callEnded");
    }

    //Triggered, when you try to call someone and something unexpected happens
    public static void callFailed(EntityScriptableRollingStock stock) {
        if(stock.getWorld().isServer)
            stock.triggerEvent("callFailed");
    }

}
