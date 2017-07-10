package movies.flag.pt.moviesapp.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import movies.flag.pt.moviesapp.R;

/**
 * Created by Silva on 30/06/2017.
 */

public class SearchScreen extends BaseScreen {

    //region: Fields Declaration
    private EditText searchEditText;
    private Button searchButton;
    //endregion

    //region: Override Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        findViews();
        addListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        Intent returnData = new Intent();
        returnData.putExtra("response", searchEditText.getText().toString());
        setResult(RESULT_OK, returnData);
        super.finish();
    }

    //endregion

    //region: Private Methods.
    private void findViews(){
        searchEditText = (EditText) findViewById(R.id.search_screen_text);
        searchButton = (Button) findViewById(R.id.search_screen_button);
    }
    private void addListeners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                finish();
                                            }
                                        }
        );
    }
    //endregion
}
