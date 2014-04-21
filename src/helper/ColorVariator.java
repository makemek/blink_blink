package helper;

import java.awt.Color;
import java.util.Random;

public class ColorVariator {
	
	private Float incR, incG, incB;
	private Color destColor;
	private float step = 0.5f;
	
	private Random rnd = new Random();
		
	public ColorVariator() {
		destColor = randomizeColor();
		incR = (float) destColor.getRed();
		incG = (float) destColor.getGreen();
		incB = (float) destColor.getBlue();
		destColor = randomizeColor();
	}
	
	public Color variate() {
		if(equal(incR, (float)destColor.getRed()) && equal(incG, (float)destColor.getGreen()) && equal(incB, (float)destColor.getBlue()))
			destColor = randomizeColor();
		
		
		incR = accumulate(incR, destColor.getRed());
		incG = accumulate(incG, destColor.getGreen());
		incB = accumulate(incB, destColor.getBlue());
		
		return new Color(incR.intValue(), incG.intValue(), incB.intValue());
	}
	
	private boolean equal(Float a, Float b) {
		
		return Math.abs(a - b) < 0.01;
	}
	
	private float accumulate(float src, int dest) {
		if(src < dest)
			src += step;
		else if(src > dest)
			src -= step;
		return src;
	}
	
	public void setStep(float val) {
		step = val;
	}
	
	private Color randomizeColor() {
		final int MAX = 0xFF;
		return new Color(rnd.nextInt(MAX), rnd.nextInt(MAX), rnd.nextInt(MAX));
	}
}
