package com.suda.java_question.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;

public class CopyDatabase {

	/**
	 * 将asseet当中的数据库文件拷到程序目录
	 */
	public static void copyEmbassy2Databases(Activity activity,
			String filePath, String fileName) {

		File oldfile = new File(filePath, fileName);

		if (oldfile.exists())
			oldfile.delete();

		File file = new File(filePath, fileName);

		if (file.exists())
			return;

		file.getParentFile().mkdirs();

		InputStream in = null;
		OutputStream out = null;

		try {

			out = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int len = 0;
			in = activity.getAssets().open(fileName);
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
			out.flush();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
