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
import java.util.Map;

/**
 * Created by dublet on 15/12/13.
 */
public class Car implements Comparable<Car> {
    public String code;
    public String name;
    public int releaseYear = -1, deceaseYear = -1, generation = -1;
    public ArrayList<Integer> pictureResources = new ArrayList<Integer>();
    @SuppressLint("UseSparseArrays")
    public HashMap<Integer, String> bigPictureResources = new HashMap<Integer, String>();
    /* Engine info */
    public String engineCode;
    public String aspiration;
    public Integer numCylinders = 1, numValesPerCylinder = -1, displacement = -1;
    public Float bore = Float.NaN, stroke = Float.NaN, compressionRatio = Float.NaN;
    public Float maxPower = Float.NaN;
    public Integer maxPowerRevs = -1;
    public Float maxTorque = Float.NaN;
    public Integer maxTorqueRevs = -1;
    public String fuel;
    public String alternator;
    public String battery;

    /* Drivetrain */
    public String transmission = "";
    public Integer gears = -1;
    public String drive = "";
    public Float gear_ratio_1 = Float.NaN, gear_ratio_2 = Float.NaN,
        gear_ratio_3 = Float.NaN, gear_ratio_4 = Float.NaN, gear_ratio_5 = Float.NaN,
        gear_ratio_6 = Float.NaN, gear_ratio_R = Float.NaN, final_drive = Float.NaN;
    
    /* Brakes */
    public String brakes_additional = null, brakes_front = null, brakes_rear = null;
    /* Tyres */
    public String rim_size = null, tyre_size = null;
    /* Suspension */
    public String suspension_front_mount = null, suspension_rear_mount = null, suspension_shock_absorbers = null, suspension_stabilisers = null;
    /* Economy */
    public String ECO_CITY = "city", ECO_MOTORWAY = "motorway", ECO_OVERALL = "overall";
    /* Measurements */
    public String MEASURE_LENGTH = "length", MEASURE_WIDTH = "width", MEASURE_HEIGHT = "height",
            MEASURE_WHEEL_BASE = "wheel_base", MEASURE_TW_FRONT = "track_width_front",
            MEASURE_TW_REAR = "track_width_rear", MEASURE_MASS = "mass",
            MEASURE_FUEL_CAP = "fuel_capacity", MEASURE_OIL_CAP = "oil_capacity",
            MEASURE_COOLANT_CAP = "coolant_capacity", MEASURE_CD_VALUE = "drag_coefficient",
            MEASURE_STEERING_ROT = "steering_wheel_rotations";
    /* Performance */
    public String PERFORMANCE_TOP_SPEED = "top_speed", PERFORMANCE_ZERO_TO_HUNDRED = "acceleration";

    HashMap<String, CorrectableData<Float>> floatValues = new HashMap<String, CorrectableData<Float>>();
    HashMap<String, CorrectableData<Integer>> intValues = new HashMap<String, CorrectableData<Integer>>();
    HashMap<String, CorrectableData<String>> stringValues = new HashMap<String, CorrectableData<String>>();

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
        NodeList nl = e.getElementsByTagName("engine");
        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();
        
