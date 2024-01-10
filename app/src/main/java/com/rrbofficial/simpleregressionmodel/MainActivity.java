package com.rrbofficial.simpleregressionmodel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    EditText inputField;
    Button button;
    TextView textView;


    Interpreter interpreter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = findViewById(R.id.editTextNumber);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        try {
            interpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String input =    inputField.getText().toString();
            float result = doInference(input);
            textView.setText("Result:" +result);
            }
        });
    }
    public float doInference(String val)
    {
        float[] input = new float[1];
        input[0] = Float.parseFloat(val);

        float[][] output = new float[1][1];
        interpreter.run(input,output);
        return output[0][0];
    }


    private MappedByteBuffer loadModelFile() throws IOException
    {
        AssetFileDescriptor assetFileDescriptor =    			this.getAssets().openFd("linear.tflite");
        FileInputStream fileInputStream = new 		FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return         fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,length);
    }
}