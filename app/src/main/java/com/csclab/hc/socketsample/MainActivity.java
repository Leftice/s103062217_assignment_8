package com.csclab.hc.socketsample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements android.view.View.OnClickListener
{
    /** Init Variable for IP page **/
    EditText inputIP;
    Button ipSend;
    String ipAdd = "";
    String oper = "";
    /**Init Variable for Main page**/
    EditText inputNumTxt1;
    EditText inputNumTxt2;
    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;

    /** Init Variable for Result page **/
    TextView textResult;
    Button return_button;

    /** Init Variable **/
    OutputStream out;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_page);
        inputIP = (EditText)findViewById(R.id.edIP);
        ipSend = (Button)findViewById(R.id.ipButton);

        ipSend.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view)
            {
                ipAdd = inputIP.getText().toString();
               Thread t = new thread();
                t.start();
            }
        });
    }

    public void jumpToMain()
    {
        setContentView(R.layout.activity_main);

        inputNumTxt1 = (EditText) findViewById(R.id.etNum1);
        inputNumTxt2 = (EditText) findViewById(R.id.etNum2);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }

    /** Function for onclick() implement */
    public void onClick(View v)
    {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating

        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        // defines the button that has been clicked and performs the corresponding operation
        // write operation into oper, we will use it later for output

        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            default:
                break;
        }
        Log.d("debug","ANS "+result);

        jumpToResult(new String(num1 + " " + oper + " " + num2 + " = " + result));
    }

    public void jumpToResult(String resultStr)
    {
        setContentView(R.layout.result_page);
        return_button = (Button) findViewById(R.id.return_button);
        textResult = (TextView) findViewById(R.id.textResult);

        if (textResult != null)
        {
            textResult.setText(resultStr);
            byte[] sendStrByte = new byte[1024];
            System.arraycopy(resultStr.getBytes(), 0, sendStrByte, 0, resultStr.length());
            try {
                out.write(sendStrByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (return_button != null)
        {
            return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    jumpToMain();
                }
            });
        }
    }

    class thread extends Thread
    {
        public void run()
        {
            try
            {
                int serverPort = 2000;

                // Create socket connect server
                Socket socket = new Socket(ipAdd, serverPort);
                System.out.println("Connected!");
                /*OutputStream out = socket.getOutputStream();*/
                out = socket.getOutputStream();
                /* String strToSend = "Hi I'm client";

                byte[] sendStrByte = new byte[1024];
                System.arraycopy(strToSend.getBytes(), 0, sendStrByte, 0, strToSend.length());
                out.write(sendStrByte);
*/
                runOnUiThread(new Runnable() {
                    public void run() {
                        jumpToMain();
                    }
                });
            }
            catch (Exception e)
            {
                System.out.println("Error" + e.getMessage());
            }
        }
    }
}