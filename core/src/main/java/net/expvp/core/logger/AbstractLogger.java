package net.expvp.core.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.common.collect.Sets;

import net.expvp.api.TimeUtil;
import net.expvp.api.interfaces.logging.ILogger;
import net.expvp.core.NullContainer;

/**
 * Default abstract implementation of ILogger to log to files
 * 
 * @author NullUser
 * @see ILogger
 */
public abstract class AbstractLogger implements ILogger {

	private final NullContainer container;
	private final File folder;
	private final File latest;
	private final Set<String> toAdd;
	private volatile boolean zipping;

	public AbstractLogger(NullContainer container, String name) {
		this.container = container;
		this.folder = new File(container.getPlugin().getDataFolder() + File.separator + name);
		this.latest = new File(folder, "LATEST.log");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		if (!latest.exists()) {
			try {
				latest.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		this.toAdd = Sets.newHashSet();
		this.zipping = false;
	}

	/**
	 * @see ILogger
	 */
	@Override
	public void save() {
		zipAndReplace();
	}

	/**
	 * @see ILogger
	 */
	@Override
	public File getLatest() {
		return latest;
	}

	/**
	 * @see ILogger
	 */
	@Override
	public File getFolder() {
		return folder;
	}

	/**
	 * @see ILogger
	 */
	@Override
	public void zipAndReplace() {
		zipping = true;
		container.getDataSavingThread().processRequest(() -> {
			try {
				String date = TimeUtil.formatDate(System.currentTimeMillis(), false);
				File zFile = new File(folder, date + ".zip");
				int link = 1;
				while (zFile.exists()) {
					zFile = new File(folder, (date + '[') + link + (']' + ".zip"));
					link++;
				}
				FileOutputStream fos = new FileOutputStream(zFile);
				ZipOutputStream zipOut = new ZipOutputStream(fos);
				File fileToZip = latest;
				FileInputStream fis = new FileInputStream(fileToZip);
				ZipEntry zipEntry = new ZipEntry(date + ".log");
				zipOut.putNextEntry(zipEntry);
				final byte[] bytes = new byte[1024];
				int length;
				while ((length = fis.read(bytes)) >= 0) {
					zipOut.write(bytes, 0, length);
				}
				zipOut.close();
				fis.close();
				fos.close();
				latest.deleteOnExit();
				if (!container.isStopped()) {
					latest.createNewFile();
					PrintWriter writer = new PrintWriter(new FileWriter(latest, true));
					toAdd.forEach(writer::println);
					toAdd.clear();
					writer.close();
				}
				zipping = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see ILogger
	 */
	@Override
	public void log(String message) {
		try {
			StringBuilder builder = new StringBuilder();
			appendDate(builder);
			builder.append('[').append("LOG").append(']').append(' ');
			builder.append(message);
			writeToFile(builder.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see ILogger
	 */
	@Override
	public void warn(String message) {
		try {
			StringBuilder builder = new StringBuilder();
			appendDate(builder);
			builder.append('[').append("WARNING").append(']').append(' ');
			builder.append(message);
			writeToFile(builder.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see ILogger
	 */
	@Override
	public void error(String message, Exception ex) {
		try {
			StringBuilder builder = new StringBuilder();
			appendDate(builder);
			StackTraceElement element = ex.getStackTrace()[0];
			builder.append('[').append(ex.toString()).append(']').append(' ');
			builder.append('[').append("ERROR - ").append(ex.getMessage()).append(" -- ").append(element.getFileName())
					.append(" where ").append(element.getMethodName()).append(" at ").append(element.getLineNumber())
					.append(']').append(' ');
			builder.append(message);
			writeToFile(builder.toString());
		} catch (IOException e) {
			ex.printStackTrace();
		}
	}

	/**
	 * Local method for appending the date to the specified builder
	 * 
	 * @param builder
	 *            To append to
	 */
	private final void appendDate(StringBuilder builder) {
		builder.append('[').append(getDateAndTime()).append(']').append(' ');
	}

	/**
	 * Local method for writing to the log file
	 * 
	 * @param string
	 *            To write
	 * @throws IOException
	 *             thrown when the file does not exist
	 */
	private final void writeToFile(String string) throws IOException {
		if (zipping) {
			toAdd.add(string);
			return;
		}
		PrintWriter writer = new PrintWriter(new FileWriter(latest, true));
		writer.println(string);
		writer.close();
	}

	/**
	 * @see ILogger
	 */
	@Override
	public String getDateAndTime() {
		return TimeUtil.formatDate(System.currentTimeMillis(), true);
	}

}
