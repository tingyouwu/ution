package com.wty.ution.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Locale;

public class FileUtils {

    /**
     * 1MB = 1024*1024B
     */
    private static final int MB = 1024 * 1024;
	/** 总包数 */
	public final static int piece = 1024 * 30;
	public int left = 0;
	private String SDPATH;

	public FileUtils() {
		// 得到当前外部存储设备的目录( /SDCARD )
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/*
	 * 判断是否有SD卡
	 */
	public synchronized static boolean checkSDcard() {
		String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 是否有足够的空间
	 *
	 * @param filePath
	 *            文件路径，不是目录的路径
	 */
	public synchronized static boolean hasEnoughMemory(String filePath) {
		File file = new File(filePath);
		long length = file.length();
		if (filePath.startsWith("/sdcard")
				|| filePath.startsWith("/mnt/sdcard")) {
			return getSDAvailableSize() > length;
		} else {
			return getSystemAvailableSize() > length;
		}

	}

	/**
	 * SD卡是否有足够的空间 以50M为标准
	 *
	 */
	public synchronized static boolean hasEnoughMemory() {
		return getSDAvailableSize() > 50 * 1024 * 1024; // 50M
	}

	/**
	 * 计算SD卡的剩余空间
	 * @return 剩余空间
	 */
	public synchronized static long getSDAvailableSize() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return getAvailableSize(Environment.getExternalStorageDirectory()
					.toString());
		}

		return 0;
	}

	/**
	 * 计算剩余空间
	 *
	 */
	private synchronized static long getAvailableSize(String path) {
		StatFs fileStats = new StatFs(path);
		fileStats.restat(path);
		return (long) fileStats.getAvailableBlocks() * fileStats.getBlockSize(); // 注意与fileStats.getFreeBlocks()的区别
	}

	public synchronized static long getSystemAvailableSize() {
		return getAvailableSize("/data");
	}

