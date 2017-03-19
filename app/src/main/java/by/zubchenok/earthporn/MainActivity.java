package by.zubchenok.earthporn;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    MyRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagesAsyncTask task = new ImagesAsyncTask(this);
        task.execute("https://www.reddit.com/r/EarthPorn/top/.json?limit=100");
    }

    private class ImagesAsyncTask extends AsyncTask<String, Void, List<String>> {
        private Context mContext;

        public ImagesAsyncTask(Context context) {
            mContext = context;
        }

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
            mAdapter = new MyRecyclerViewAdapter(data);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
