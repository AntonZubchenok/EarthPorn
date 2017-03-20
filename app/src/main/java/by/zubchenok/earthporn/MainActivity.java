package by.zubchenok.earthporn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String URL_NEW_IMAGES = "https://www.reddit.com/r/EarthPorn/new/.json?limit=100";
    private static final String URL_TOP_IMAGES = "https://www.reddit.com/r/EarthPorn/top/.json?limit=100";
    RecyclerView mRecyclerView;
    MyRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRecyclerView();
        setButtons();
    }

    private void setRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyRecyclerViewAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void setButtons() {
        Button newButton = (Button) findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesAsyncTask task = new ImagesAsyncTask();
                task.execute(URL_NEW_IMAGES);
            }
        });

        Button topButton = (Button) findViewById(R.id.top_button);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesAsyncTask task = new ImagesAsyncTask();
                task.execute(URL_TOP_IMAGES);
            }
        });
    }

    private class ImagesAsyncTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<String> result = QueryUtils.fetchImageUrls(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> data) {
            mAdapter.setData(data);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
