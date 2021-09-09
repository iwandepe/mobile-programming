package com.iwan.rumusabc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText textA = (EditText) findViewById(R.id.input_a);
        EditText textB = (EditText) findViewById(R.id.input_b);
        EditText textC = (EditText) findViewById(R.id.input_c);
        EditText textX1 = (EditText) findViewById(R.id.input_x1);
        EditText textX2 = (EditText) findViewById(R.id.input_x2);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                if (textA.length() > 0 && textB.length() > 0 && textC.length() > 0) {
                    // value a, b, and c
                    double a = Double.parseDouble(textA.getText().toString());
                    double b = Double.parseDouble(textB.getText().toString());
                    double c = Double.parseDouble(textC.getText().toString());
                    double root1, root2;

                    // calculate the determinant (b2 - 4ac)
                    double determinant = b * b - 4 * a * c;

                    // check if determinant is greater than 0
                    if (determinant > 0) {

                        // two real and distinct roots
                        root1 = (-b + Math.sqrt(determinant)) / (2 * a);
                        root2 = (-b - Math.sqrt(determinant)) / (2 * a);

                        System.out.format("root1 = %.2f and root2 = %.2f", root1, root2);
                        textX1.setText(String.format("%.2f", root1));
                        textX2.setText(String.format("%.2f", root2));
                    }

                    // check if determinant is equal to 0
                    else if (determinant == 0) {

                        // two real and equal roots
                        // determinant is equal to 0
                        // so -b + 0 == -b
                        root1 = root2 = -b / (2 * a);
                        System.out.format("root1 = root2 = %.2f;", root1);

                        textX1.setText(String.format("%.2f", root1));
                        textX2.setText(String.format("%.2f", root2));
                    }

                    // if determinant is less than zero
                    else {

                        // roots are complex number and distinct
                        double real = -b / (2 * a);
                        double imaginary = Math.sqrt(-determinant) / (2 * a);
                        System.out.format("root1 = %.2f+%.2fi", real, imaginary);
                        System.out.format("\nroot2 = %.2f-%.2fi", real, imaginary);

                        textX1.setText(String.format("%.2f+%.2fi", real, imaginary));
                        textX2.setText(String.format("%.2f+%.2fi", real, imaginary));
                    }
                }
            }
        });
    }
}