package com.example.thebutterfly.ui.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thebutterfly.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                return ;
            }
        });
        Button btn = root.findViewById(R.id.analyse_button);
        btn.setOnClickListener(new View.OnClickListener() {
            RadioGroup q1,q2,q3,q4,q5;
                                   @Override
                                   public void onClick(View v) {
                                        q1 = root.findViewById(R.id.question_1);
                                        q2 = root.findViewById(R.id.question_2);
                                        q3 = root.findViewById(R.id.question_3);
                                        q4 = root.findViewById(R.id.question_4);
                                         q5 = root.findViewById(R.id.question_5);
                                       RadioButton a1=root.findViewById(q1.getCheckedRadioButtonId());
                                       RadioButton a2 = root.findViewById(q2.getCheckedRadioButtonId());
                                       RadioButton a3 = root.findViewById(q3.getCheckedRadioButtonId());
                                       RadioButton a4 = root.findViewById(q4.getCheckedRadioButtonId());
                                       RadioButton a5 = root.findViewById(q5.getCheckedRadioButtonId());
                                       StringBuffer answer = new StringBuffer();
                                       try {
                                           answer.append(a1.getText().toString());
                                           answer.append(" " + a2.getText().toString());
                                           answer.append(" " + a3.getText().toString());
                                           answer.append(" " + a4.getText().toString());
                                           answer.append(" " + a5.getText().toString());
                                           String ans = answer.toString();
                                           String[] answers = ans.split(" ");
                                           int count = 0;
                                           for (int i = 0; i < 5; i++) {
                                               if (answers[i].equals("Yes"))
                                                   count++;
                                           }
                                           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                           builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   dialog.dismiss();
                                                   q1.clearCheck();
                                                   q2.clearCheck();
                                                   q3.clearCheck();
                                                   q4.clearCheck();
                                                   q5.clearCheck();
                                               }
                                           });
                                           if(count<=2)
                                           {
                                               builder.setMessage(R.string.no_risk)
                                                       .setTitle(R.string.nr);
                                           }
                                           else
                                           {
                                               builder.setMessage(R.string.at_risk)
                                                       .setTitle(R.string.ar);
                                           }
                                           AlertDialog dialog = builder.create();
                                           dialog.setCanceledOnTouchOutside(false);
                                           dialog.show();
                                       } catch (NullPointerException npe) {
                                           Toast.makeText(getActivity(), "Please Answer All The Questions", Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               }
        );
        return root;
    }
}