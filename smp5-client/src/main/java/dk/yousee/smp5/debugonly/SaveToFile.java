/**
 * 
 */
package dk.yousee.smp5.debugonly;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.xmlbeans.XmlObject;

/**
 * @author m64746
 *
 *         Date: 06/11/2015 Time: 16:04:55
 */
public class SaveToFile {

	public static void saveXmlRequest(XmlObject xmlRequest) {
//		FileOutputStream fop = null;
//		File file;
//		String content = xmlRequest.xmlText();
//		try {
//			String todayAsString = new SimpleDateFormat("ddHHmm").format(new Date()) + new Random().nextInt(100);
//			file = new File("C:\\Users\\IT People\\Downloads\\smp" + todayAsString + ".xml");
//			fop = new FileOutputStream(file);
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			byte[] contentInBytes = content.getBytes();
//			fop.write(contentInBytes);
//			fop.flush();
//			fop.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (fop != null) {
//					fop.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public static void saveResponse(String response) {
//		FileOutputStream fop = null;
//		File file;
//		try {
//			String todayAsString = new SimpleDateFormat("ddHHmm").format(new Date()) + new Random().nextInt(100);
//			file = new File("C:\\Users\\IT People\\Downloads\\smpResposta" + todayAsString + ".xml");
//			fop = new FileOutputStream(file);
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			byte[] contentInBytes = response.getBytes();
//			fop.write(contentInBytes);
//			fop.flush();
//			fop.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (fop != null) {
//					fop.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

}
