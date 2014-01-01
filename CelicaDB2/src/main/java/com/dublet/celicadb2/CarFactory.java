package com.dublet.celicadb2;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by dublet on 15/12/13.
 */
public class CarFactory {
    private static CarFactory ourInstance = new CarFactory();
    private static HashMap<String, Car> _Cars = new HashMap<String, Car>();
    private static Context _ctx = null;
    private static Integer dbVersion = -1, correctionsVersion = -1;
    private static HashMap<String, List<CorrectableData<String>>> corrections = new HashMap<String, List<CorrectableData<String>>>();

    private static final String CORRECTIONS_FILE = "corrections.xml";

    public static CarFactory getInstance(Activity a) {
        _ctx = a;
        return ourInstance;
    }

    public static CarFactory getInstance() {
        assert(_ctx != null);
        return ourInstance;
    }

    private CarFactory() {

    }

    public Context getContext() {
        assert(_ctx != null);
        return _ctx;
    }

    private String readFile(int resourceId) {
        assert(_ctx != null);
        try {
            InputStream is = _ctx.getResources().openRawResource(resourceId);
            byte[] b = new byte[is.available()];
            int bytesRead = is.read(b);
            assert(bytesRead == is.available());
            return new String(b);
        }
        // catch (FileNotFoundException e) { Log.e("FNF: ", e.getMessage()); }
        catch (IOException e) { Log.e("FNF: ", e.getMessage()); }

        return "";
    }

