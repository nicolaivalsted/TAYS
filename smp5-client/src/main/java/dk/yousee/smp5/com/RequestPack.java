package dk.yousee.smp5.com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.xmlbeans.XmlObject;

public abstract class RequestPack<INPUT> {

	public String generateRequest(INPUT input) {
		XmlObject xml;
		xml = createXml(input);
		//DEBUG
		FileOutputStream fop = null;
		File file;
		String content = xml.xmlText();

		try {
			String todayAsString = new SimpleDateFormat("ddHHmm").format(new Date()) + new Random().nextInt(100);
			file = new File("C:\\Users\\IT People\\Downloads\\smp" + todayAsString + ".xml");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return xml.xmlText();
	}

	public abstract XmlObject createXml(INPUT input);

}