/**
 * @author: Ramesh Kuthala
 */
package com.hackthon.groceryshopping;

import com.hackthon.utilities.DownloadAndSetItemImage;
import com.hackthon.utilities.ItemDetails;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowItemDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_show_item_details);

		final ItemDetails itemDetails = (ItemDetails)getIntent().getSerializableExtra("ItemDetails");
		System.out.println("item name from bundle: "+ itemDetails.getName());
		TextView itemtitle = (TextView) findViewById(R.id.itemtitle);
		TextView itemPrice = (TextView) findViewById(R.id.itemprice);
		ImageView itemImage = (ImageView) findViewById(R.id.itemimage);
		itemtitle.setText(itemDetails.getName());
		
		TextView itemdisc = (TextView) findViewById(R.id.itemdisc);
		itemdisc.setText(itemDetails.getDescription());
		itemPrice.setText("$"+itemDetails.getPricing());
		new DownloadAndSetItemImage().execute(itemImage, itemDetails.getImageLocation());
		
		Button addToCart = (Button)findViewById(R.id.addtocart);
		
		addToCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CartData.getInstance().addSelectedItem(itemDetails);
				Toast.makeText(getApplicationContext(), "Your Item is added to list", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