    static private Document getDocumentElement(String xmlString) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));
            return db.parse(is);
        }
        catch (ParserConfigurationException e) { Log.e("PCE: ", e.getMessage()); }
        catch (SAXException e) { Log.e("SAXE: ", e.getMessage()); }
        catch (IOException e) { Log.e("IOE: ", e.getMessage()); }

        return null;
    }

    private void fillList()
    {
        assert(_Cars != null);
        String xmlContents = readFile(R.raw.celicas);
        Document d = getDocumentElement(xmlContents);
        if (d != null) {
            NodeList versionNodelist = d.getElementsByTagName("version");
            String versionNodeName = versionNodelist.item(0).getNodeName().toLowerCase();
            String versionNodeValue = versionNodelist.item(0).getFirstChild().getNodeValue();
            if (versionNodeName.equals("version")) {
                dbVersion = Integer.parseInt(versionNodeValue);
            }

            NodeList nl = d.getElementsByTagName("car");
            for (int i = 0; i < nl.getLength(); ++i) {
                Element element = (Element)nl.item(i);
                try {
                    Car c = new Car(element);
                    _Cars.put(c.code, c);
                } catch (RuntimeException e) {
                    Log.e("XML Error for element: ", element.toString());
                }
            }
        }

        loadCorrections();
    }

    HashMap<String, Car> getCarMap() {
        assert(_Cars != null);
        if (_Cars.isEmpty()) {
            fillList();
        }

        return _Cars;
    }

    ArrayList<Car> getCarList() {
        assert(_Cars != null);
        if (_Cars.isEmpty()) {
            fillList();
        }

        ArrayList<Car> list = new ArrayList<Car>(_Cars.values());
        Collections.sort(list);
        return list;
    }

    Car getCar(String code) {
        assert(_Cars != null);
        if (_Cars.isEmpty()) {
            fillList();
        }

        return _Cars.get(code);
    }

    void saveCorrections() {
        if (Util.isExternalStorageWritable()) {
            try {
                FileOutputStream fos = _ctx.openFileOutput(CORRECTIONS_FILE, Context.MODE_PRIVATE);
                fos.write("<corrections>".getBytes());
                fos.write(("<version>" + _ctx.getPackageManager().getPackageInfo(_ctx.getPackageName(), 0).versionCode + "</version>").getBytes());
                for (Car car : CarFactory.getInstance().getCarList()) {
                    fos.write(car.getCorrections().getBytes());
                }
                fos.write("</corrections>".getBytes());
                fos.close();
            }
            catch (PackageManager.NameNotFoundException o) {
                Log.e("Could not save corrections", o.getMessage());
            }
            catch (IOException o) {
                Log.e("Could not save corrections", o.getMessage());
            }
            catch (NullPointerException o) {
                Log.e("Could not save corrections", o.getMessage());
            }
        }
    }

    public String getCorrectionsXML() {
        if (Util.isExternalStorageReadable()) {
            try {
                BufferedInputStream bis = new BufferedInputStream(_ctx.openFileInput(CORRECTIONS_FILE));
                byte[] b = new byte[bis.available()];
                int bytesRead = bis.read(b);
                assert(bytesRead == bis.available());
                String s = new String(b);
                bis.close();
                return s;
            }
            catch (IOException o) {
                Log.e("Could not read corrections", o.getMessage());
            }
        }
        return null;
    }

    void loadCorrections() {
        String s = getCorrectionsXML();
        if (s == null)
            return;

        Document d = getDocumentElement(s);
        if (d != null) {
            NodeList versionNodelist = d.getElementsByTagName("version");
            if (versionNodelist.getLength() > 0) {
                String versionNodeName = versionNodelist.item(0).getNodeName().toLowerCase();
                String versionNodeValue = versionNodelist.item(0).getFirstChild().getNodeValue();
                if (versionNodeName.equals("version")) {
                    correctionsVersion = Integer.parseInt(versionNodeValue);
                }
            }

            NodeList nl = d.getElementsByTagName("correction_of_model");
            for (int numModelsCorrected = 0; numModelsCorrected < nl.getLength(); ++numModelsCorrected ) {
                NodeList children = nl.item(numModelsCorrected).getChildNodes();
                Car currentCarToCorrect = null;
                for (int i = 0; i < children.getLength(); i++) {
                    if (!children.item(i).hasChildNodes()) {
                        continue;
                    }
                    String nodeName = children.item(i).getNodeName().toLowerCase();
                    String nodeValue = children.item(i).getFirstChild().getNodeValue();
                    if (currentCarToCorrect == null) {
                        if (!nodeName.equalsIgnoreCase("modelcode"))
                            throw new RuntimeException("EEK");
                        currentCarToCorrect = getCar(nodeValue);
                    } else {
                        NodeList correctionNodes = children.item(i).getChildNodes();
                        if (correctionNodes.getLength() == 3) {
                            if (!correctionNodes.item(0).getNodeName().toLowerCase().equals("element"))
                                throw new RuntimeException("EEK");
                            if (!correctionNodes.item(1).getNodeName().toLowerCase().equals("orig"))
                                throw new RuntimeException("EEK");
                            if (!correctionNodes.item(2).getNodeName().toLowerCase().equals("value"))
                                throw new RuntimeException("EEK");
                            String element = correctionNodes.item(0).getFirstChild().getNodeValue();
                            String orig = correctionNodes.item(1).getFirstChild().getNodeValue();
                            String corrected = correctionNodes.item(2).getFirstChild().getNodeValue();


                            if (corrections.containsKey(currentCarToCorrect.code)) {
                                corrections.get(currentCarToCorrect.code).add(new CorrectableData<String>(element, orig, corrected));
                            } else {
                                corrections.put(currentCarToCorrect.code, Arrays.asList(new CorrectableData<String>(element, orig, corrected)));
                            }
                            if (dbVersion <= correctionsVersion) {
                                currentCarToCorrect.loadCorrection(element, orig, corrected);
                            }
                        } else {
                            throw new RuntimeException("EEK");
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, List<CorrectableData<String>>> getCorrections(){
        HashMap<String, List<CorrectableData<String>>> corrections = new HashMap<String, List<CorrectableData<String>>>();
        for (Car car : _Cars.values()) {
            List<CorrectableData<String>> stringCorrections = car.getCorrrectionsList();
            if (!stringCorrections.isEmpty())
                corrections.put(car.code, stringCorrections);
        }
        return corrections;
    }

    public void clearCorrections() {
        corrections.clear();
        for (Car car : _Cars.values()) {
            car.clearCorrections();
        }
    }
}