                if (nodeName.equalsIgnoreCase("code")) engineCode = nodeValue;
                else if (nodeName.equalsIgnoreCase("aspiration")) aspiration = nodeValue;
                else if (nodeName.equalsIgnoreCase("cylinders")) numCylinders = Integer.parseInt(nodeValue);
                else if (nodeName.equalsIgnoreCase("valves_per_cylinder")) numValesPerCylinder = Integer.parseInt(nodeValue);
                else if (nodeName.equalsIgnoreCase("displacement")) displacement = Integer.parseInt(nodeValue);
                else if (nodeName.equalsIgnoreCase("bore")) bore =  Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("stroke")) stroke = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("compression_ratio")) compressionRatio = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("power_output")) maxPower = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("power_revs")) maxPowerRevs = Integer.parseInt(nodeValue);
                else if (nodeName.equalsIgnoreCase("torque_output")) maxTorque = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("torque_revs")) maxTorqueRevs = Integer.parseInt(nodeValue);
                else if (nodeName.equalsIgnoreCase("fuel")) fuel = nodeValue;
                else if (nodeName.equalsIgnoreCase("alternator")) alternator = nodeValue;
                else if (nodeName.equalsIgnoreCase("battery")) battery = nodeValue;
            }
        }
    }

    private void loadDrivetrainData(Element e) {
        NodeList nl = e.getElementsByTagName("drivetrain");
        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();

                if (nodeName.equalsIgnoreCase("transmission")) transmission = nodeValue;
                else if (nodeName.equalsIgnoreCase("gears")) gears = Integer.parseInt(nodeValue);
                else if (nodeName.equalsIgnoreCase("drive")) drive = nodeValue;
                else if (nodeName.equalsIgnoreCase("gear_ratio_1")) gear_ratio_1 = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("gear_ratio_2")) gear_ratio_2 = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("gear_ratio_3")) gear_ratio_3 = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("gear_ratio_4")) gear_ratio_4 = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("gear_ratio_5")) gear_ratio_5 = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("gear_ratio_6")) gear_ratio_6 = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("gear_ratio_R")) gear_ratio_R = Float.parseFloat(nodeValue);
                else if (nodeName.equalsIgnoreCase("final_drive")) final_drive = Float.parseFloat(nodeValue);
            }
        }
    }

    private void loadPerformanceData(Element e) {
        String[] elements = { PERFORMANCE_TOP_SPEED, PERFORMANCE_ZERO_TO_HUNDRED };
        for (String element : elements) {
            floatValues.put(element, new CorrectableData<Float>(element, Float.NaN));
        }
        NodeList nl = e.getElementsByTagName("performance");
        readFloatElements(nl, elements);
    }

    private void loadEconomyData(Element e) {
        String[] elements = { ECO_CITY, ECO_MOTORWAY, ECO_OVERALL};
        for (String element : elements) {
            floatValues.put(element, new CorrectableData<Float>(element, Float.NaN));
        }

        NodeList nl = e.getElementsByTagName("economy");
        readFloatElements(nl, elements);
    }

    private void loadMeasurementsData(Element e) {
        String[] elements = {
                MEASURE_LENGTH, MEASURE_WIDTH, MEASURE_HEIGHT, MEASURE_WHEEL_BASE,
                MEASURE_TW_FRONT, MEASURE_TW_REAR, MEASURE_MASS, MEASURE_FUEL_CAP,
                MEASURE_OIL_CAP, MEASURE_COOLANT_CAP, MEASURE_CD_VALUE, MEASURE_STEERING_ROT };
        for (String element : elements) {
            floatValues.put(element, new CorrectableData<Float>(element, Float.NaN));
        }
        NodeList nl = e.getElementsByTagName("measurements");
        readFloatElements(nl, elements);
    }

    private void loadBrakesData(Element e) {
        NodeList nl = e.getElementsByTagName("brakes");
        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();

                if (nodeName.equalsIgnoreCase("additional")) brakes_additional = nodeValue;
                else if (nodeName.equalsIgnoreCase("front")) brakes_front = nodeValue;
                else if (nodeName.equalsIgnoreCase("rear")) brakes_rear = nodeValue;
            }
        }
    }

    private void loadSuspensionData(Element e) {
        NodeList nl = e.getElementsByTagName("suspension");
        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();

                if (nodeName.equalsIgnoreCase("front_mount")) suspension_front_mount = nodeValue;
                else if (nodeName.equalsIgnoreCase("rear_mount")) suspension_rear_mount = nodeValue;
                else if (nodeName.equalsIgnoreCase("shock_absorbers")) suspension_shock_absorbers = nodeValue;
                else if (nodeName.equalsIgnoreCase("stabilisers")) suspension_stabilisers = nodeValue;
            }
        }
    }

    private void loadTyresData(Element e) {
        NodeList nl = e.getElementsByTagName("tyres");
        if (nl.getLength() > 0) {
            NodeList children = nl.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (!children.item(i).hasChildNodes()) {
                    continue;
                }
                String nodeName = children.item(i).getNodeName();
                String nodeValue = children.item(i).getFirstChild().getNodeValue();

                if (nodeName.equalsIgnoreCase("rim_size")) rim_size = nodeValue;
                else if (nodeName.equalsIgnoreCase("tyre_size")) tyre_size = nodeValue;
            }
        }
    }

    private void readFloatElements(NodeList nl, String[] elements) {
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
        if (!maxPower.isNaN() && !mass().getValue().isNaN()) {
            return maxPower / (mass().getValue() / 1000f);
        }
        return Float.NaN;
    }

    public float getWeightPerTorque() {
        if (!maxTorque.isNaN() && !mass().getValue().isNaN()) {
            return mass().getValue() / maxTorque;
        }
        return Float.NaN;
    }

    public String getCorrections() {
        boolean hasCorrections = false;
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, CorrectableData<Float>> entry : floatValues.entrySet()) {
            String key = entry.getKey();
            CorrectableData<Float> value = entry.getValue();
            if (value.isCorrected()) {
                hasCorrections = true;
                sb.append(value.toXML(key));
            }
        }
        if (hasCorrections) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("<correction_of_model>");
            sb2.append("<modelcode>" +  code + "</modelcode>");
            sb2.append(sb.toString());
            sb2.append("</correction_of_model>");
            return sb2.toString();
        }
        return "";
    }

    public void correct(String element, String newValue) {
        if (floatValues.containsKey(element)) {
            floatValues.get(element).setCorrected(Util.parseFloat(newValue));
        }
    }

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
}

