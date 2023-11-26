package com.example.inventorymanager.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.inventorymanager.databinding.FragmentCameraBinding;

public class cameraFragment extends Fragment {

    private FragmentCameraBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}
