package com.directoryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.directoryapp.R;
import com.directoryapp.databinding.ViewImageSlideBinding;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<View> viewsToSlide;

    public SliderAdapter(Context context, ArrayList<View> views) {
        this.context = context;
        this.viewsToSlide = views;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return viewsToSlide.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        ViewImageSlideBinding binding = ViewImageSlideBinding.inflate(LayoutInflater.from(context), container, false);

        // Add slide view to view wrap
//        View viewToSlide = viewsToSlide.get(position);
//
//        if(viewToSlide.getParent()!=null){
//            ((ViewGroup) viewToSlide.getParent()).removeView(viewToSlide);
//        }
//
//
//        View view = LayoutInflater.from(context).inflate(R.layout.view_image_slide, container, false);
//
//        FrameLayout viewWrap = view.findViewById(R.id.viewWrap);
//        TextView currentSlideText = view.findViewById(R.id.currentSlideText);
//
//        viewWrap.addView(viewToSlide);
//
//        // Slide text
//        String current = ""+(position+1)+"/"+ viewsToSlide.size();
//        currentSlideText.setText(current);
//
//        container.addView(view);
//        return view;

        View view = LayoutInflater.from(context).inflate(R.layout.view_image_slide,container,false);
        FrameLayout img_wrap = view.findViewById(R.id.viewWrap);
        TextView text = view.findViewById(R.id.currentSlideText);

        View imageView = viewsToSlide.get(position);
        if(imageView.getParent()!=null){
            ((ViewGroup)imageView.getParent()).removeView(imageView);
        }

        img_wrap.addView(imageView);

        String current = ""+(position+1)+"/"+viewsToSlide.size();
        text.setText(current);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
