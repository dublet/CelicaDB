package com.dublet.celicadb2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dublet on 31/12/13.
 */
public class CorrectionAdapter extends BaseExpandableListAdapter {
    List<String> _correctedModels;
    HashMap<String, List<CorrectableData<String>>> _corrections;
    Context _context;

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
             CorrectableData<String> data = (CorrectableData<String>) getChild(groupPosition, childPosition);
              assert(data != null);

             ((TextView)convertView.findViewById(R.id.element)).setText(data.tag);
             ((TextView)convertView.findViewById(R.id.original)).setText(data.orig);
             ((TextView)convertView.findViewById(R.id.corrected)).setText(data.corrected);
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

        ((TextView) convertView.findViewById(R.id.corrected_modelcode)).setText(modelcode);
        ((TextView) convertView.findViewById(R.id.corrected_name)).setText(car.name);
        ((TextView) convertView.findViewById(R.id.corrected_count)).setText("" + _corrections.get(modelcode).size());

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
