package com.example.inventorymanager.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventorymanager.databinding.FragmentAddItemBinding;
import com.example.inventorymanager.databinding.FragmentCameraBinding;
import com.example.inventorymanager.ui.addItem.addItemViewModel;

public class cameraFragment extends Fragment {

    private FragmentCameraBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cameraViewModel cameraViewModel = new ViewModelProvider(this).get(cameraViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
}
