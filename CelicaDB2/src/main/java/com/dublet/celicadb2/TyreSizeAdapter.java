package com.dublet.celicadb2;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dublet on 31/12/13.
 */
public class TyreSizeAdapter extends BaseExpandableListAdapter {
    private class RimSize {
        public final float rimSize;
        public final float minSize;
        public final float maxSize;
        public final float idealMinSize;
        public final float idealMaxSize;
        public RimSize() {
            rimSize = minSize = maxSize = idealMinSize = idealMaxSize = Float.NaN;
        }
        public RimSize(float r, float min, float max, float idealMin, float idealMax) {
            rimSize = r;
            minSize = min;
            maxSize = max;
            idealMinSize = idealMin;
            idealMaxSize = idealMax;
        }
        public boolean isIdealFit(float f) { return (idealMinSize <= f && idealMaxSize >= f); }
        public boolean isFit(float f) { return (minSize <= f && maxSize >= f); }
    }

    private class RimSizeList extends ArrayList<RimSize> {
        public RimSizeList() {
            super(16);
            add(new RimSize(5.0f, 155, 185, 165, 175));
            add(new RimSize(5.5f, 165, 195, 175, 185));
            add(new RimSize(6.0f, 175, 205, 185, 195));
            add(new RimSize(6.5f, 185, 215, 195, 205));
            add(new RimSize(7.0f, 195, 225, 205, 215));
            add(new RimSize(7.5f, 205, 235, 215, 225));
            add(new RimSize(8.0f, 215, 245, 225, 235));
            add(new RimSize(8.5f, 225, 255, 235, 245));
            add(new RimSize(9.0f, 235, 265, 245, 255));
            add(new RimSize(9.5f, 245, 275, 255, 265));
            add(new RimSize(10.0f, 255, 285, 265, 275));
            add(new RimSize(10.5f, 265, 295, 275, 285));
            add(new RimSize(11.0f, 275, 305, 285, 295));
            add(new RimSize(11.5f, 285, 315, 295, 305));
            add(new RimSize(12.0f, 295, 325, 305, 315));
            add(new RimSize(12.5f, 305, 335, 315, 325));
        }

        public RimSize getSmallestIdealFit(float tyreWidth) {
            for (RimSize r : this) {
                if (r.isIdealFit(tyreWidth))
                    return r;
            }
            return null;
        }
        public RimSize getBiggestIdealFit(float tyreWidth) {
            for (int i = size() - 1; i > -1; --i) {
                RimSize r = get(i);
                if (r.isIdealFit(tyreWidth))
                    return r;
            }
            return null;
        }
        public String getIdealFit(float tyreWidth) {
            RimSize smallest = getSmallestIdealFit(tyreWidth), largest = getBiggestIdealFit(tyreWidth);
            if (smallest != null && largest != null) {
                if (smallest == largest)
                    return "" + smallest.rimSize + "J";
                return "" + smallest.rimSize + "-" + largest.rimSize + "J";
            }
            return "";
        }

        public RimSize getSmallestFit(float tyreWidth) {
            for (RimSize r : this) {
                if (r.isFit(tyreWidth))
                    return r;
            }
            return null;
        }
        public RimSize getBiggestFit(float tyreWidth) {
            for (int i = size() - 1; i > -1; --i) {
                RimSize r = get(i);
                if (r.isFit(tyreWidth))
                    return r;
            }
            return null;
        }
        public String getFit(float tyreWidth) {
            RimSize smallest = getSmallestFit(tyreWidth), largest = getBiggestFit(tyreWidth);
            if (smallest != null && largest != null) {
                if (smallest == largest)
                    return "" + smallest.rimSize + "J";
                return "" + smallest.rimSize + "-" + largest.rimSize + "J";
            }
            return "";
        }

    }

    final List<Integer> _suggestedAlloySizes = new ArrayList<Integer>();
    final SparseArray<List<TyreSize>> _suggestedTyreSizes = new SparseArray<List<TyreSize>>();
    final Context _context;
    final RimSizeList _rimSizeList = new RimSizeList();
    TyreSize _stockSize = new TyreSize();

