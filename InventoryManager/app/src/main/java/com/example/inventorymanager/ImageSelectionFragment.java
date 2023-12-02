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
import com.example.inventorymanager.databinding.FragmentImageSelectionBinding;


public class ImageSelectionFragment extends DialogFragment {
    private FragmentImageSelectionBinding binding;
    private OnImageOptionClickListener optionClickListener;
    public static final int CHOICE_GALLERY = 1;
    public static final int CHOICE_CAMERA = 2;


    /**
     * Interface definition for a callback to be invoked when an image selection option is clicked.
     */
    public interface OnImageOptionClickListener {
        /**
         * Helper method that is called when an image selection option is clicked.
         * @param choice The choice made by the user to take a picture or select from gallery
         */
        void onOptionClick(int choice);
    }

    /**
     * Generates the user interface of the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return the root of the view.
     */
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

    /**
     * Sets the OnImageOptionClickListener for this fragment to manage click events
     * @param listener The listener to set
     */
    public void setOnImageOptionClickListener(OnImageOptionClickListener listener) {
        this.optionClickListener = listener;
    }
}
