package com.example.svampkartan.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.svampkartan.MapsActivity;
import com.example.svampkartan.R;
import com.example.svampkartan.ml.Model;
import com.google.common.util.concurrent.ListenableFuture;


import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class IdentifyFragment extends Fragment {

    private Model tensorFlowLiteModel;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    private PreviewView previewView;
    private TextView predictionResultText;
    private Button identifyButton;

    public IdentifyFragment() {

    }

    public static IdentifyFragment newInstance() {
        return new IdentifyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getActivity()));

        try {
            tensorFlowLiteModel = Model.newInstance(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_identify, container, false);

        previewView = view.findViewById(R.id.previewView);

        identifyButton = view.findViewById(R.id.identifyButton);
        identifyButton.setOnClickListener(v -> identifyMushroom());

        predictionResultText = view.findViewById(R.id.PredictionResultText);
        predictionResultText.setText("-");

        return view;
    }

    private void identifyMushroom() {
        Bitmap bitmap = previewView.getBitmap();
        List<Category> probability = classifyImage(bitmap);
        setPredictionResultText(probability);
        stopPreview();
    }

    private List<Category> classifyImage(Bitmap bitmap){

            // Creates inputs for reference.
            TensorImage image = TensorImage.fromBitmap(bitmap);

            // Runs model inference and gets result.
            Model.Outputs outputs = tensorFlowLiteModel.process(image);
            return outputs.getProbabilityAsCategoryList();


    }

    private void setPredictionResultText(List<Category> probability) {
        predictionResultText.setText(R.string.none);
        for (Category c : probability) {
            if (c.getScore() > 0.6) {
                String pt = c.getLabel() + " (" + (int) (c.getScore() * 100) + "%)";
                predictionResultText.setText(pt);
            }
        }
    }

    private void startPreview() {
        predictionResultText.setText("-");
        identifyButton.setText(R.string.identify);
        identifyButton.setOnClickListener(v -> identifyMushroom());
        try {
            bindPreview(cameraProviderFuture.get());
        } catch (Exception e)  {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        identifyButton.setText(R.string.retry);
        identifyButton.setOnClickListener(v -> startPreview());
        try {
            cameraProviderFuture.get().unbindAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        applyStyle();
    }

    @Override
    public void onResume() {
        super.onResume();
        applyStyle();
    }

    private void applyStyle() {
        MapsActivity activity = (MapsActivity) getActivity();

        CardView cardView = activity.findViewById(R.id.IdentifyMushroomBg);

        TextView predictionText = activity.findViewById(R.id.PredictionText);

        int bgColor, titleTextColor, textColor;
        if (activity.defaultTheme) {
            bgColor = getResources().getColor(R.color.norm_bg, activity.getTheme());
            titleTextColor = getResources().getColor(R.color.norm_title_text, activity.getTheme());
            textColor = getResources().getColor(R.color.norm_text, activity.getTheme());
        } else {
            bgColor = getResources().getColor(R.color.xmas_bg, activity.getTheme());
            titleTextColor = getResources().getColor(R.color.xmas_title_text, activity.getTheme());
            textColor = getResources().getColor(R.color.xmas_text, activity.getTheme());
        }
        cardView.setCardBackgroundColor(bgColor);
        identifyButton.setTextColor(titleTextColor);
        predictionText.setTextColor(titleTextColor);
        predictionResultText.setTextColor(textColor);

    }


}