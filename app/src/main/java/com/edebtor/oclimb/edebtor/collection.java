package com.edebtor.oclimb.edebtor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.edebtor.oclimb.edebtor.Common.ServiceHandler;
import com.edebtor.oclimb.edebtor.Common.commo;
import com.edebtor.oclimb.edebtor.Model.two_item;
import com.edebtor.oclimb.edebtor.printers.PrinterActivity;
import com.edebtor.oclimb.edebtor.utill.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class collection extends AppCompatActivity {
    public static String msg;
    public static String msg1;
    public static String msg2;
    public static int cr_id;
    int crdit_id;
    Button btn_print, btn_histry, btn_print_csr;
    EditText installment_text,last_payement_date,due_date,total_amount,sub_total,additional_amount,paid_amount,total_granted_amount,se_granted_amount,get_Pno1;
    Spinner getselectdate;
    String get_installment_text,get_last_payement_date,get_due_date,get_total_amount,get_sub_total,get_additional_amount,get_paid_amount,get_PayFor,get_GrantAmount,get_duration,get_PaidAmount,get_due;
    double instalment_payemet,need_payement;


    Context context=this;

    Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        db=new Database(collection.this);

        installment_text = (EditText) findViewById(R.id.installment_text);
        due_date = (EditText) findViewById(R.id.due_date);
        last_payement_date = (EditText) findViewById(R.id.last_payement_date);
        total_amount = (EditText) findViewById(R.id.total_amount);
        additional_amount = (EditText) findViewById(R.id.additional_amount);
        // paid_amount = (EditText) findViewById(R.id.paid_amount);
        sub_total = (EditText) findViewById(R.id.sub_total);

        total_granted_amount = (EditText) findViewById(R.id.total_granted_amount);
        se_granted_amount = (EditText) findViewById(R.id.granted_amount);

        getselectdate = (Spinner) findViewById(R.id.select_days);

        Bundle bundle = getIntent().getExtras();

        crdit_id= Integer.parseInt(crdit_manage.crdit_id);
        cr_id = crdit_id;

        crditDe(crdit_id);
        setdates();

        getselectdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if(pos != 7) {

                    Double Dates = Double.parseDouble(String.valueOf(pos))+1;
                    get_PayFor = String.valueOf(pos)+1;
                    sub_total.setText(String.valueOf(instalment_payemet * Dates));

                }else{
                    get_PayFor = "0";
                    sub_total.setText(String.valueOf(need_payement));
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_histry=findViewById(R.id.btn_histry);
        btn_histry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(collection.this, cridit_histry.class);
                startActivity(i);


            }
        });

        btn_print=findViewById(R.id.btn_print);
        btn_print_csr = findViewById(R.id.btn_print_csr);

        //printing the collector copy
        btn_print_csr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(collection.this);
                builder.setMessage("Do you want to save the payment?").setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String get_installment_text, get_last_payement_date, get_due_date, get_total_amount, get_sub_total, get_additional_amount, get_paid_amount;

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Calendar cal = Calendar.getInstance();

                        String curent_date = dateFormat.format(cal.getTime());

                        String idInvoice_Payments = "1";
                        String Amount = sub_total.getText().toString();
                        String AdditionalAmount = additional_amount.getText().toString();
                        String g_installment_text = installment_text.getText().toString();

                        String g_total_amount = total_amount.getText().toString();

                        String DateTime = dateFormat.format(cal.getTime());

                        String PayFor = get_PayFor;
                        String Credit_Invoice_idCredit_Invoice = String.valueOf(crdit_id);
                        String user_idUser = LoginActivity.uid;
                        String Status = "1";
                        String sync_status = "1";


                        if (Amount.equals("")) {
                            Toast.makeText(collection.this, "Please Enter Sub Total.", Toast.LENGTH_SHORT).show();
                        } else if (AdditionalAmount.equals("")) {
                            Toast.makeText(collection.this, "Please Enter Additional Amount.", Toast.LENGTH_SHORT).show();
                        } else if (PayFor.equals("")) {
                            Toast.makeText(collection.this, "Please Enter PayFor.", Toast.LENGTH_SHORT).show();
                        } else{


                            db.invoice_payments_data(idInvoice_Payments, Amount, AdditionalAmount, DateTime, PayFor, Credit_Invoice_idCredit_Invoice, user_idUser, Status, sync_status);

                            Toast.makeText(collection.this, "Payment saved successfully!", Toast.LENGTH_SHORT).show();

                            String getcrdit_cust= crdit_manage.crdit_cust;

                                String total_Granted_Amount = total_granted_amount.getText().toString();
                                System.out.println("total_Granted_Amount>>>>>> : " + total_Granted_Amount);

                                double extraPaid = Double.parseDouble(g_total_amount) - Double.parseDouble(g_installment_text);
                                String extra = String.format("%.2f", extraPaid);
                                double totalPaidAmount = Double.parseDouble(AdditionalAmount) + Double.parseDouble(g_installment_text);
                                String totalPaid = String.format("%.2f", totalPaidAmount);
                                double totalDue = Double.parseDouble(total_Granted_Amount) - Double.parseDouble(totalPaid);
                                String dueAmount = String.format("%.2f", totalDue);


                            /*msg = "\n G5 Credit Lanka    \n " + curent_date + " \n Loan No : " + Credit_Invoice_idCredit_Invoice + "\n"+ getcrdit_cust +" \n ----------------------------- \n Rental (Monthly Installment) : Rs." + g_installment_text + " \n Loan Amount : Rs." + get_GrantAmount + "\n Duration : " + get_duration + " \n ----------------------------- \n" +
                                    " Total Paid : Rs." + get_PaidAmount + "\n Total Due : Rs." + get_due + "\n Amount Paid on the current day : Rs." + g_total_amount + " \n ----------------------------- \n Hotline - 071 986 20 62 \n \n   Powered by SaviMaga \n        071 986 20 62 \n";*/

                            msg1 = "\n\n Savi Maga    \n " + curent_date + " \n Loan No : " + Credit_Invoice_idCredit_Invoice + "\n"+ getcrdit_cust +" \n ----------------------------- \n Installment : Rs." + g_installment_text + " \n Loan Amount : Rs." + get_GrantAmount + "\n Duration : " + get_duration + " \n ----------------------------- \n" +
                                    " Total Paid : Rs." + totalPaid + "\n Total Due : Rs." + dueAmount + "\n Additional amount : Rs." + extra +  " \n\n --------------- " + "\n Customer Signature" + " \n ----------------------------- \n   Hotline - 071 234 56 78  \n      Powered by SaviMaga \n        071 234 56 78 \n\n";

                            msg = msg1;
                            Intent ii = new Intent(collection.this, PrinterActivity.class);

                            startActivity(ii);


                        }


                    }
                }).setNegativeButton("Cancel",null) ;
                AlertDialog alt =   builder.create();
                alt.show();


            }
        });

        //printing for Customer copy

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String get_installment_text, get_last_payement_date, get_due_date, get_total_amount, get_sub_total, get_additional_amount, get_paid_amount;

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();

                String curent_date = dateFormat.format(cal.getTime());

                //String DateTime =arr_user.getString("DateTime");
                String idInvoice_Payments = "1";
                String Amount = sub_total.getText().toString();
                String AdditionalAmount = additional_amount.getText().toString();
                String g_installment_text = installment_text.getText().toString();
                //String g_sub_total =sub_total.getText().toString();
                String g_total_amount = total_amount.getText().toString();

                String DateTime = dateFormat.format(cal.getTime());

                String PayFor = get_PayFor;
                String Credit_Invoice_idCredit_Invoice = String.valueOf(crdit_id);
                String user_idUser = LoginActivity.uid;
                String Status = "1";
                String sync_status = "1";


                if (Amount.equals("")) {
                    Toast.makeText(collection.this, "Please Enter Sub Total.", Toast.LENGTH_SHORT).show();
                } else if (AdditionalAmount.equals("")) {
                    Toast.makeText(collection.this, "Please Enter Additional Amount.", Toast.LENGTH_SHORT).show();
                } else if (PayFor.equals("")) {
                    Toast.makeText(collection.this, "Please Enter PayFor.", Toast.LENGTH_SHORT).show();
                } else{


                    db.invoice_payments_data(idInvoice_Payments, Amount, AdditionalAmount, DateTime, PayFor, Credit_Invoice_idCredit_Invoice, user_idUser, Status, sync_status);

                    String getcrdit_cust= crdit_manage.crdit_cust;

                    String total_Granted_Amount = total_granted_amount.getText().toString();
                    System.out.println("total_Granted_Amount>>>>>> : " + total_Granted_Amount);

                    double extraPaid = Double.parseDouble(g_total_amount) - Double.parseDouble(g_installment_text);
                    String extra = String.format("%.2f", extraPaid);
                    double totalPaidAmount = Double.parseDouble(AdditionalAmount) + Double.parseDouble(g_installment_text);
                    String totalPaid = String.format("%.2f", totalPaidAmount);
                    double totalDue = Double.parseDouble(total_Granted_Amount) - Double.parseDouble(totalPaid);
                    String dueAmount = String.format("%.2f", totalDue);

                    msg2 = "\n\n Savi Maga    \n " + curent_date + " \n Loan No : " + Credit_Invoice_idCredit_Invoice + "\n"+ getcrdit_cust +" \n ----------------------------- \n Installment : Rs." + g_installment_text + " \n Loan Amount : Rs." + get_GrantAmount + "\n Duration : " + get_duration + " \n ----------------------------- \n" +
                            " Total Paid : Rs." + totalPaid + "\n Total Due : Rs." + dueAmount + "\n Additional amount : Rs." + extra + " \n ----------------------------- \n   Hotline - 071 234 56 78  \n      Powered by SaviMaga \n        071 234 56 78  \n\n";


                    msg = msg2;
                    Intent ii = new Intent(collection.this, PrinterActivity.class);

                    startActivity(ii);


                }


            }
        });

        additional_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(getCurrentFocus() == additional_amount) {
                    if (!charSequence.toString().isEmpty() || !charSequence.toString().equals("")) {

                        String Amount = sub_total.getText().toString();
                        double g_sub_total = Double.parseDouble(sub_total.getText().toString());
                        double g_interest_val = Double.parseDouble(charSequence.toString());
                        total_amount.setText(String.valueOf(g_sub_total + g_interest_val));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    //==========================================================================================

    private void setcrditDe(String installment,String lastDate,int dateDiff) {

        installment_text = (EditText) findViewById(R.id.installment_text);
        installment_text.setText(installment);
    }

    private void setdates() {

        String[] xs = new String[] {"1 Days","2 Days","3 Days","4 Days","5 Days","6 Days","7 Days","Full Amount"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, xs
        );


        getselectdate.setAdapter(spinnerArrayAdapter);
    }


    private void  crditDe(int crdit_id){

        try {

            String payment[] = db.getinvoice_payment(crdit_id);


            String DailyPaymentAmount =  payment[0];
            String paid_total_amount =  payment[1];
            String last_payement =  payment[2];
            String payemet_due_date =  payment[3];
            String allTotalAmount =  payment[4];
            get_GrantAmount =  payment[5];
            get_duration =payment[6];
            get_PaidAmount =payment[7];
            installment_text.setText(DailyPaymentAmount);

            total_granted_amount.setText(allTotalAmount);
            se_granted_amount.setText(get_GrantAmount);

            due_date.setText(payemet_due_date);
            total_amount.setText(DailyPaymentAmount);
            last_payement_date.setText(last_payement);
            //get_Pno1.setText(c_Pno1);
            //sub_total.setText(DailyPaymentAmount);


            instalment_payemet = Double.parseDouble(DailyPaymentAmount);
            need_payement = Double.parseDouble(allTotalAmount) - instalment_payemet;

            get_due = String.valueOf(Double.parseDouble(allTotalAmount) - Double.parseDouble(get_PaidAmount) );

            System.out.println("DUE AMOUNT " + get_due + "TOT>>> " + allTotalAmount + "PAID>>>>> "+ get_PaidAmount);

        }catch (Exception e){

        }

    }
}
