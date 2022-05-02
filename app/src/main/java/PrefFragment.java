
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.finalproject.R;

public class PrefFragment extends PreferenceFragment {
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                 //从xml文件加载选项
        addPreferencesFromResource(R.xml.preferences);
}
}