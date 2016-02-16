package com.zandroid.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class MGAppUtil {

	public static List<String[]> mProcessList = null;
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static void redirectActivity(Context context, Class<?> dstClass) {
		final Intent i = new Intent();
		i.setClass(context, dstClass);
		context.startActivity(i);
	}

	public static void setImageSelector(Context context,
			final ImageView imageView, int drawableNormal, int drawableFocused) {
		InputStream streamNormal = context.getResources().openRawResource(
				drawableNormal);
		final Bitmap bitmapNormal = BitmapFactory.decodeStream(streamNormal);

		InputStream streamFocused = context.getResources().openRawResource(
				drawableFocused);
		final Bitmap bitmapFocused = BitmapFactory.decodeStream(streamFocused);

		imageView.setImageBitmap(bitmapNormal);

		imageView.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					imageView.setImageBitmap(bitmapFocused);
				} else {
					imageView.setImageBitmap(bitmapNormal);
				}
			}
		});

	}

	public static void setImageBackground(Context context, ImageView imageView,
			int drawableNormal) {
		InputStream streamNormal = context.getResources().openRawResource(
				drawableNormal);
		Bitmap bitmapNormal = BitmapFactory.decodeStream(streamNormal);
		imageView.setImageBitmap(bitmapNormal);
	}

	/*
	 * ������view����listiview�߶�
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * �����жϷ����Ƿ�����.
	 *
	 * @param context
	 *            the context
	 * @param className
	 *            �жϵķ������� "com.xxx.xx..XXXService"
	 * @return true ������ false ��������
	 */
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> servicesList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		Iterator<RunningServiceInfo> l = servicesList.iterator();
		while (l.hasNext()) {
			RunningServiceInfo si = (RunningServiceInfo) l.next();
			if (className.equals(si.service.getClassName())) {
				isRunning = true;
			}
		}
		return isRunning;
	}

	/**
	 * ֹͣ����.
	 *
	 * @param context
	 *            the context
	 * @param className
	 *            the class name
	 * @return true, if successful
	 */
	public static boolean stopRunningService(Context context, String className) {
		Intent intent_service = null;
		boolean ret = false;
		try {
			intent_service = new Intent(context, Class.forName(className));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (intent_service != null) {
			ret = context.stopService(intent_service);
		}
		return ret;
	}

	/**
	 * Gets the number of cores available in this device, across all processors.
	 * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
	 *
	 * @return The number of cores, or 1 if failed to get result
	 */
	public static int getNumCores() {
		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					// Check if filename is "cpu", followed by a single digit
					// number
					if (Pattern.matches("cpu[0-9]", pathname.getName())) {
						return true;
					}
					return false;
				}

			});
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * �������ݿ�.
	 *
	 * @param context
	 *            the context
	 * @param dbName
	 *            the db name
	 * @param rawRes
	 *            the raw res
	 * @return true, if successful
	 */
	public static boolean importDatabase(Context context, String dbName,
			int rawRes) {
		int buffer_size = 1024;
		InputStream is = null;
		FileOutputStream fos = null;
		boolean flag = false;

		try {
			String dbPath = "/data/data/" + context.getPackageName()
					+ "/databases/" + dbName;
			File dbfile = new File(dbPath);
			// �ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ����ݿ�
			if (!dbfile.exists()) {
				// ����������ݿ�
				if (!dbfile.getParentFile().exists()) {
					dbfile.getParentFile().mkdirs();
				}
				dbfile.createNewFile();
				is = context.getResources().openRawResource(rawRes);
				fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[buffer_size];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.flush();
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return flag;
	}

	/**
	 * �򿪼���.
	 *
	 * @param context
	 *            the context
	 */
	public static void showSoftInput(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * �رռ����¼�.
	 *
	 * @param context
	 *            the context
	 */
	public static void closeSoftInput(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null
				&& ((Activity) context).getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(((Activity) context)
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 *
	 * ���������ݽ���������Ӧ�ó���.
	 *
	 * @param context
	 * @param processName
	 * @return
	 */
	public static ApplicationInfo getApplicationInfo(Context context,
			String processName) {
		if (processName == null) {
			return null;
		}

		PackageManager packageManager = context.getApplicationContext()
				.getPackageManager();
		List<ApplicationInfo> appList = packageManager
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (ApplicationInfo appInfo : appList) {
			if (processName.equals(appInfo.processName)) {
				return appInfo;
			}
		}
		return null;
	}

	/**
	 *
	 * ������kill����.
	 *
	 * @param context
	 * @param pid
	 */
	public static void killProcesses(Context context, int pid,
			String processName) {
		/*
		 * String cmd = "kill -9 "+pid; Process process = null; DataOutputStream
		 * os = null; try { process = Runtime.getRuntime().exec("su"); os = new
		 * DataOutputStream(process.getOutputStream()); os.writeBytes(cmd +
		 * "\n"); os.writeBytes("exit\n"); os.flush(); process.waitFor(); }
		 * catch (Exception e) { e.printStackTrace(); }
		 * AbLogUtil.d(AbAppUtil.class, "#kill -9 "+pid);
		 */

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = null;
		try {
			if (processName.indexOf(":") == -1) {
				packageName = processName;
			} else {
				packageName = processName.split(":")[0];
			}

			activityManager.killBackgroundProcesses(packageName);

			//
			Method forceStopPackage = activityManager.getClass()
					.getDeclaredMethod("forceStopPackage", String.class);
			forceStopPackage.setAccessible(true);
			forceStopPackage.invoke(activityManager, packageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * ������ִ������.
	 *
	 * @param command
	 * @param workdirectory
	 * @return
	 */
	public static String runCommand(String[] command, String workdirectory) {
		String result = "";
		ZLogUtil.d(MGAppUtil.class, "#" + command);
		try {
			ProcessBuilder builder = new ProcessBuilder(command);
			// set working directory
			if (workdirectory != null) {
				builder.directory(new File(workdirectory));
			}
			builder.redirectErrorStream(true);
			Process process = builder.start();
			InputStream in = process.getInputStream();
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				String str = new String(buffer);
				result = result + str;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *
	 * ���������нű�.
	 *
	 * @param script
	 * @return
	 */
	public static String runScript(String script) {
		String sRet = "";
		try {
			final Process m_process = Runtime.getRuntime().exec(script);
			final StringBuilder sbread = new StringBuilder();
			Thread tout = new Thread(new Runnable() {
				public void run() {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(m_process.getInputStream()),
							8192);
					String ls_1 = null;
					try {
						while ((ls_1 = bufferedReader.readLine()) != null) {
							sbread.append(ls_1).append("\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			tout.start();

			final StringBuilder sberr = new StringBuilder();
			Thread terr = new Thread(new Runnable() {
				public void run() {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(m_process.getErrorStream()),
							8192);
					String ls_1 = null;
					try {
						while ((ls_1 = bufferedReader.readLine()) != null) {
							sberr.append(ls_1).append("\n");
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedReader.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			terr.start();

			int retvalue = m_process.waitFor();
			while (tout.isAlive()) {
				Thread.sleep(50);
			}
			if (terr.isAlive())
				terr.interrupt();
			String stdout = sbread.toString();
			String stderr = sberr.toString();
			sRet = stdout + stderr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sRet;
	}

	/**
	 * Ӧ�ó������������ȡ RootȨ�ޣ��豸�������ƽ�(���ROOTȨ��)
	 *
	 * @return Ӧ�ó�����/���ȡRootȨ��
	 */
	public static boolean getRootPermission(Context context) {
		String packageCodePath = context.getPackageCodePath();
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + packageCodePath;
			// �л���root�ʺ�
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 *
	 * ��������ȡ�������е���Ϣ.
	 *
	 * @return
	 */
	public static List<String[]> getProcessRunningInfo() {
		List<String[]> processList = null;
		try {
			String result = runCommandTopN1();
			processList = parseProcessRunningInfo(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return processList;
	}

	/**
	 *
	 * ������top -n 1.
	 *
	 * @return
	 */
	public static String runCommandTopN1() {
		String result = null;
		try {
			String[] args = { "/system/bin/top", "-n", "1" };
			result = runCommand(args, "/system/bin/");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *
	 * ��������������.
	 *
	 * @param info
	 *            User 39%, System 17%, IOW 3%, IRQ 0% PID PR CPU% S #THR VSS
	 *            RSS PCY UID Name 31587 0 39% S 14 542288K 42272K fg u0_a162
	 *            cn.amsoft.process 313 1 17% S 12 68620K 11328K fg system
	 *            /system/bin/surfaceflinger 32076 1 2% R 1 1304K 604K bg
	 *            u0_a162 /system/bin/top
	 * @return
	 */
	public static List<String[]> parseProcessRunningInfo(String info) {
		List<String[]> processList = new ArrayList<String[]>();
		int Length_ProcStat = 10;
		String tempString = "";
		boolean bIsProcInfo = false;
		String[] rows = null;
		String[] columns = null;
		rows = info.split("[\n]+");
		// ʹ��������ʽ�ָ��ַ���
		for (int i = 0; i < rows.length; i++) {
			tempString = rows[i];
			// AbLogUtil.d(AbAppUtil.class, tempString);
			if (tempString.indexOf("PID") == -1) {
				if (bIsProcInfo == true) {
					tempString = tempString.trim();
					columns = tempString.split("[ ]+");
					if (columns.length == Length_ProcStat) {
						// ��/system/bin/��ȥ��
						if (columns[9].startsWith("/system/bin/")) {
							continue;
						}
						// AbLogUtil.d(AbAppUtil.class,
						// "#"+columns[9]+",PID:"+columns[0]);
						processList.add(columns);
					}
				}
			} else {
				bIsProcInfo = true;
			}
		}
		return processList;
	}

	/**
	 *
	 * ��������ȡ�����ڴ�.
	 *
	 * @param context
	 * @return
	 */
	public static long getAvailMemory(Context context) {
		// ��ȡandroid��ǰ�����ڴ��С
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		// ��ǰϵͳ�����ڴ� ,����õ��ڴ��С���
		return memoryInfo.availMem;
	}

	/**
	 *
	 * ���������ڴ�.
	 *
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context) {
		// ϵͳ�ڴ���Ϣ�ļ�
		String file = "/proc/meminfo";
		String memInfo;
		String[] strs;
		long memory = 0;

		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader, 8192);
			// ��ȡmeminfo��һ�У�ϵͳ�ڴ��С
			memInfo = bufferedReader.readLine();
			strs = memInfo.split("\\s+");
			for (String str : strs) {
				ZLogUtil.d(MGAppUtil.class, str + "\t");
			}
			// ���ϵͳ���ڴ棬��λKB
			memory = Integer.valueOf(strs[1]).intValue() * 1024;
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ByteתλKB��MB
		return memory;
	}

}
