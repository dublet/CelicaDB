package com.dublet.celicadb2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dublet on 15/12/13.
 */
public class Car implements Comparable<Car> {
    public String code;
    public String name;
    public int releaseYear = -1, deceaseYear = -1, generation = -1;
    public final ArrayList<Integer> pictureResources = new ArrayList<Integer>();
    @SuppressLint("UseSparseArrays")
    public final HashMap<Integer, String> bigPictureResources = new HashMap<Integer, String>();

    /* Drivetrain */
    public final String DRIVETRAIN_TRANSMISSION = "transmission", DRIVETRAIN_NUM_GEARS = "gears",
            DRIVETRAIN_DRIVE = "drive", DRIVETRAIN_GR_1 = "gear_ratio_1", DRIVETRAIN_GR_2 = "gear_ratio_2",
            DRIVETRAIN_GR_3 = "gear_ratio_3", DRIVETRAIN_GR_4 = "gear_ratio_4", DRIVETRAIN_GR_5 = "gear_ratio_5",
            DRIVETRAIN_GR_6 = "gear_ratio_6", DRIVETRAIN_GR_R = "gear_ratio_R", DRIVETRAIN_GR_FINAL = "final_drive";
    /* Engine */
    public final String ENGINE_CODE = "code", ENGINE_ASPIRATION = "aspiration",
            ENGINE_FUEL = "fuel", ENGINE_ALTERNATOR = "alternator",
            ENGINE_BATTERY = "battery", ENGINE_DISPLACEMENT = "displacement",
            ENGINE_BORE="bore", ENGINE_STROKE = "stroke", ENGINE_COMPRESSION_RATIO = "compression_ratio",
            ENGINE_NUM_CYLINDERS = "cylinders", ENGINE_NUM_VALVES_PER_CYLINDER = "valves_per_cylinder",
            ENGINE_MAX_POWER = "power_output", ENGINE_MAX_POWER_REVS = "power_revs",
            ENGINE_MAX_TORQUE = "torque_output", ENGINE_MAX_TORQUE_REVS = "torque_revs";
    /* Economy */
    public final String ECO_CITY = "city", ECO_MOTORWAY = "motorway", ECO_OVERALL = "overall";
    /* Measurements */
    public final String MEASURE_LENGTH = "length", MEASURE_WIDTH = "width", MEASURE_HEIGHT = "height",
            MEASURE_WHEEL_BASE = "wheel_base", MEASURE_TW_FRONT = "track_width_front",
            MEASURE_TW_REAR = "track_width_rear", MEASURE_MASS = "mass",
            MEASURE_FUEL_CAP = "fuel_capacity", MEASURE_OIL_CAP = "oil_capacity",
            MEASURE_COOLANT_CAP = "coolant_capacity", MEASURE_CD_VALUE = "drag_coefficient",
            MEASURE_STEERING_ROT = "steering_wheel_rotations";
    /* Performance */
    public final String PERFORMANCE_TOP_SPEED = "top_speed", PERFORMANCE_ZERO_TO_HUNDRED = "acceleration";
    /* Tyres */
    public final String TYRES_RIM_SIZE = "rim_size", TYRES_SIZE = "tyre_size";
    /* Brakes */
    public final String BRAKES_FRONT = "front", BRAKES_REAR = "rear", BRAKES_ADDITIONAL = "additional";
    /* Suspension */
    public final String SUSPENSION_FRONT = "front_mount", SUSPENSION_REAR = "rear_mount",
            SUSPENSION_SHOCKS = "shock_absorbers", SUSPENSION_STABILISERS = "stabilisers";

    final HashMap<String, CorrectableData<Float>> floatValues = new HashMap<String, CorrectableData<Float>>();
    final HashMap<String, CorrectableData<Integer>> intValues = new HashMap<String, CorrectableData<Integer>>();
    final HashMap<String, CorrectableData<String>> stringValues = new HashMap<String, CorrectableData<String>>();

    Car(Element d) {
        NodeList children = d.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!children.item(i).hasChildNodes()) {
                continue;
            }
            String nodeName = children.item(i).getNodeName();
            String nodeValue = children.item(i).getFirstChild().getNodeValue();

