package com.example.thebutterfly.ui.notifications;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.thebutterfly.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import static com.example.thebutterfly.R.id.ed;
import static com.example.thebutterfly.R.id.tv;

public class NotificationsFragment extends Fragment  {

    private NotificationsViewModel notificationsViewModel;
    final Attribute attributetsh = new Attribute("tsh");

    final List<String> classes = new ArrayList<String>() {
        {
            add("hypothyroid");
            add("negative");
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Button b = root.findViewById(R.id.b);
        TextView tv=root.findViewById(R.id.tv);
        EditText ed=root.findViewById(R.id.ed);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double d = Double.parseDouble(ed.getText().toString());
                    ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
                        {
                            add(attributetsh);
                            Attribute attributeClass = new Attribute("@@class@@", classes);
                            add(attributeClass);
                        }
                    };
                    // unpredicted data sets (reference to sample structure for new instances)
                    Instances dataUnpredicted = new Instances("TestInstances",
                            attributeList, 1);
                    // last feature is target variable
                    dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);

                    // create new instance: this one should fall into the setosa domain
                    DenseInstance newInstancePredict = new DenseInstance(dataUnpredicted.numAttributes()) {
                        {
                            setValue(attributetsh, d);
                        }
                    };

                    // instance to use in prediction
                    DenseInstance newInstance = newInstancePredict;
                    // reference to dataset
                    newInstance.setDataset(dataUnpredicted);

                    // import ready trained model
                    Classifier cls = null;
                    try {
                        AssetManager assetManager = getContext().getAssets();
                        cls = (Classifier) weka.core.SerializationHelper
                                .read(assetManager.open("modelj.model"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (cls == null)
                        return;

                    // predict new sample
                    try {
                        double result = cls.classifyInstance(newInstance);
                        System.out.println("Index of predicted class label: " + result );
                        if(result==0.0) {
                            tv.setText("Hypothyroid POSITIVE");
                        }
                        else if(result==1.0) {
                            tv.setText("Hypothyroid NEGATIVE");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getActivity(), "Please enter valid value", Toast.LENGTH_SHORT).show();
                }
                // Instances(...) requires ArrayList<> instead of List<>...
            }
        });
        return root;
    }
}