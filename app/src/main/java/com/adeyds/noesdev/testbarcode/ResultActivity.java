package com.adeyds.noesdev.testbarcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtHasil;
    private ImageView imgBarcode;
    private Button btnConv, btnScan;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int BLACK = 0xFF000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        edtHasil = findViewById(R.id.edt_hasil);
        imgBarcode = findViewById(R.id.img_barcode);
        btnConv = findViewById(R.id.btn_conv);
        btnScan = findViewById(R.id.btn_scan);

        btnScan.setOnClickListener(this);
        btnConv.setOnClickListener(this);

//        edtHasil.setText(getIntent().getStringExtra(getString(R.string.barcode)));

//        convertBarcode(edtHasil.getText().toString());

    }

    private void convertBarcode(String text) {
        try {
            Bitmap bitmap = encodeAsBitmap(text);
            imgBarcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, imgBarcode.getWidth(), imgBarcode.getWidth(), null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, imgBarcode.getWidth(), 0, 0, w, h);
        return bitmap;

    }

//    private void convertBarcode(String text) {
//        String text="" // Whatever you need to encode in the QR code
//        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//        try {
//            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
//            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//            imageView.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode ==RESULT_OK){
        edtHasil.setText(data.getStringExtra(getString(R.string.barcode)));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_conv : {
                convertBarcode(edtHasil.getText().toString());
            } break;
            case R.id.btn_scan: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivityForResult(intent, 2);
            } break;

        }
    }
}
