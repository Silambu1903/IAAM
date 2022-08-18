package com.rax.iaam.Others;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.rax.iaam.Callbacks.QRCallback;
import com.rax.iaam.R;
import com.rax.iaam.databinding.FragmentQrcodeReaderBinding;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

public class QRCodeReader extends Fragment {
    private CameraSource.Builder builder;
    private Boolean isQRReaded = false;
    private QRCallback callback;

    private FragmentQrcodeReaderBinding binding;
    private CameraSource cameraSource;
    private BarcodeDetector qrDetector;
    private Context mContext;
    private Detector.Processor<Barcode> detector = new Detector.Processor<Barcode>() {
        @Override
        public void release() {

        }

        @Override
        public void receiveDetections(Detector.Detections<Barcode> detections) {
            final SparseArray barcodes = detections.getDetectedItems();
            if (barcodes.size() > 0) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Write Logic for Getting the value from qr to class
                        try {
                            if (!isQRReaded) {
                                isQRReaded = true;
                                Barcode qrCode = (Barcode) barcodes.valueAt(0);
                                String QrValue = qrCode.displayValue;
                                callback.OnQRReader(QrValue);
                                binding.btnRetry.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();
                            binding.btnRetry.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    };
    private Boolean flashOn = false;



    public QRCodeReader(QRCallback callback) {
        this.callback = callback;
    }

    public static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }

                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qrcode_reader, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        qrDetector = new BarcodeDetector.Builder(mContext).setBarcodeFormats(Barcode.QR_CODE).build();
        builder = new CameraSource.Builder(mContext, qrDetector);
        builder.setAutoFocusEnabled(true).setRequestedFps(15.0f);
        cameraSource = builder.build();
        setUpQrDetector(detector);

        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnRetry.setVisibility(View.GONE);
                isQRReaded = false;
            }
        });

        binding.svQrReader.getHolder().addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(binding.svQrReader.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                try {
                    cameraSource.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.flash.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (flashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnOnFlash() {
        try {
//            if (!flashOn) {
//                cameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
//                binding.flash.setImageResource(R.drawable.ic_flash_off_black_24dp);
//                flashOn = true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnOffFlash() {
        try {
//            if (flashOn) {
//                cameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                binding.flash.setImageResource(R.drawable.ic_flash_on_black_24dp);
//                flashOn = false;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceView(Fragment newFrag) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, newFrag, "");
        ft.commit();
    }

    private void setUpQrDetector(Detector.Processor<Barcode> detector) {
        qrDetector.setProcessor(detector);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
