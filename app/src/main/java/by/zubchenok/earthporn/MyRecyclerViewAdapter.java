package by.zubchenok.earthporn;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private List<String> imageUrls;

    public MyRecyclerViewAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.MyViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        holder.bindData(imageUrl);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public MyViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.image_view);
        }

        public void bindData(String imageUrl) {
            this.mImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
