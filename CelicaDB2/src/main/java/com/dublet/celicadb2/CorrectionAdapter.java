package com.dublet.celicadb2;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dublet.celicadb2.widgets.BaseTextWatcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dublet on 31/12/13.
 */
public class CorrectionAdapter extends BaseExpandableListAdapter {
    final List<String> _correctedModels;
    final HashMap<String, List<CorrectableData<String>>> _corrections;
    final Context _context;

    public CorrectionAdapter(Context c) {
        _context = c;
        _corrections = CarFactory.getInstance().getCorrections();
        _correctedModels = Arrays.asList(_corrections.keySet().toArray(new String[_corrections.size()]));
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._corrections.get(this._correctedModels.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_correction_list_item, null);
        }

         /* Set data */
         try {
             final CorrectableData<String> data = (CorrectableData<String>) getChild(groupPosition, childPosition);
             assert(data != null);

             TextView elementView = ((TextView)convertView.findViewById(R.id.element)),
                     originalView = ((TextView)convertView.findViewById(R.id.original)),
                     correctedView = ((TextView)convertView.findViewById(R.id.corrected));

             elementView.setText(data.tag);
             originalView.setText(data.orig);
             correctedView.setText(data.corrected);

             final Car car = CarFactory.getInstance().getCar(_correctedModels.get(groupPosition));
             if (car.isFloat(data.tag)) {
                 correctedView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
             } else if (car.isInteger(data.tag)) {
                 correctedView.setInputType(InputType.TYPE_CLASS_NUMBER);
             } else if (car.isString(data.tag)) {
                 correctedView.setInputType(InputType.TYPE_CLASS_TEXT);
             }

             correctedView.addTextChangedListener(new BaseTextWatcher() {
                 @Override
                 public void afterTextChanged(Editable s) {
                     super.afterTextChanged(s);
                    /*TODO fix watchers data.setCorrected(s.toString());
                     car.correct(data.tag, s.toString());*/
                 }
             });
         } catch (ClassCastException e) { Log.e("CCE", e.getMessage()); }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._corrections.get(_correctedModels.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _correctedModels.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return _correctedModels.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_correction_list_group, null);
        }

        String modelcode = (String) getGroup(groupPosition);
        Car car = CarFactory.getInstance().getCar(modelcode);
        int correctionCount = _corrections.get(modelcode).size();
        TextView codeView = ((TextView)convertView.findViewById(R.id.corrected_modelcode)),
                nameView = ((TextView)convertView.findViewById(R.id.corrected_name)),
                countView = ((TextView)convertView.findViewById(R.id.corrected_count));

        codeView.setText(modelcode);
        nameView.setText(car.name);
        countView.setText("" + correctionCount + " correction" + (correctionCount > 1 ? "s" : ""));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
