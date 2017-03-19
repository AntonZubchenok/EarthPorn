package by.zubchenok.earthporn;

import android.content.Context;
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
    MyRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button newButton = (Button) findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesAsyncTask task = new ImagesAsyncTask(MainActivity.this);
                task.execute(URL_NEW_IMAGES);
            }
        });

        Button topButton = (Button) findViewById(R.id.top_button);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagesAsyncTask task = new ImagesAsyncTask(MainActivity.this);
                task.execute(URL_TOP_IMAGES);
            }
        });
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
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (position % 3 == 0 ? 2 : 1);
                }
            });
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
