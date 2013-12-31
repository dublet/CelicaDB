package com.dublet.celicadb2;

/**
 * Created by dublet on 30/12/13.
 */
public class Converter {
    private static final Float lPer100kmToMphUsMultiplier = 235.2146f;
    private static final Float lPer100kmToMphUkMultiplier = 282.481f;

    private static final Float metresToFeetMultiplier =  0.3048f;
    private static final Float metresToRodMultiplier = 5.0292f;

    private static final Float kmphToMphMultiplier =  1.609344f;
    private static final Float kmphToFurlongPerFortnightMultiplier = 1670.24576f;

    private static final Float litresToGalUsMultiplier =  3.785411784f;
    private static final Float litresToGalUkMultiplier = 4.54609f;
    private static final Float litresToHogsheadMultiplier = 238.7f;

    private static final Float kgToPoundMultiplier = 0.45359237f;
    private static final Float kgToBagsOfCementMultiplier = 50.802345f;


    public static Float lPer100kmToMpgUS(Float lPer100Km) {
        return  lPer100kmToMphUsMultiplier / lPer100Km;
    }

    public static Float lPer100kmToMpgUK(Float lPer100Km) {
        return  lPer100kmToMphUkMultiplier / lPer100Km;
    }

    public static Float lPer100kmToRodsPerHogstead(Float lPer100Km) {
        return lPer100kmToMpgUS(lPer100Km) / 1 * (63f / 1f) * (320f / 1f);
    }

    public static Float mpgUkTolPer100km(Float mpgUk) {
        return lPer100kmToMphUkMultiplier / mpgUk;
    }

    public static Float mpgUSTolPer100km(Float mpgUs) {
        return lPer100kmToMphUsMultiplier / mpgUs;
    }

    public static Float metresToFeet(Float metres) {
        return metres / metresToFeetMultiplier;
    }

    public static Float metresToRods(Float metres) {
        return metres / metresToRodMultiplier;
    }

    public static Float feetToMetres(Float feet) {
        return feet * metresToFeetMultiplier;
    }

    public static Float rodsToMetres(Float rods) {
        return rods  * metresToRodMultiplier;
    }

    public static Float kmphToMph(Float kmph) {
        return kmph / kmphToMphMultiplier;
    }

    public static Float kmphToFurlongPerFortnight(Float kmph) {
        return kmph / kmphToFurlongPerFortnightMultiplier;
    }

    public static Float mphToKmph(Float mph) {
        return mph * kmphToMphMultiplier;
    }

    public static Float furlongPerFortnightToKmph(Float furlongPerFortnight) {
        return furlongPerFortnight * kmphToFurlongPerFortnightMultiplier;
    }

    public static Float litresToGalUs(Float litres) {
        return litres  / litresToGalUsMultiplier;
    }

    public static Float litresToGalUK(Float litres) {
        return litres  / litresToGalUkMultiplier;
    }

    public static Float litresToHogshead(Float litres) {
        return litres  / litresToHogsheadMultiplier;
    }

    public static Float galUsToLitres(Float litres) {
        return litres  * litresToGalUsMultiplier;
    }

    public static Float galUKToLitres(Float litres) {
        return litres  * litresToGalUkMultiplier;
    }

    public static Float hogsheadToLitres(Float litres) {
        return litres  * litresToHogsheadMultiplier;
    }

    public static Float kgToPounds(Float kgs) {
        return kgs  / kgToPoundMultiplier;
    }

    public static Float kgToBagsOfCement(Float kgs) {
        return kgs  / kgToBagsOfCementMultiplier;
    }

    public static Float poundsToKg(Float pounds) {
        return pounds  * kgToPoundMultiplier;
    }

    public static Float bagsOfCementToKg(Float bagsOfCement) {
        return bagsOfCement  * kgToBagsOfCementMultiplier;
    }
}
