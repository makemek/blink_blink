package button;

import java.io.File;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

public class AudioBrowser {
	
	private JFileChooser chooser = new JFileChooser();
	private Minim minim;
	
	public AudioBrowser(Minim minim)
	{
		super();
		this.minim = minim;
		
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Select Audio");
		Action details = chooser.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		setFilter();
	}
		
	private void setFilter()
	{
		CustomFilter mp3Filter = new CustomFilter("mp3", "MP3 Only (*.mp3)");
		CustomFilter wavFilter = new CustomFilter("wav", "WAV Only (*.wav)");
		CustomFilter aiffFilter = new CustomFilter("aiff", "AIFF Only (*.aiff)");
		CustomFilter auFilter = new CustomFilter("au", "AU Only (*.au)");
		CustomFilter sndFilter = new CustomFilter("snd", "SND Only (*.snd)");
		AcceptableAudio allFormat = new AcceptableAudio(mp3Filter, wavFilter, aiffFilter, auFilter, sndFilter);
		
		chooser.setFileFilter(mp3Filter);
		chooser.setFileFilter(wavFilter);
		chooser.setFileFilter(aiffFilter);
		chooser.setFileFilter(auFilter);
		chooser.setFileFilter(sndFilter);
		chooser.setFileFilter(allFormat);
	}
	
	public AudioPlayer browse()
	{
		boolean isOK = false;
		AudioPlayer song = null;
		
		//chooser.setCurrentDirectory(new File("C:\\Users\\Apipol\\Music"));
		
		while(!isOK) {
					
			int status = chooser.showOpenDialog(null);
			File file = chooser.getSelectedFile();
			
			if(status == JFileChooser.CANCEL_OPTION) 
				return null;
			
			try {
				minim.stop();
				System.gc();
				song = minim.loadFile(file.getAbsolutePath(), 2048);
				isOK = true;
			}
			
			catch (Exception e) {
				System.err.println(e);
				String message = "File: " + file.getName() + " is corrupted.";
				JOptionPane.showMessageDialog(null, message, "Can't open the file", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		return song;
	}
	
	public File getSelectedFile() {
		return chooser.getSelectedFile();
	}
	
}

class CustomFilter extends FileFilter
{
	private final String FORMAT;
	private final String DESCRIP;
	
	public CustomFilter(final String FORMAT, final String DESCRIPTION)
	{
		this.FORMAT = FORMAT;
		DESCRIP = DESCRIPTION;
	}
		
	public String extensionFormat() {return FORMAT;}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		
		if(extension(f).equalsIgnoreCase(FORMAT))
			return true;
		
		return false;
	}

	public static String extension (File f)
	{
		String fileName = f.getName();
		int extIdx = fileName.lastIndexOf('.');
		if( (extIdx > 0) && (extIdx < fileName.length() - 1) )
			return fileName.substring(extIdx + 1);
		else
			return "";
	}

	@Override
	public String getDescription() {
		return DESCRIP;
	}
}

class AcceptableAudio extends FileFilter
{
	private CustomFilter[] filters;
	
	public AcceptableAudio(CustomFilter... filters)
	{
		this.filters = filters;
	}
	
	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		
		for(CustomFilter filter : filters)
			if(filter.accept(f))
				return true;
		
		return false;
	}

	@Override
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("All acceptable format (");
		
		for(CustomFilter filter : filters)
			sb.append("*" + filter.extensionFormat() + ", ");
		
		sb.setCharAt(sb.length() - 2, ')');
		
		return sb.toString();
	}
}