            if (nodeName.equalsIgnoreCase("modelcode")) code = nodeValue;
            else if (nodeName.equalsIgnoreCase("name")) name = nodeValue;
            else if (nodeName.equalsIgnoreCase("generation")) { try { generation = Integer.parseInt(nodeValue); } catch (Exception e) { Log.e("", e.getMessage()); } }
            else if (nodeName.equalsIgnoreCase("release_date")) { try { releaseYear = Integer.parseInt(nodeValue);} catch (Exception e) { Log.e("", e.getMessage()); } }
            else if (nodeName.equalsIgnoreCase("decease_date")) { try { deceaseYear = Integer.parseInt(nodeValue);} catch (Exception e) { Log.e("", e.getMessage()); } }

            else if (nodeName.equalsIgnoreCase("picture_resource_name")) {
                try {
                    Context ctx = CarFactory.getInstance().getContext();
                    Resources r = ctx.getResources();
                    pictureResources.add(r.getIdentifier(nodeValue, "drawable", ctx.getPackageName()));
                }
                catch (Exception e) { Log.e("Image", "Found not drawable resource" + nodeValue, e); }
            } else if (nodeName.equalsIgnoreCase("picture_big_resource_name")) {
                try {
                    Context ctx = CarFactory.getInstance().getContext();
                    Resources r = ctx.getResources();
                    int resourceId = r.getIdentifier(nodeValue, "drawable", ctx.getPackageName());
                    NamedNodeMap attributes = children.item(i).getAttributes();
                    String narrative = attributes.getNamedItem("narrative").getNodeValue();
                    bigPictureResources.put(resourceId, narrative);
                }
                catch (Exception e) { Log.e("Image", "Found not drawable resource: " + nodeValue, e); }
            }
        }
        if (code == null) {
            throw new RuntimeException();
        }
        loadEngineData(d);
        loadDrivetrainData(d);
        loadPerformanceData(d);
        loadEconomyData(d);
        loadMeasurementsData(d);
        loadBrakesData(d);
        loadSuspensionData(d);
        loadTyresData(d);
    }

    private void loadEngineData(Element e) {
        String[] stringElements = { ENGINE_CODE, ENGINE_ASPIRATION, ENGINE_FUEL, ENGINE_ALTERNATOR, ENGINE_BATTERY };
        String[] floatElements = { ENGINE_DISPLACEMENT, ENGINE_BORE, ENGINE_STROKE, ENGINE_COMPRESSION_RATIO,
                ENGINE_MAX_POWER , ENGINE_MAX_TORQUE};
        String[] intElements = { ENGINE_NUM_CYLINDERS, ENGINE_NUM_VALVES_PER_CYLINDER,
                ENGINE_MAX_POWER_REVS, ENGINE_MAX_TORQUE_REVS };
        NodeList nl = e.getElementsByTagName("engine");
        readFloatElements(nl, floatElements);
        readStringElements(nl, stringElements);
        readIntElements(nl, intElements);
    }

    private void loadDrivetrainData(Element e) {
        String[] stringElements = { DRIVETRAIN_TRANSMISSION, DRIVETRAIN_DRIVE };
        String[] floatElements = { DRIVETRAIN_GR_1, DRIVETRAIN_GR_2,
                DRIVETRAIN_GR_3, DRIVETRAIN_GR_4, DRIVETRAIN_GR_5,
                DRIVETRAIN_GR_6, DRIVETRAIN_GR_R, DRIVETRAIN_GR_FINAL };
        String[] intElements = { DRIVETRAIN_NUM_GEARS };
        NodeList nl = e.getElementsByTagName("drivetrain");
        readFloatElements(nl, floatElements);
        readStringElements(nl, stringElements);
        readIntElements(nl, intElements);
    }

    private void loadPerformanceData(Element e) {
        String[] elements = { PERFORMANCE_TOP_SPEED, PERFORMANCE_ZERO_TO_HUNDRED };

        NodeList nl = e.getElementsByTagName("performance");
        readFloatElements(nl, elements);
    }

    private void loadEconomyData(Element e) {
        String[] elements = { ECO_CITY, ECO_MOTORWAY, ECO_OVERALL};
        NodeList nl = e.getElementsByTagName("economy");
        readFloatElements(nl, elements);
    }

    private void loadMeasurementsData(Element e) {
        String[] elements = {
                MEASURE_LENGTH, MEASURE_WIDTH, MEASURE_HEIGHT, MEASURE_WHEEL_BASE,
                MEASURE_TW_FRONT, MEASURE_TW_REAR, MEASURE_MASS, MEASURE_FUEL_CAP,
                MEASURE_OIL_CAP, MEASURE_COOLANT_CAP, MEASURE_CD_VALUE, MEASURE_STEERING_ROT };
        NodeList nl = e.getElementsByTagName("measurements");
        readFloatElements(nl, elements);
    }

    private void loadBrakesData(Element e) {
        String[] elements = { BRAKES_FRONT, BRAKES_REAR, BRAKES_ADDITIONAL };
        NodeList nl = e.getElementsByTagName("brakes");
        readStringElements(nl, elements);
    }

    private void loadSuspensionData(Element e) {
        String[] elements = { SUSPENSION_FRONT, SUSPENSION_REAR,
                SUSPENSION_SHOCKS, SUSPENSION_STABILISERS };
        NodeList nl = e.getElementsByTagName("suspension");
        readStringElements(nl, elements);
    }

    private void loadTyresData(Element e) {
        String[] elements = { TYRES_RIM_SIZE, TYRES_SIZE };
        NodeList nl = e.getElementsByTagName("tyres");
        readStringElements(nl, elements);
    }

    private void readFloatElements(NodeList nl, String[] elements) {
        for (String element : elements) {
            floatValues.put(element, new CorrectableData<Float>(element, Float.NaN));
        }

        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();

                for (String element : elements) {
                    if (nodeName.equals(element)) {
                        try {
                            floatValues.get(element).orig = Float.parseFloat(nodeValue);
                        } catch (NumberFormatException exception) {
                            Log.e("Could not parse for code " + code + " element " +
                                    nodeName + " value: " + nodeValue, exception.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void readStringElements(NodeList nl, String[] elements) {
        for (String element : elements) {
            stringValues.put(element, new CorrectableData<String>(element, null));
        }

        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();

                for (String element : elements) {
                    if (nodeName.equals(element)) {
                        stringValues.get(element).orig = nodeValue;
                    }
                }
            }
        }
    }

    private void readIntElements(NodeList nl, String[] elements) {
        for (String element : elements) {
            intValues.put(element, new CorrectableData<Integer>(element, -1));
        }

        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();

                for (String element : elements) {
                    if (nodeName.equals(element)) {
                        try {
                            intValues.get(element).orig = Integer.parseInt(nodeValue);
                        } catch (NumberFormatException exception) {
                            Log.e("Could not parse for code " + code + " element " +
                                    nodeName + " value: " + nodeValue, exception.getMessage());
                        }
                    }
                }
            }
        }
    }

    public String toString() {
        return name + " (" + code + ")";
    }

    @Override
    public int compareTo(Car otherCar) {
        return code.compareTo(otherCar.code);
    }

    public float getAcceleration() {
        if (zero2hundred().getValue().isNaN())
            return 0f;
        return zero2hundred().getValue();
    }

    public float getTopSpeed() {
        if (top_speed().getValue().isNaN())
            return 0f;
        return top_speed().getValue();
    }

    public float getPowerPerWeight() {
        if (!maxPower().getValue().isNaN() && !mass().getValue().isNaN()) {
            return maxPower().getValue() / (mass().getValue() / 1000f);
        }
        return Float.NaN;
    }

    public float getWeightPerTorque() {
        if (!maxTorque().getValue().isNaN() && !mass().getValue().isNaN()) {
            return mass().getValue() / maxTorque().getValue();
        }
        return Float.NaN;
    }

    public String getCorrectionsXML() {
        boolean hasCorrections = false;
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, CorrectableData<Float>> entry : floatValues.entrySet()) {
            String key = entry.getKey();
            CorrectableData<Float> value = entry.getValue();
            if (value.orig != Float.NaN && value.isCorrected()) {
                hasCorrections = true;
                sb.append(value.toXML(key));
            }
        }
        for (Map.Entry<String, CorrectableData<Integer>> entry : intValues.entrySet()) {
            String key = entry.getKey();
            CorrectableData<Integer> value = entry.getValue();
            if (value.orig > -1 && value.isCorrected()) {
                hasCorrections = true;
                sb.append(value.toXML(key));
            }
        }
        for (Map.Entry<String, CorrectableData<String>> entry : stringValues.entrySet()) {
            String key = entry.getKey();
            CorrectableData<String> value = entry.getValue();
            if (value.orig != null && value.isCorrected()) {
                hasCorrections = true;
                sb.append(value.toXML(key));
            }
        }
        if (hasCorrections) {
            String output = "<correction_of_model>";
            output += "<modelcode>" +  code + "</modelcode>";
            output += sb.toString();
            output += "</correction_of_model>";
            return output;
        }
        return "";
    }

    public void correct(String element, String newValue) {
        try {
        if (floatValues.containsKey(element)) {
            floatValues.get(element).setCorrected(Util.parseFloat(newValue));
        } else if (intValues.containsKey(element)) {
            intValues.get(element).setCorrected(Integer.parseInt(newValue));
        } else if (stringValues.containsKey(element)) {
            stringValues.get(element).setCorrected(newValue);
        }
        } catch (NumberFormatException e) {
            int i =0;

        }
        assert(false);
    }

    public void loadCorrection(String element, String orig, String corrected) {
        if (floatValues.containsKey(element)) {
            floatValues.get(element).setCorrected(Util.parseFloat(corrected));
        }  else if (intValues.containsKey(element)) {
            intValues.get(element).setCorrected(Integer.parseInt(corrected));
        } else if (stringValues.containsKey(element)) {
            stringValues.get(element).setCorrected(corrected);
        } else {
            stringValues.put(element, new CorrectableData<String>(element, orig, corrected));
        }
    }

    public List<CorrectableData<String>> getCorrrectionsList() {
        List<CorrectableData<String>> list = new ArrayList<CorrectableData<String>>();
        for (CorrectableData<String> stringData : stringValues.values()) {
            if (stringData.orig != null && stringData.isCorrected())
                list.add(stringData);
        }
        for (CorrectableData<Integer> intData : intValues.values()) {
            if (intData.isCorrected())
                list.add(new CorrectableData<String>(intData.tag, "" + intData.orig, "" + intData.corrected));
        }
        for (CorrectableData<Float> floatData : floatValues.values()) {
            if (floatData.orig != Float.NaN && floatData.isCorrected())
                list.add(new CorrectableData<String>(floatData.tag, "" + floatData.orig, "" + floatData.corrected));
        }
        return list;
    }

    public boolean isFloat(String element) { return floatValues.containsKey(element); }
    public boolean isInteger(String element) { return intValues.containsKey(element); }
    public boolean isString(String element) { return stringValues.containsKey(element); }

    public void clearCorrections() {
        for (Map.Entry<String, CorrectableData<Float>> entry : floatValues.entrySet()) {
            CorrectableData<Float> value = entry.getValue();
            value.removeCorrection();
        }
        for (Map.Entry<String, CorrectableData<Integer>> entry : intValues.entrySet()) {
            CorrectableData<Integer> value = entry.getValue();
            value.removeCorrection();
        }
        for (Map.Entry<String, CorrectableData<String>> entry : stringValues.entrySet()) {
            CorrectableData<String> value = entry.getValue();
            value.removeCorrection();
        }
    }

    public CorrectableData<String> engineCode() { return stringValues.get(ENGINE_CODE); }
    public CorrectableData<String> aspiration() { return stringValues.get(ENGINE_ASPIRATION); }
    public CorrectableData<String> fuel() { return stringValues.get(ENGINE_FUEL); }
    public CorrectableData<String> alternator() { return stringValues.get(ENGINE_ALTERNATOR); }
    public CorrectableData<String> battery() { return stringValues.get(ENGINE_BATTERY); }
    public CorrectableData<Float> displacement() { return floatValues.get(ENGINE_DISPLACEMENT); }
    public CorrectableData<Float> bore() { return floatValues.get(ENGINE_BORE); }
    public CorrectableData<Float> stroke() { return floatValues.get(ENGINE_STROKE); }
    public CorrectableData<Float> compressionRatio() { return floatValues.get(ENGINE_COMPRESSION_RATIO); }

    public CorrectableData<Integer> numCylinders() { return intValues.get(ENGINE_NUM_CYLINDERS); }
    public CorrectableData<Integer> numValesPerCylinder() { return intValues.get(ENGINE_NUM_VALVES_PER_CYLINDER); }

    public CorrectableData<Float> eco_city() { return floatValues.get(ECO_CITY); }
    public CorrectableData<Float> eco_motorway() { return floatValues.get(ECO_MOTORWAY); }
    public CorrectableData<Float> eco_overall() { return floatValues.get(ECO_OVERALL); }

    public CorrectableData<Float> top_speed() { return floatValues.get(PERFORMANCE_TOP_SPEED); }
    public CorrectableData<Float> zero2hundred() { return floatValues.get(PERFORMANCE_ZERO_TO_HUNDRED); }

    public CorrectableData<Float> length() { return floatValues.get(MEASURE_LENGTH); }
    public CorrectableData<Float> width() { return floatValues.get(MEASURE_WIDTH); }
    public CorrectableData<Float> height() { return floatValues.get(MEASURE_HEIGHT); }
    public CorrectableData<Float> wheel_base() { return floatValues.get(MEASURE_WHEEL_BASE); }
    public CorrectableData<Float> track_width_front() { return floatValues.get(MEASURE_TW_FRONT); }
    public CorrectableData<Float> track_width_rear() { return floatValues.get(MEASURE_TW_REAR); }
    public CorrectableData<Float> mass() { return floatValues.get(MEASURE_MASS); }
    public CorrectableData<Float> fuel_capacity() { return floatValues.get(MEASURE_FUEL_CAP); }
    public CorrectableData<Float> oil_capacity() { return floatValues.get(MEASURE_OIL_CAP); }
    public CorrectableData<Float> coolant_capacity() { return floatValues.get(MEASURE_COOLANT_CAP); }
    public CorrectableData<Float> drag_coefficient() { return floatValues.get(MEASURE_CD_VALUE); }
    public CorrectableData<Float> steering_wheel_rotations() { return floatValues.get(MEASURE_STEERING_ROT); }

    public CorrectableData<String> rim_size() { return stringValues.get(TYRES_RIM_SIZE); }
    public CorrectableData<String> tyre_size() { return stringValues.get(TYRES_SIZE); }

    public CorrectableData<String> brakes_additional() { return stringValues.get(BRAKES_ADDITIONAL); }
    public CorrectableData<String> brakes_front() { return stringValues.get(BRAKES_FRONT); }
    public CorrectableData<String> brakes_rear() { return stringValues.get(BRAKES_REAR); }

    public CorrectableData<String> suspension_front_mount() { return stringValues.get(SUSPENSION_FRONT); }
    public CorrectableData<String> suspension_rear_mount() { return stringValues.get(SUSPENSION_REAR); }
    public CorrectableData<String> suspension_shock_absorbers() { return stringValues.get(SUSPENSION_SHOCKS); }
    public CorrectableData<String> suspension_stabilisers() { return stringValues.get(SUSPENSION_STABILISERS); }

    public CorrectableData<Float> maxPower() { return floatValues.get(ENGINE_MAX_POWER); }
    public CorrectableData<Integer> maxPowerRevs() { return intValues.get(ENGINE_MAX_POWER_REVS); }
    public CorrectableData<Float> maxTorque() { return floatValues.get(ENGINE_MAX_TORQUE); }
    public CorrectableData<Integer> maxTorqueRevs() { return intValues.get(ENGINE_MAX_TORQUE_REVS); }

    public CorrectableData<String> transmission () { return stringValues.get(DRIVETRAIN_TRANSMISSION); }
    public CorrectableData<Integer> gears() { return intValues.get(DRIVETRAIN_NUM_GEARS); }
    public CorrectableData<String> drive() { return stringValues.get(DRIVETRAIN_DRIVE); }
    public CorrectableData<Float> gear_ratio_1() { return floatValues.get(DRIVETRAIN_GR_1); }
    public CorrectableData<Float> gear_ratio_2() { return floatValues.get(DRIVETRAIN_GR_2); }
    public CorrectableData<Float> gear_ratio_3() { return floatValues.get(DRIVETRAIN_GR_3); }
    public CorrectableData<Float> gear_ratio_4() { return floatValues.get(DRIVETRAIN_GR_4); }
    public CorrectableData<Float> gear_ratio_5() { return floatValues.get(DRIVETRAIN_GR_5); }
    public CorrectableData<Float> gear_ratio_6() { return floatValues.get(DRIVETRAIN_GR_6); }
    public CorrectableData<Float> gear_ratio_R() { return floatValues.get(DRIVETRAIN_GR_R); }
    public CorrectableData<Float> final_drive() { return floatValues.get(DRIVETRAIN_GR_FINAL); }
}


