package com.example.inventorymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.databinding.FragmentImageSelectionBinding;

public class ImageSelectionFragment extends DialogFragment {

    private FragmentImageSelectionBinding binding;
    // Define constants for choices
    private OnImageOptionClickListener optionClickListener;

    public static final int CHOICE_GALLERY = 1;
    public static final int CHOICE_CAMERA = 2;

    public interface OnImageOptionClickListener {
        void onOptionClick(int choice);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImageSelectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Find buttons in the layout
        Button galleryButton = binding.galleryButton;
        Button takePictureButton = binding.takePictureButton;
        ImageButton closeButton = binding.closeButton;

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionClickListener != null) {
                    optionClickListener.onOptionClick(CHOICE_GALLERY);
                }
                dismiss(); // Close the fragment after handling the action
            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionClickListener != null) {
                    optionClickListener.onOptionClick(CHOICE_CAMERA);
                }
                dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return root;
    }

    public void setOnImageOptionClickListener(OnImageOptionClickListener listener) {
        this.optionClickListener = listener;
    }
}
