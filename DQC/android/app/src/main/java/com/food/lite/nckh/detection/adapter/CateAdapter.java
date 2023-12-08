package com.food.lite.nckh.detection.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.food.lite.nckh.detection.model.Category;
import com.food.lite.nckh.detection.model.Vob;

import org.tensorflow.lite.examples.detection.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> alCate;
    private ArrayList<Category> allCate;
    public CateAdapter(Context context, ArrayList<Category> alCate) {
        this.context = context;
        this.alCate = alCate;
    }


    @NonNull
    @Override
    public CateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        //gan vao card_view
        View view = inflater.inflate(R.layout.list_cate, parent, false);
        return new CateAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CateAdapter.ViewHolder holder, int position) {
        String cost = formatNumber(Integer.parseInt(alCate.get(position).getCateCost().toString()))+"đ";
        holder.cateName.setText(alCate.get(position).getCateName());
        holder.cateCost.setText(cost);
        holder.cateType.setText(alCate.get(position).getCateType());
        holder.cateUnit.setText(alCate.get(position).getCateUnit());
        holder.cateID.setText(String.valueOf(alCate.get(position).getLabelID()));




//         Assuming getCateImg() returns a byte array representing the image
        byte[] imageData = alCate.get(position).getCateImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        holder.cateImg.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return alCate.size();
    }

//    public Filter getFilter() {
//        return cateFilter;
//    }
//    private Filter cateFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            ArrayList<Category> filteredList = new ArrayList<>();
//
//            if(constraint == null || constraint.length()==0){
//                filteredList.addAll(allCate);
//            }else{
////                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (Category item : allCate) {
//                    if (item.getCateName().toLowerCase().contains(constraint.toString().toLowerCase()) )  {
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//
//            return  results;
//
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            alCate.clear();
//            alCate.addAll((Collection<? extends Category>) results.values);
//            notifyDataSetChanged();
//
//        }
//    };


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cateName, cateCost, cateUnit, cateType, cateID;
        public ImageView cateImg;
        public LinearLayout cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //imageView = imageView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.listCate);
            cateName = itemView.findViewById(R.id.cateName);
            cateCost = itemView.findViewById(R.id.cateCost);
            cateUnit=itemView.findViewById(R.id.cateUnit);
            cateType=itemView.findViewById(R.id.cateType);
            cateImg = itemView.findViewById(R.id.cateImg);
            cateID = itemView.findViewById(R.id.cateID);
        }
    }
    private static String formatNumber(Integer number) {
        // Create a NumberFormat instance with grouping enabled
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        // Format the number
        return numberFormat.format(number);
    }
}
