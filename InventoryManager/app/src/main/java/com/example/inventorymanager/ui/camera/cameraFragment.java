package com.example.inventorymanager.ui.camera;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.widget.Toast;

import com.example.inventorymanager.MainActivity;
import com.example.inventorymanager.R;
import com.example.inventorymanager.databinding.FragmentCameraBinding;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class cameraFragment extends Fragment {

    // The following code is based on and excerpted from a Youtube Tutorial
    // URL: https://www.youtube.com/watch?v=L482ZAno-fY
    // Linked GitHub repo: https://github.com/Everyday-Programmer/Android-Camera-using-CameraX
    // Published April 10, 2023, accessed November 2023
    // License: GNU General Public License v3.0

    // Portions of the code were adapted from the Android CameraX documentation and samples.
    // URL: https://developer.android.com/training/camerax

    private FragmentCameraBinding binding;
    private Button capture;
    private PreviewView previewView;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;

    private ArrayList<String> localImagePaths;

    // Activity result launcher to request camera and storage permissions
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                startCamera(cameraFacing);
            }
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        previewView = binding.previewView;
        capture = binding.captureButton;

//        this.localImagePaths = getArguments().getStringArrayList("localImagePaths");

        // Check if camera permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // If not granted, request the permission using the activity result launcher
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            // If permission is already granted, start the camera
            startCamera(cameraFacing);
        }

        return root;
    }

    public void startCamera(int cameraFacing) {
        // Calculate the aspect ratio based on the preview view dimensions
        int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());

        // Get an instance of the camera provider
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(requireContext());

        // Add a listener to handle the camera provider instance
        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = (ProcessCameraProvider) listenableFuture.get();

                // Create a preview use case
                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                // Create an image capture use case
                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(requireActivity().getWindowManager().getDefaultDisplay().getRotation()).build();

                // Create a camera selector based on the facing direction
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                // Unbind any existing use cases and bind new ones
                cameraProvider.unbindAll();

                // Bind the camera with the lifecycle of the fragment
                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Check if storage permission is granted
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            // If not granted, request the permission using the activity result launcher
                            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        // Take a picture using the image capture use case
                        takePicture(imageCapture);
                    }
                });
                // Set the surface provider for the preview
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    public void takePicture(ImageCapture imageCapture) {
        // Create a file to store the captured image
        final File file = new File(requireContext().getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();

        System.out.println(file.getPath());

        // Take the picture and handle the result using callbacks
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // Display a toast message on the UI thread when the image is saved successfully
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Image saved at: " + file.getPath(), Toast.LENGTH_SHORT).show();
                    }
                });
//                // Restart the camera preview after taking a picture
//                startCamera(cameraFacing);

                // ENFORCING only one picture to be taken
                // once user presses camera icon to take pic, go back to add item screen
                // send the path back to the add item page for display
//                localImagePaths.add(file.getPath());
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("localImagePaths", localImagePaths);

                // navigate to the add item fragment
//                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                navController.navigate(R.id.navigation_addItem); // TODO: Add bundle
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // Display a toast message on the UI thread when there is an error saving the image
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "Failed to save: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                // Restart the camera preview after an error
                startCamera(cameraFacing);
            }
        });
    }

    // Method to calculate the aspect ratio based on width and height
    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}