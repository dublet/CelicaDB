package com.dublet.celicadb2;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dublet.celicadb2.widgets.BaseTextWatcher;
import com.dublet.celicadb2.widgets.DateRangeView;
import com.dublet.celicadb2.widgets.ValueChangeListener;
import com.dublet.celicadb2.widgets.ValueView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment representing a single Car detail screen.
 * This fragment is either contained in a {@link CarListActivity}
 * in two-pane mode (on tablets) or a {@link CarDetailActivity}
 * on handsets.
 */
public class CarDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "car_id";

    /**
     * The content this fragment is presenting.
     */
    private Car mItem;

    private final HashMap<EditText, BaseTextWatcher> mTextWatchers = new HashMap<EditText, BaseTextWatcher>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CarDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = CarFactory.getInstance(getActivity()).getCarMap().get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    private void updatePicture(View rootView, final Car car) {
        int resourceToDisplay = R.drawable.celica_trace;
        if (!car.pictureResources.isEmpty())
        {
            resourceToDisplay = mItem.pictureResources.get(0);
        }
        try {
            ImageView imageView = ((ImageView)rootView.findViewById(R.id.car_detail_picture));
            imageView.setImageResource(resourceToDisplay);
            boolean hasBigImage = !car.bigPictureResources.isEmpty();
            if (hasBigImage) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(view.getContext(), ImageViewerActivity.class);
                        i.putExtra(ImageViewerActivity.BIG_PICTURES_MSG, car.bigPictureResources);
                        startActivityForResult(i, 1);
                    }
                });
            } else {
                imageView.setOnClickListener(null);
            }
            setFieldVisibility(rootView, R.id.car_detail_big_pictures_label, hasBigImage);

        }
        catch (Resources.NotFoundException e) { Log.e("IMAGE RESOURCE NOT FOUND!!", e.getMessage()); }
    }

    private void setTextOfField(View rootView, int resourceId, String value) {
        try {
            ((TextView) rootView.findViewById(resourceId)).setText(value);
        }
        catch (Resources.NotFoundException e) { Log.e("RESOURCE NOT FOUND!!", e.getMessage()); }
        catch (Exception e) { Log.e("Exception", e.getMessage()); }
    }

    private void setStringField(View rootView, int resourceId, String value) {
        String stringVal = "?";

        if (value != null) {
            stringVal = "" + value;
        }
        setTextOfField(rootView, resourceId, stringVal);
    }

    private void setIntField(View rootView, int resourceId, int value) {
        String stringVal = "?";

        if (value > 0) {
            stringVal = "" + value;
        }
        setTextOfField(rootView, resourceId, stringVal);
    }

    private void setFloatField(View rootView, int resourceId, Float value) {
        String stringVal = "?";

        if (!value.isNaN()) {
            stringVal = "" + value;
        }
        setTextOfField(rootView, resourceId, stringVal);
    }

    private void setFieldVisibility(View rootView, int resourceId, boolean isVisible) {
        rootView.findViewById(resourceId).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void setFieldsVisibility(View rootView, List<Integer> resourceIds, boolean isVisible) {
        for (int resourceId : resourceIds) {
            setFieldVisibility(rootView, resourceId, isVisible);
        }
    }

    private void updateIntFields(View rootView, List<Integer> visibilitySet, Integer intValue) {
        assert(!visibilitySet.isEmpty());
        setIntField(rootView, visibilitySet.get(0), intValue);
        setFieldsVisibility(rootView, visibilitySet, intValue > 0);
    }

    private void updateCorrectableStringFields(View rootView, List<Integer> visibilitySet, final CorrectableData<String> stringValue) {
        assert(!visibilitySet.isEmpty());

        try {
            final EditText editText = (EditText) rootView.findViewById(visibilitySet.get(0));
            BaseTextWatcher textWatcher;
            if (mTextWatchers.containsKey(editText))
                textWatcher = mTextWatchers.get(editText);
            else {
                textWatcher =  new BaseTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        super.afterTextChanged(s);
                        stringValue.setCorrected(s.toString());
                        editText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
                    }};
                mTextWatchers.put(editText, textWatcher);
            }
            editText.removeTextChangedListener(textWatcher);
            if (stringValue.orig != null) {
                editText.setText(stringValue.getValue());
                if (stringValue.isCorrected())
                    editText.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
                else
                    editText.setTypeface(Typeface.DEFAULT, 0);

            } else {
                editText.setText("");
            }
            editText.addTextChangedListener(textWatcher);
        }
        catch (Resources.NotFoundException e) { Log.e("RESOURCE NOT FOUND!!", e.getMessage()); }
     //   catch (Exception e) { Log.e("Exception", e.getMessage()); }

        setFieldsVisibility(rootView, visibilitySet, stringValue.orig != null);
    }

    private void updateIntViewFields(View rootView, List<Integer> visibilitySet, Integer intValue) {
        assert(!visibilitySet.isEmpty());

        try {
            ValueView<Integer> intView = (ValueView<Integer>) rootView.findViewById(visibilitySet.get(0));
            intView.setValue(intValue);
        }
        catch (Resources.NotFoundException e) { Log.e("RESOURCE NOT FOUND!!", e.getMessage()); }
        catch (Exception e) { Log.e("Exception", e.getMessage()); }

        setFieldsVisibility(rootView, visibilitySet, intValue > -1);
    }

    private void updateCorrectableIntViewFields(View rootView, List<Integer> visibilitySet, final CorrectableData<Integer> intData) {
        assert(!visibilitySet.isEmpty());

        try {
            final ValueView<Integer> intView = (ValueView<Integer>) rootView.findViewById(visibilitySet.get(0));
            if (intData.isCorrected())
                intView.setCorrected();
            intView.setValue(intData.getValue());
            intView.setChangeListener(new ValueChangeListener<Integer>() {
                final ValueView listenerFloatView = intView;

                @Override
                public void valueChanged(Integer newValue) {
                    intData.setCorrected(newValue);
                    listenerFloatView.setCorrected();
                }
            });
        }
        catch (Resources.NotFoundException e) { Log.e("RESOURCE NOT FOUND!!", e.getMessage()); }
      //  catch (Exception e) { Log.e("Exception", e.getMessage()); }

        setFieldsVisibility(rootView, visibilitySet, intData.getValue() > -1);
    }

    private void updateFloatViewFields(View rootView, List<Integer> visibilitySet, Float floatValue) {
        assert(!visibilitySet.isEmpty());

        try {
            ValueView<Float> floatView = (ValueView<Float>) rootView.findViewById(visibilitySet.get(0));
            floatView.setValue(floatValue);
        }
        catch (Resources.NotFoundException e) { Log.e("RESOURCE NOT FOUND!!", e.getMessage()); }
        catch (Exception e) { Log.e("Exception", e.getMessage()); }

        setFieldsVisibility(rootView, visibilitySet, !floatValue.isNaN());
    }

    private void updateCorrectableFloatViewFields(View rootView, List<Integer> visibilitySet, final CorrectableData<Float> floatData) {
        assert(!visibilitySet.isEmpty());
        assert(floatData != null);

        try {
            final ValueView<Float> floatView = (ValueView<Float>) rootView.findViewById(visibilitySet.get(0));
            if (floatData.isCorrected())
                floatView.setCorrected();
            floatView.setValue(floatData.getValue());
            floatView.setChangeListener(new ValueChangeListener<Float>() {
                final ValueView listenerFloatView = floatView;
                @Override
                public void valueChanged(Float newValue) {
                    floatData.setCorrected(newValue);
                    listenerFloatView.setCorrected();
                }
            });
        }
        catch (Resources.NotFoundException e) { Log.e("RESOURCE NOT FOUND!!", e.getMessage()); }
       // catch (Exception e) { Log.e("Exception", e.getMessage()); }

        setFieldsVisibility(rootView, visibilitySet, !floatData.getValue().isNaN());
    }

    private void updateDateRangeFields(View rootView, List<Integer> visibilitySet, Integer fromValue, Integer toValue) {
        assert(!visibilitySet.isEmpty());

        try {
            ((DateRangeView) rootView.findViewById(visibilitySet.get(0))).setValue(fromValue, toValue);
        }
        catch (Resources.NotFoundException e) { Log.e("RESOURCE NOT FOUND!!", e.getMessage()); }
        catch (Exception e) { Log.e("Exception", e.getMessage()); }

        setFieldsVisibility(rootView, visibilitySet, fromValue > 0 || toValue > 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car_detail, container, false);

        if (mItem != null) {
            setTextOfField(rootView, R.id.car_detail, mItem.name);
            setTextOfField(rootView, R.id.car_detail_code, mItem.code);
            updateDateRangeFields(rootView, Arrays.asList(R.id.car_detail_release_date_range), mItem.releaseYear, mItem.deceaseYear);
            updatePicture(rootView, mItem);
            /* Engine */
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_engine_code, R.id.car_detail_engine_code_label), mItem.engineCode());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_engine_aspiration, R.id.car_detail_engine_aspiration_label), mItem.aspiration());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_engine_fuel, R.id.car_detail_engine_fuel_label), mItem.fuel());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_engine_alternator, R.id.car_detail_engine_alternator_label), mItem.alternator());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_engine_battery, R.id.car_detail_engine_battery_label), mItem.battery());
            updateCorrectableIntViewFields(rootView, Arrays.asList(R.id.car_detail_engine_cylinders, R.id.car_detail_engine_cylinders_label), mItem.numCylinders());
            updateCorrectableIntViewFields(rootView, Arrays.asList(R.id.car_detail_engine_valves_per_cylinder, R.id.car_detail_engine_valves_per_cylinder_label), mItem.numValesPerCylinder());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_engine_displacement, R.id.car_detail_engine_displacement_label), mItem.displacement());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_engine_bore, R.id.car_detail_engine_bore_label), mItem.bore());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_engine_stroke, R.id.car_detail_engine_stroke_label), mItem.stroke());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_engine_compression_view, R.id.car_detail_engine_compression_ratio_label), mItem.compressionRatio());
            setFieldsVisibility(rootView, Arrays.asList(R.id.car_detail_engine_max_power_label, R.id.car_detail_engine_max_power, R.id.car_detail_engine_max_power_unit), mItem.maxPower > 0);
            setFloatField(rootView, R.id.car_detail_engine_max_power, mItem.maxPower);
            setFieldsVisibility(rootView, Arrays.asList(R.id.car_detail_engine_max_power_revs, R.id.car_detail_engine_max_power_revs_unit), mItem.maxPowerRevs > 0);
            setIntField(rootView, R.id.car_detail_engine_max_power_revs, mItem.maxPowerRevs);
            setFieldsVisibility(rootView, Arrays.asList(R.id.car_detail_engine_max_torque_label, R.id.car_detail_engine_max_torque, R.id.car_detail_engine_max_torque_unit), mItem.maxTorque > 0);
            setFloatField(rootView, R.id.car_detail_engine_max_torque, mItem.maxTorque);
            setFieldsVisibility(rootView, Arrays.asList(R.id.car_detail_engine_max_torque_revs, R.id.car_detail_engine_max_torque_revs_unit), mItem.maxTorqueRevs > 0);
            setIntField(rootView, R.id.car_detail_engine_max_torque_revs, mItem.maxTorqueRevs);
            
            /* Drivetrain */
            //setStringField(rootView, R.id.car_detail_drivetrain_transmission, mItem.transmission);
            //setStringField(rootView, R.id.car_detail_drivetrain_drive, mItem.drive);
            //setIntField(rootView, R.id.car_detail_drivetrain_gears, mItem.gears);
            Spinner gearsSpinner = (Spinner)rootView.findViewById(R.id.car_detail_drivetrain_gears);
            if (mItem.gears > 2) {
                gearsSpinner.setSelection(mItem.gears - 3);
                gearsSpinner.setVisibility(View.VISIBLE);
            } else {
                gearsSpinner.setVisibility(View.GONE);
            }
            Spinner transmissionSpinner = (Spinner)rootView.findViewById(R.id.car_detail_drivetrain_transmission);
            if (mItem.transmission.toLowerCase().contains("auto")) {
                transmissionSpinner.setSelection(1);
            } else {
                transmissionSpinner.setSelection(0);
            }
            Spinner driveSpinner = (Spinner)rootView.findViewById(R.id.car_detail_drivetrain_drive);
            if (mItem.drive.contains("RWD")) {
                driveSpinner.setSelection(1);
            } else if (mItem.drive.contains("4WD")) {
                driveSpinner.setSelection(2);
            } else  {
                driveSpinner.setSelection(0);
            }

            setFloatField(rootView, R.id.car_detail_drivetrain_gr1, mItem.gear_ratio_1);
            setFieldVisibility(rootView, R.id.car_detail_drivetrain_gr1, mItem.gears > 0);
            setFloatField(rootView, R.id.car_detail_drivetrain_gr2, mItem.gear_ratio_2);
            setFieldVisibility(rootView, R.id.car_detail_drivetrain_gr2, mItem.gears > 1);
            setFloatField(rootView, R.id.car_detail_drivetrain_gr3, mItem.gear_ratio_3);
            setFieldVisibility(rootView, R.id.car_detail_drivetrain_gr3, mItem.gears > 2);
            setFloatField(rootView, R.id.car_detail_drivetrain_gr4, mItem.gear_ratio_4);
            setFieldsVisibility(rootView, Arrays.asList(R.id.car_detail_drivetrain_gr4_label, R.id.car_detail_drivetrain_gr4), mItem.gears > 3);
            setFloatField(rootView, R.id.car_detail_drivetrain_gr5, mItem.gear_ratio_5);
            setFieldsVisibility(rootView, Arrays.asList(R.id.car_detail_drivetrain_gr5_label, R.id.car_detail_drivetrain_gr5), mItem.gears > 4);
            setFloatField(rootView, R.id.car_detail_drivetrain_gr6, mItem.gear_ratio_6);
            setFieldsVisibility(rootView, Arrays.asList(R.id.car_detail_drivetrain_gr6_label, R.id.car_detail_drivetrain_gr6), mItem.gears > 5);
            setFloatField(rootView, R.id.car_detail_drivetrain_grr, mItem.gear_ratio_R);
            setFloatField(rootView, R.id.car_detail_drivetrain_final_drive, mItem.final_drive);
            /* Performance */
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_performance_top_speed, R.id.car_detail_performance_top_speed_label), mItem.top_speed());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_performance_zero2hundred, R.id.car_detail_performance_zero2hundred_label), mItem.zero2hundred());
            updateFloatViewFields(rootView, Arrays.asList(R.id.car_detail_performance_weight_per_torque, R.id.car_detail_performance_weight_per_torque_label), mItem.getWeightPerTorque());
            updateFloatViewFields(rootView, Arrays.asList(R.id.car_detail_performance_power_per_weight, R.id.car_detail_performance_power_per_weight_label), mItem.getPowerPerWeight());
            /* Economy */
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_economy_overall, R.id.car_detail_economy_overall_label), mItem.eco_overall());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_economy_city, R.id.car_detail_economy_city_label), mItem.eco_city());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_economy_motorway, R.id.car_detail_economy_motorway_label), mItem.eco_motorway());
            /* Tyres */
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_tyres_rim_size, R.id.car_detail_tyres_rim_size_label), mItem.rim_size());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_tyres_size, R.id.car_detail_tyres_size_label), mItem.tyre_size());
            /* Brakes */
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_brakes_front, R.id.car_detail_brakes_front_label), mItem.brakes_front());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_brakes_rear, R.id.car_detail_brakes_rear_label), mItem.brakes_rear());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_brakes_additional, R.id.car_detail_brakes_additional_label), mItem.brakes_additional());
            /* Suspension */
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_suspension_front_mount, R.id.car_detail_suspension_front_mount_label), mItem.suspension_front_mount());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_suspension_rear_mount, R.id.car_detail_suspension_rear_mount_label), mItem.suspension_rear_mount());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_suspension_shock, R.id.car_detail_suspension_shock_label), mItem.suspension_shock_absorbers());
            updateCorrectableStringFields(rootView, Arrays.asList(R.id.car_detail_suspension_stabilisers, R.id.car_detail_suspension_stabilisers_label), mItem.suspension_stabilisers());
            /* Measurements */
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_length, R.id.car_detail_measurement_length_label), mItem.length());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_width, R.id.car_detail_measurement_width_label), mItem.width());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_height, R.id.car_detail_measurement_height_label), mItem.height());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_wheel_base, R.id.car_detail_measurement_wheel_base_label), mItem.wheel_base());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_track_width_front, R.id.car_detail_measurement_track_width_front_label), mItem.track_width_front());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_track_width_rear, R.id.car_detail_measurement_track_width_rear_label), mItem.track_width_rear());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_mass, R.id.car_detail_measurement_mass_label), mItem.mass());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_fuel_capacity, R.id.car_detail_measurement_fuel_capacity_label), mItem.fuel_capacity());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_oil_capacity, R.id.car_detail_measurement_oil_capacity_label), mItem.oil_capacity());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_coolant_capacity, R.id.car_detail_measurement_coolant_capacity_label), mItem.coolant_capacity());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_drag_coefficient, R.id.car_detail_measurement_drag_coefficient_label), mItem.drag_coefficient());
            updateCorrectableFloatViewFields(rootView, Arrays.asList(R.id.car_detail_measurement_steering_wheel_rotations, R.id.car_detail_measurement_steering_wheel_rotations_label), mItem.steering_wheel_rotations());


        }

        return rootView;
    }

    public void setEditMode(boolean newEditMode) {
        View rootView = getView();
        /* Value Views */
        for (Integer i : Arrays.asList(R.id.car_detail_economy_overall, R.id.car_detail_economy_city, R.id.car_detail_economy_motorway,
                R.id.car_detail_measurement_length, R.id.car_detail_measurement_width, R.id.car_detail_measurement_height,
                R.id.car_detail_measurement_wheel_base, R.id.car_detail_measurement_track_width_front, R.id.car_detail_measurement_track_width_rear,
                R.id.car_detail_measurement_mass, R.id.car_detail_measurement_fuel_capacity, R.id.car_detail_measurement_oil_capacity,
                R.id.car_detail_measurement_coolant_capacity, R.id.car_detail_measurement_drag_coefficient, R.id.car_detail_measurement_steering_wheel_rotations,
                R.id.car_detail_performance_top_speed, R.id.car_detail_performance_zero2hundred,
                R.id.car_detail_engine_displacement, R.id.car_detail_engine_bore,
                R.id.car_detail_engine_stroke, R.id.car_detail_engine_compression_view,
                R.id.car_detail_engine_cylinders, R.id.car_detail_engine_valves_per_cylinder)) {
                ((ValueView)rootView.findViewById(i)).setEditMode(newEditMode);
        }

        /* Strings fields */
        for (Integer i : Arrays.asList(R.id.car_detail_tyres_rim_size, R.id.car_detail_tyres_size,
                R.id.car_detail_brakes_front,  R.id.car_detail_brakes_rear, R.id.car_detail_brakes_additional,
                R.id.car_detail_suspension_front_mount, R.id.car_detail_suspension_rear_mount,
                R.id.car_detail_suspension_shock,  R.id.car_detail_suspension_stabilisers,
                R.id.car_detail_engine_code, R.id.car_detail_engine_aspiration,
                R.id.car_detail_engine_fuel, R.id.car_detail_engine_alternator,
                R.id.car_detail_engine_battery)) {
            int disabledType = InputType.TYPE_NULL;
            int enabledType = InputType.TYPE_CLASS_TEXT;
            int newInputType = newEditMode ? enabledType : disabledType;
            ((EditText)rootView.findViewById(i)).setInputType(newInputType);
        }
        /* Spinners */
        for (Integer i : Arrays.asList(R.id.car_detail_drivetrain_gears, R.id.car_detail_drivetrain_transmission, R.id.car_detail_drivetrain_drive)) {
            rootView.findViewById(i).setEnabled(newEditMode);
        }
    }
}
