package com.example.inventorymanager.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.inventorymanager.databinding.KeywordNotFoundBinding;

/**
 * This class manages the page(keyword_not_found) that displays a message that the keyword queried was not found
 */
public class keywordNotFoundFragment extends Fragment{

    private KeywordNotFoundBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.print("We've reached keyword not found");
        binding = KeywordNotFoundBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getActivity().setTitle("Keyword not found");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}