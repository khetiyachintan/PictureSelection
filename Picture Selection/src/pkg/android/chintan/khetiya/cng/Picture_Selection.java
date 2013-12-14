package pkg.android.chintan.khetiya.cng;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Picture_Selection extends Activity implements OnClickListener
	{

		ImageView user_photo;
		LinearLayout get_more;

		@Override
		protected void onCreate(Bundle savedInstanceState)
			{
				// TODO Auto-generated method stub
				super.onCreate(savedInstanceState);
				setContentView(R.layout.main);
				user_photo = (ImageView) findViewById(R.id.user_photo);
				get_more = (LinearLayout) findViewById(R.id.get_more);
				user_photo.setOnClickListener(this);
				get_more.setOnClickListener(this);
			}

		@Override
		public void onClick(View v)
			{
				// TODO Auto-generated method stub

				if (v.getId() == R.id.user_photo)
					{
						// call dialog to picture mode camera / gallery
						Image_Picker_Dialog();
					} else if (v.getId() == R.id.get_more)
					{
						Intent intent = new Intent(Picture_Selection.this, My_Blog.class);
						startActivity(intent);
					}

			}

		public void Image_Picker_Dialog()
			{

				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
				myAlertDialog.setTitle("Pictures Option");
				myAlertDialog.setMessage("Select Picture Mode");

				myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface arg0, int arg1)
							{
								Utility.pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
								Utility.pictureActionIntent.setType("image/*");
								Utility.pictureActionIntent.putExtra("return-data", true);
								startActivityForResult(Utility.pictureActionIntent, Utility.GALLERY_PICTURE);
							}
					});

				myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface arg0, int arg1)
							{
								Utility.pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(Utility.pictureActionIntent, Utility.CAMERA_PICTURE);
							}
					});
				myAlertDialog.show();

			}

		// After the selection of image you will retun on the main activity with bitmap image 
		protected void onActivityResult(int requestCode, int resultCode, Intent data)
			{
				super.onActivityResult(requestCode, resultCode, data);
				if (requestCode == Utility.GALLERY_PICTURE)
					{
						// data contains result 
						// Do some task 
						Image_Selecting_Task(data);
					} else if (requestCode == Utility.CAMERA_PICTURE)
					{
						// Do some task
						Image_Selecting_Task(data);
					}
			}

		public void Image_Selecting_Task(Intent data)
			{
				try
					{
						Utility.uri = data.getData();
						if (Utility.uri != null)
							{
								// User had pick an image.
								Cursor cursor = getContentResolver().query(Utility.uri, new String[]
									{ android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
								cursor.moveToFirst();
								// Link to the image
								final String imageFilePath = cursor.getString(0);

								//Assign string path to File
								Utility.Default_DIR = new File(imageFilePath);

								// Create new dir MY_IMAGES_DIR if not created and copy image into that dir and store that image path in valid_photo
								Utility.Create_MY_IMAGES_DIR();

								// Copy your image 
								Utility.copyFile(Utility.Default_DIR, Utility.MY_IMG_DIR);

								// Get new image path and decode it
								Bitmap b = Utility.decodeFile(Utility.Paste_Target_Location);

								// use new copied path and use anywhere 
								String valid_photo = Utility.Paste_Target_Location.toString();
								b = Bitmap.createScaledBitmap(b, 150, 150, true);

								//set your selected image in image view
								user_photo.setImageBitmap(b);
								cursor.close();

							} else
							{
								Toast toast = Toast.makeText(this, "Sorry!!! You haven't selecet any image.", Toast.LENGTH_LONG);
								toast.show();
							}
					} catch (Exception e)
					{
						// you get this when you will not select any single image 
						Log.e("onActivityResult", "" + e);

					}
			}

	}
