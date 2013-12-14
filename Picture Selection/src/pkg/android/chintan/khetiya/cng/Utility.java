package pkg.android.chintan.khetiya.cng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Utility
	{

		public static File Copy_sourceLocation;
		public static File Paste_Target_Location;
		public static File MY_IMG_DIR, Default_DIR;
		public static Uri uri;
		public static Intent pictureActionIntent = null;
		public static final int CAMERA_PICTURE = 1;
		public static final int GALLERY_PICTURE = 2;

		public static String Get_Random_File_Name()
			{
				final Calendar c = Calendar.getInstance();
				int myYear = c.get(Calendar.YEAR);
				int myMonth = c.get(Calendar.MONTH);
				int myDay = c.get(Calendar.DAY_OF_MONTH);
				String Random_Image_Text = "" + myDay + myMonth + myYear + "_" + Math.random();
				return Random_Image_Text;
			}

		// Copy your image into specific folder 
		public static File copyFile(File current_location, File destination_location)
			{
				Copy_sourceLocation = new File("" + current_location);
				Paste_Target_Location = new File("" + destination_location + "/" + Utility.Get_Random_File_Name() + ".jpg");

				Log.v("Purchase-File", "sourceLocation: " + Copy_sourceLocation);
				Log.v("Purchase-File", "targetLocation: " + Paste_Target_Location);
				try
					{
						// 1 = move the file, 2 = copy the file
						int actionChoice = 2;
						// moving the file to another directory
						if (actionChoice == 1)
							{
								if (Copy_sourceLocation.renameTo(Paste_Target_Location))
									{
										Log.i("Purchase-File", "Move file successful.");
									} else
									{
										Log.i("Purchase-File", "Move file failed.");
									}
							}

						// we will copy the file
						else
							{
								// make sure the target file exists
								if (Copy_sourceLocation.exists())
									{

										InputStream in = new FileInputStream(Copy_sourceLocation);
										OutputStream out = new FileOutputStream(Paste_Target_Location);

										// Copy the bits from instream to outstream
										byte[] buf = new byte[1024];
										int len;

										while ((len = in.read(buf)) > 0)
											{
												out.write(buf, 0, len);
											}
										in.close();
										out.close();

										Log.i("copyFile", "Copy file successful.");

									} else
									{
										Log.i("copyFile", "Copy file failed. Source file missing.");
									}
							}

					} catch (NullPointerException e)
					{
						Log.i("copyFile", "" + e);

					} catch (Exception e)
					{
						Log.i("copyFile", "" + e);
					}
				return Paste_Target_Location;
			}

		// 	decode your image into bitmap format 
		public static Bitmap decodeFile(File f)
			{
				try
					{
						BitmapFactory.Options o = new BitmapFactory.Options();
						o.inJustDecodeBounds = true;
						BitmapFactory.decodeStream(new FileInputStream(f), null, o);

						// Find the correct scale value. It should be the power of 2.
						final int REQUIRED_SIZE = 70;
						int width_tmp = o.outWidth, height_tmp = o.outHeight;
						int scale = 1;
						while (true)
							{
								if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
									break;
								width_tmp /= 2;
								height_tmp /= 2;
								scale++;
							}

						BitmapFactory.Options o2 = new BitmapFactory.Options();
						o2.inSampleSize = scale;
						return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
					} catch (FileNotFoundException e)
					{
						Log.e("decodeFile", "" + e);
					}
				return null;
			}

		// Create New Dir (folder) if not exist 
		public static File Create_MY_IMAGES_DIR()
			{
				try
					{
						// Get SD Card path & your folder name
						MY_IMG_DIR = new File(Environment.getExternalStorageDirectory(), "/My_Image/");

						// check if exist 
						if (!MY_IMG_DIR.exists())
							{
								// Create New folder 
								MY_IMG_DIR.mkdirs();
								Log.i("path", ">>.." + MY_IMG_DIR);
							}
					} catch (Exception e)
					{
						// TODO: handle exception
						Log.e("Create_MY_IMAGES_DIR", "" + e);
					}
				return MY_IMG_DIR;
			}
	}
