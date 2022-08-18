package com.rax.iaam.Others;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.os.Environment;

import androidx.preference.PreferenceManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rax.iaam.Enums.QRMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.rax.iaam.Others.ApplicationClass.keyQrHeight;
import static com.rax.iaam.Others.ApplicationClass.keyQrWidth;

public class QRGenerator {
    private QRCodeWriter qrCodeWriter = new QRCodeWriter();
    private int qrHeight = 1000, qrWidth = 1000;


    public void setQrHeight(int qrHeight) {
        this.qrHeight = qrHeight;
    }

    public void setQrWidth(int qrWidth) {
        this.qrWidth = qrWidth;
    }

    public Bitmap generateQRCode(Context mContext, String qrData, String title) throws Exception {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        qrHeight = preferences.getInt(keyQrHeight, 180);
        qrWidth = preferences.getInt(keyQrWidth, 180);
        BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK); // Text Color
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(10); // Text Size
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
        // some more settings...

        canvas.drawBitmap(bitmap, 0, 0, paint);
        canvas.drawText(title, 10, 10, paint);


        return bitmap;
    }


    public boolean saveImage(Context mContext, Bitmap myBitmap, String fileName, QRMode mode) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File qrLocation;
        switch (mode) {
            case socketQR:
                qrLocation = new File(Environment.getExternalStorageDirectory() + "/IAAM/QRCodes/Socket");
                break;
            case MachineQR:
                qrLocation = new File(Environment.getExternalStorageDirectory() + "/IAAM/QRCodes/Machine");
                break;
            case deskQR:
                qrLocation = new File(Environment.getExternalStorageDirectory() + "/IAAM/QRCodes/Desk");
                break;
            default:
                qrLocation = new File(Environment.getExternalStorageDirectory() + "/IAAM/QRCodes");
                break;
        }

        // have the object build the directory structure, if needed.

        if (!qrLocation.exists()) {
            qrLocation.mkdirs();
        }

        try {
            File f = new File(qrLocation, fileName + ".jpg");
            f.createNewFile();   //give read write permission
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(mContext,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }
}