	/**
	 * 提示存储空间不足
	 */
	public static void tipUnEnoughMemory(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("SD卡不存在或者SD卡存储空间不足50M,请查看存储情况");
		builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(Settings.ACTION_MEMORY_CARD_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		builder.setNeutralButton("否", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	public static String getMd5ByFile(File file) {

		String value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(
					FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}

	public static int getFileChunkTotal(File file) {
		int chunk = 0;
		try {
			FileInputStream fStream = new FileInputStream(file);
			byte[] buffer = new byte[piece];
			while ((fStream.read(buffer)) != -1) {
				chunk = chunk + 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chunk;
	}

	public static byte[] getBytesFromFile(File file) {
		if (file == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(piece);
			byte[] b = new byte[piece];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
            e.printStackTrace();
		}
		return null;
	}

	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	// /**
	// * 判断SD卡上的文件夹是否存在
	// *
	// * @param fileName
	// * @return
	// */
	public static boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static void writeFile(String data,String path) {

		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] bytes = data.getBytes();
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(bytes);
			fout.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    public static void writeFile2(String data,String path) {

        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
            BufferedWriter writer=new BufferedWriter(write);
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	/**
	 *
	 * 使用文件通道的方式复制文件
	 *
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */

	public static void fileChannelCopy(File s, File t) {
		try {
			FileInputStream fi;
			FileOutputStream fo;
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			fileChannelCopy(fi, fo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * 使用文件通道的方式复制文件
	 *
	 * @param fi
	 *            源文件
	 * @param fo
	 *            复制到的新文件
	 */

	public static void fileChannelCopy(FileInputStream fi, FileOutputStream fo) {
		FileChannel in = null;
		FileChannel out = null;
		try {
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void Copy(InputStream inStream, OutputStream fos) {
		try {
			int bytesum = 0;
			int byteread = 0;

			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fos.write(buffer, 0, byteread);
			}
			inStream.close();
			fos.close();
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

	public static void Copy(InputStream inStream, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File f = new File(newPath);
			if(!f.exists()){
				f.createNewFile();
			}

			FileOutputStream fs = new FileOutputStream(f);
			byte[] buffer = new byte[1444];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
			fs.close();
		} catch (Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

	public static void Copy(File source, String newPath) {
		try {
			if (!source.exists()) {
				System.out.println("source is not exsit ");
			} else {
				InputStream is = new FileInputStream(source);
				Copy(is, newPath);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String readInputStream(InputStream is){
		String result = "";
		InputStreamReader reader = null;
		BufferedReader bufReader = null;
		try {
			reader = new InputStreamReader(is);
			bufReader = new BufferedReader(reader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufReader != null)
					if (is != null)
						is.close();
				if (reader != null)
					reader.close();
				bufReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
   }

	public static String readFromFile(String path){
		String result = "";
		File file = new File(path);
		if(!file.exists())return null;
		FileInputStream in = null;
		try{
			in = new FileInputStream(file);
			int length = in.available();
	        byte [] buffer = new byte[length];
	        in.read(buffer);
	        result = EncodingUtils.getString(buffer, "UTF-8");

	    }
	    catch(Exception e){
	     e.printStackTrace();
	    }  finally{
	    	try {
	    		if(in!=null)in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

		return result;
	}

	public static String readFromAsset(Context context,String fileName){
	    String res="";
	    try{
	     InputStream in = context.getResources().getAssets().open(fileName);
	     int length = in.available();
	        byte [] buffer = new byte[length];
	        in.read(buffer);
	        res = EncodingUtils.getString(buffer, "UTF-8");
	    }
	    catch(Exception e){
	     e.printStackTrace();
	    }
	    return res;
	}

	/**
	 * 复制Assets 里的整个文件夹到SD卡里
	 *
	 * @param assetDir
	 * @param dir
	 */
	public static void CopyAssets(Context context,String assetDir, String dir) {
		String[] files;
		try {
			AssetManager am = context.getResources().getAssets();
			// 获得Assets一共有几多文件
			files = am.list(assetDir);


		File mWorkingPath = new File(dir);
		// 如果文件路径不存在
		if (!mWorkingPath.exists()) {
			// 创建文件夹
			if (!mWorkingPath.mkdirs()) {
				// 文件夹创建不成功时调用
			}
		}

		for (int i = 0; i < files.length; i++) {

				// 获得每个文件的名字
				String fileName = files[i];
				// 根据路径判断是文件夹还是文件
				if (am.list(assetDir+"/"+fileName).length!=0){
//				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						CopyAssets(context,fileName, dir + fileName + "/");
					} else {
						CopyAssets(context,assetDir + "/" + fileName, dir + "/" + fileName + "/");
					}
					continue;
				}

				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists()) {
					outFile.delete();
				}
				InputStream in = null;
				if (0 != assetDir.length()) {
					in = am.open(assetDir + "/" + fileName);
				} else {
					in = am.open(fileName);
				}
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();

		}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean CopyAsset(Context context,String from, String to) {

        try {
            int byteread = 0;
                InputStream inStream =context.getResources().getAssets().open(from);
                OutputStream fs = new BufferedOutputStream(new FileOutputStream(
                        to));
                byte[] buffer = new byte[8192];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 删除文件安全方式：
     */
    public static void deleteFile(File file) {
        if(file==null || !file.exists())return;
        if (file.isFile()) {
            deleteFileSafely(file);
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                deleteFileSafely(file);
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteFile(childFiles[i]);
            }
            deleteFileSafely(file);
        }
    }

    /**
     * 安全删除文件.
     */
    private static boolean deleteFileSafely(File file) {
        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            file.renameTo(tmp);
            return tmp.delete();
        }
        return false;
    }

    public static Intent createFileIntent
			(String filePath){

		File file = new File(filePath);
		if(!file.exists()) return null;
		/* 取得扩展名 */
		String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase(Locale.getDefault());
		/* 依扩展名的类型决定MimeType */
		if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
				end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
			return getAudioFileIntent(filePath);
		}else if(end.equals("3gp")||end.equals("mp4")){
			return getAudioFileIntent(filePath);
		}else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
				end.equals("jpeg")||end.equals("bmp")){
			return getImageFileIntent(filePath);
		}else if(end.equals("apk")){
			return getApkFileIntent(filePath);
		}else if(end.equals("ppt")){
			return getPptFileIntent(filePath);
		}else if(end.equals("xls")){
			return getExcelFileIntent(filePath);
		}else if(end.equals("doc")){
			return getWordFileIntent(filePath);
		}else if(end.equals("pdf")){
			return getPdfFileIntent(filePath);
		}else if(end.equals("chm")){
			return getChmFileIntent(filePath);
		}else if(end.equals("txt")){
			return getTextFileIntent(filePath,false);
		}else{
			return getAllIntent(filePath);
		}
	}

	//Android获取一个用于打开默认文件的intent
	public static Intent getAllIntent( String param ) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"*/*");
		return intent;
	}
	//Android获取一个用于打开APK文件的intent
	public static Intent getApkFileIntent( String param ) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri,"application/vnd.android.package-archive"); 
		return intent;
	}

	//Android获取一个用于打开VIDEO文件的intent
	public static Intent getVideoFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	//Android获取一个用于打开AUDIO文件的intent
	public static Intent getAudioFileIntent( String param ){

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	//Android获取一个用于打开Html文件的intent   
	public static Intent getHtmlFileIntent( String param ){

		Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	//Android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent( String param ) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param ));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	//Android获取一个用于打开PPT文件的intent   
	public static Intent getPptFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");   
		return intent;   
	}   

	//Android获取一个用于打开Excel文件的intent   
	public static Intent getExcelFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/vnd.ms-excel");   
		return intent;   
	}   

	//Android获取一个用于打开Word文件的intent   
	public static Intent getWordFileIntent( String param ){  

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/msword");   
		return intent;   
	}   

	//Android获取一个用于打开CHM文件的intent   
	public static Intent getChmFileIntent( String param ){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/x-chm");   
		return intent;   
	}   

	//Android获取一个用于打开文本文件的intent   
	public static Intent getTextFileIntent( String param, boolean paramBoolean){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		if (paramBoolean){   
			Uri uri1 = Uri.parse(param );   
			intent.setDataAndType(uri1, "text/plain");   
		}else{   
			Uri uri2 = Uri.fromFile(new File(param ));   
			intent.setDataAndType(uri2, "text/plain");   
		}   
		return intent;   
	}  
	//Android获取一个用于打开PDF文件的intent   
	public static Intent getPdfFileIntent( String param ){   

		Intent intent = new Intent("android.intent.action.VIEW");   
		intent.addCategory("android.intent.category.DEFAULT");   
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
		Uri uri = Uri.fromFile(new File(param ));   
		intent.setDataAndType(uri, "application/pdf");   
		return intent;   
	}
	
	/**
     * @Description:计算sdcard上的剩余空间
     * @return MB
     */
    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
        return (int) sdFreeMB;
    }
}
