package mohaben.com.stopbuy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private RecyclerView recyclerView;
    private Button startScanningButton;
    private SearchView searchView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        recyclerView = findViewById(R.id.recyclerView);
        startScanningButton = findViewById(R.id.startScanningButton);
        searchView = findViewById(R.id.searchView);

        setupViewPager();
        setupRecyclerView();
        setupSearchView();

        startScanningButton.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });
    }

    private void setupViewPager() {
        List<Integer> images = Arrays.asList(R.drawable.image1, R.drawable.image2, R.drawable.image3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, images);
        viewPager.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        // Load products
        loadProducts();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return false;
            }
        });
    }

    private void loadProducts() {
        // Load products into the productList
        productList.add(new Product("Product 1", "1234567890", true));
        productList.add(new Product("Product 2", "0987654321", false));
        productAdapter.notifyDataSetChanged();
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                checkBarcode(result.getContents());
            } else {
                Toast.makeText(this, "No barcode found", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkBarcode(String barcode) {
        for (Product product : productList) {
            if (product.getBarcode().equals(barcode)) {
                if (product.isBoycotted()) {
                    Toast.makeText(this, String.format(getString(R.string.boycotted_product_detected), product.getName()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, String.format(getString(R.string.product_not_boycotted), product.getName()), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
        Toast.makeText(this, String.format(getString(R.string.product_not_found), barcode), Toast.LENGTH_LONG).show();
    }
}