    public TyreSizeAdapter(Context c, TyreSize tyreSize) {
        _context = c;
        _stockSize = tyreSize;

        if (_stockSize != null) {
            generate(_stockSize);
        } else {
            Log.e("Uh-uh", tyreSize.toString());
            return;
        }

        Activity a = (Activity)_context;
        if (a instanceof TyreSizeActivity) {
            EditText maxDeviationView = (EditText)a.findViewById(R.id.max_deviation),
                    compareTyreWidthView = ((EditText)a.findViewById(R.id.compare_tyre_width)),
                    compareProfileView = ((EditText)a.findViewById(R.id.compare_profile)),
                    compareAlloyView = ((EditText)a.findViewById(R.id.compare_alloy_diameter));

            maxDeviationView.setText("1.0");
            compareTyreWidthView.setText("" + _stockSize.tyreWidth);
            compareProfileView.setText("" + _stockSize.profileSize);
            compareAlloyView.setText("" + _stockSize.alloySize);

            Button b = ((Button)a.findViewById(R.id.compare_refresh));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    generate(getStockTyre());
                }
            });
        }

    }

    private TyreSize getStockTyre() {
        Activity a = (Activity)_context;
        if (a instanceof TyreSizeActivity) {
            EditText compareTyreWidthView = ((EditText)a.findViewById(R.id.compare_tyre_width)),
                    compareProfileView = ((EditText)a.findViewById(R.id.compare_profile)),
                    compareAlloyView = ((EditText)a.findViewById(R.id.compare_alloy_diameter));
            try {
                return new TyreSize(
                        Integer.parseInt(compareTyreWidthView.getText().toString()),
                        Integer.parseInt(compareProfileView.getText().toString()),
                        Integer.parseInt(compareAlloyView.getText().toString()),
                        -1, null);
            }
            catch (NumberFormatException e) { Log.e("NFE", e.getMessage()); }
            catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
        }
        return null;
    }

    private float getMaxDeviation() {
        Activity a = (Activity)_context;
        if (a instanceof TyreSizeActivity) {
            EditText maxDeviationView = (EditText)a.findViewById(R.id.max_deviation);
            try {
                return Float.parseFloat(maxDeviationView.getText().toString()) / 100f;
            }
            catch (NumberFormatException e) { Log.e("NFE", e.getMessage()); }
            catch (NullPointerException e) { Log.e("NPE", e.getMessage()); }
        }
        return 0.01f;
    }

    private void generate(TyreSize tyre) {
        assert(tyre != null);
        float softMax = getMaxDeviation();
        int limit = 50;

        _stockSize = tyre;
        _suggestedTyreSizes.clear();
        _suggestedAlloySizes.clear();

        for (int i = tyre.alloySize; i < tyre.alloySize + 3; ++i) {
            _suggestedAlloySizes.add(i);
            _suggestedTyreSizes.put(i, new ArrayList<TyreSize>());
        }

        for (int suggestedAlloySize : _suggestedAlloySizes) {
            for (int suggestedWidth = tyre.tyreWidth - 10; suggestedWidth < tyre.tyreWidth + 50; suggestedWidth += 5) {
                Float previousDiff = Float.NaN;
                for (int suggestedProfile = 25; suggestedProfile < 80; suggestedProfile += 5) {
                    if (_suggestedTyreSizes.get(suggestedAlloySize).size() > limit)
                        break;
                    TyreSize guess = new TyreSize(suggestedWidth, suggestedProfile, suggestedAlloySize, -1 , null);
                    float diff = Math.abs(tyre.differenceTo(guess));
                    float absDiff = Math.abs(diff);
                    if (previousDiff.isNaN() && absDiff > softMax && diff > previousDiff) {
                        break;
                    }
                    previousDiff = diff;
                    if (absDiff < softMax) {
                        _suggestedTyreSizes.get(suggestedAlloySize).add(guess);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._suggestedTyreSizes.get(this._suggestedAlloySizes.get(groupPosition)).get(childPosititon);
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
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_tyre_size_list_item, null);
        }

         /* Set data */
         try {
             final TyreSize data = (TyreSize) getChild(groupPosition, childPosition);
             assert(data != null);
             TextView tyreSizeView = ((TextView)convertView.findViewById(R.id.tyre_size)),
                     differenceView = ((TextView)convertView.findViewById(R.id.difference)),
                     idealRimFitmentView = ((TextView)convertView.findViewById(R.id.ideal_rim_fitment)),
                     rimFitmentView = ((TextView)convertView.findViewById(R.id.rim_fitment));

             tyreSizeView.setText(data.toString());
             NumberFormat nf = NumberFormat.getInstance();
             nf.setMaximumFractionDigits(3);
             differenceView.setText(nf.format(_stockSize.differenceTo(data) * 100f) + " %");
             idealRimFitmentView.setText(_rimSizeList.getIdealFit(data.tyreWidth));
             rimFitmentView.setText(_rimSizeList.getFit(data.tyreWidth));


         } catch (ClassCastException e) { Log.e("CCE", e.getMessage()); }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._suggestedTyreSizes.get(_suggestedAlloySizes.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _suggestedTyreSizes.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return _suggestedTyreSizes.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_tyre_size_list_group, null);
        }

        TextView codeView = ((TextView)convertView.findViewById(R.id.alloy_size_group));

        codeView.setText("" + _suggestedAlloySizes.get(groupPosition) + "\"");

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
