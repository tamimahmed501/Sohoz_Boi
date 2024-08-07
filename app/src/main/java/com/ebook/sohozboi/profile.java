package com.ebook.sohozboi;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.textfield.TextInputEditText;

public class profile extends Fragment {

    TextView login, createaccount, name, email, id, logout,editprofile;
    LinearLayout profilelay, loginlay;
    RelativeLayout invite,faq, privacy, cpassword,agent,delete;

    private static final String PREF_NAME = "MyPrefs";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_profile, container, false);

        login = myView.findViewById(R.id.login);
        createaccount = myView.findViewById(R.id.createaccount);
        name = myView.findViewById(R.id.name);
        email = myView.findViewById(R.id.email);
        id = myView.findViewById(R.id.id);
        profilelay = myView.findViewById(R.id.profilelay);
        loginlay = myView.findViewById(R.id.loginlay);
        logout = myView.findViewById(R.id.logout);
        invite = myView.findViewById(R.id.invite);
        faq = myView.findViewById(R.id.faq);
        privacy = myView.findViewById(R.id.privacy);
        cpassword = myView.findViewById(R.id.cpassword);
        agent = myView.findViewById(R.id.agent);
        delete = myView.findViewById(R.id.delete);
        editprofile = myView.findViewById(R.id.editprofile);
        logout.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Permission Required").setMessage("Do you want to Log out?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences sharedPreferencesx = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editorx = sharedPreferencesx.edit();
                    editorx.clear();
                    editorx.apply();



                    Toast.makeText(getContext(), "Log-out Successfull.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), start.class));
                    Animatoo.animateSwipeRight(getContext());

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog alert = builder.create();
            alert.show();



        });



        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "You cann't use this service right now.", Toast.LENGTH_SHORT).show();

            }
        });


        agent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(Intent.ACTION_VIEW);

                // Set the WhatsApp package name and URI
                intent.setPackage("com.whatsapp");
                intent.setData(Uri.parse("https://wa.me/+8801618121413"));
                startActivity(intent);



            }
        });






        cpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "You cann't use this service right now.", Toast.LENGTH_SHORT).show();

            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), com.ebook.sohozboi.login.class));
                Animatoo.animateSwipeLeft(getContext());
            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), com.ebook.sohozboi.signin.class));
                Animatoo.animateSwipeLeft(getContext());
            }
        });



        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                shareLink();



            }
        });


        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                webView.URL="";
                webView.TOPTEXT="FAQ";


                startActivity(new Intent(getContext(), webView.class));
                Animatoo.animateSwipeLeft(getContext());
            }
        });


        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                webView.URL="https://sites.google.com/view/shohozboi";
                webView.TOPTEXT="Privacy Policy";


                startActivity(new Intent(getContext(), webView.class));
                Animatoo.animateSwipeLeft(getContext());
            }
        });


        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);


        String uid = sharedPreferences1.getString("uid", "");
        String passwordx = sharedPreferences1.getString("password", "");




        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog;
                dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.acdelete);
                dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.alertbackground));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);

                ImageView cross;
                cross = dialog.findViewById(R.id.cross);
                CardView cancel, confirm;
                TextInputEditText password;
                LottieAnimationView lottie;

                cancel = dialog.findViewById(R.id.cancel);
                confirm = dialog.findViewById(R.id.confirm);
                password = dialog.findViewById(R.id.password);
                lottie = dialog.findViewById(R.id.lottie);

                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String mypassword = password.getText().toString();

                        if (mypassword.length() > 1) {
                            if (passwordx.contains(mypassword)) {
                                lottie.setVisibility(View.VISIBLE);

                                String url = "https://wikipediabangla.com/apps/delete.php?id=" + uid + "&table=account";

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (!isAdded() || getActivity() == null) {
                                            return; // Fragment is not attached to activity
                                        }

                                        if (response.contains("deleted")) {
                                            lottie.setVisibility(View.GONE);

                                            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.clear();
                                            editor.apply();

                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                            alertDialog.setTitle("Alert");
                                            alertDialog.setMessage("Account deleted successfully.");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    startActivity(new Intent(getContext(), MainActivity.class));
                                                    getActivity().finish();
                                                }
                                            });
                                            alertDialog.show();
                                            alertDialog.setCancelable(false);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        if (!isAdded() || getActivity() == null) {
                                            return; // Fragment is not attached to activity
                                        }
                                        lottie.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                requestQueue.add(stringRequest);

                            } else {
                                password.setError("Incorrect Password");
                            }
                        } else {
                            password.setError("Empty");
                        }
                    }
                });
            }
        });




        SharedPreferences sharedPreferencesx = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String idx = sharedPreferencesx.getString("id", "");
        String namex = sharedPreferencesx.getString("name", "");
        String emailx = sharedPreferencesx.getString("email", "");
        String status = sharedPreferencesx.getString("status", "");

        //String verified = sharedPreferencesx.getString("isVerified", "");

        if (status.contains("login")) {
            loginlay.setVisibility(View.GONE);
            profilelay.setVisibility(View.VISIBLE);

            name.setText(namex);
            id.setText("ID: " + idx);
            email.setText(emailx);
        } else {
            profilelay.setVisibility(View.GONE);
            loginlay.setVisibility(View.VISIBLE);
        }


        return myView;
    }

    private void shareLink() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Here's the link to Invite: http://play.google.com/store/apps/details?id=com.ebook.sohozboi");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }
}
