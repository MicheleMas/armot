/*
    ARMOT  Arp Monitoring Tool
    
    Copyright (C) 2012  Massaro Michele, Tomasello Alex

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class represents a Rounded Border JPanel.
 */
public class RoundedPanel extends JPanel {

	/** Stroke size. it is recommended to set it to 1 for better view */
	protected int strokeSize = 1;
	/** Color of shadow */
	protected Color shadowColor = Color.black;
	/** Sets if it drops shadow */
	protected boolean shady = true;
	/** Sets if it has an High Quality view */
	protected boolean highQuality = true;
	/** Double values for Horizzontal and Vertical radius of corner arcs */
	protected Dimension arcs = new Dimension(10, 10);
	/** Distance between border of shadow and border of opaque panel */
	protected int shadowGap = 2;
	/** The offset of shadow. */
	protected int shadowOffset = 4;
	/** The transparency value of shadow. ( 0 - 255) */
	protected int shadowAlpha = 150;

	public RoundedPanel(String text) {
		super();
		JLabel label=new JLabel(text);
		label.setOpaque(false);
		label.setFont(new Font("Arial", Font.BOLD, 16));
		add(label);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		int height = getHeight();
		int shadowGap = this.shadowGap;
		Graphics2D graphics = (Graphics2D) g;

		// Sets antialiasing if HQ.
		if (highQuality) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		}

		// Draws shadow borders if any.
		if (shady) {
			Point2D.Float p1 = new Point2D.Float(0.f, 75.f); // Gradient line start
			Point2D.Float p2 = new Point2D.Float(0.f, 0.f); // Gradient line end
			GradientPaint g1 = new GradientPaint(p1, new Color(189,200,200), p2,
					new Color(189,200,200), true);
			graphics.setPaint(g1);
			graphics.fillRoundRect(shadowOffset,// X position
					shadowOffset,// Y position
					width - strokeSize - shadowOffset, // width
					height - strokeSize - shadowOffset, // height
					arcs.width, arcs.height);// arc Dimension
		} else {
			shadowGap = 1;
		}

		// Draws the rounded opaque panel with borders.

		
		
		
		graphics.fillRoundRect(0, 0, width - shadowGap, height - shadowGap,
				arcs.width, arcs.height);
		graphics.setColor(getForeground());
		graphics.setStroke(new BasicStroke(strokeSize));
		
		graphics.drawRoundRect(0, 0, width - shadowGap, height - shadowGap,
				arcs.width, arcs.height);
		

		// Sets strokes to default, is better.
		graphics.setStroke(new BasicStroke());
//		graphics.drawString(getText(), 20, 20);
	}

	/**
	 * Check if component has High Quality enabled.
	 * 
	 * @return <b>TRUE</b> if it is HQ ; <b>FALSE</b> Otherwise
	 */
	public boolean isHighQuality() {
		return highQuality;
	}

	/**
	 * Sets whether this component has High Quality or not
	 * 
	 * @param highQuality
	 *            if <b>TRUE</b>, set this component to HQ
	 */
	public void setHighQuality(boolean highQuality) {
		this.highQuality = highQuality;
	}

	/**
	 * Returns the Color of shadow.
	 * 
	 * @return a Color object.
	 */
	public Color getShadowColor() {
		return shadowColor;
	}

	/**
	 * Sets the Color of shadow
	 * 
	 * @param shadowColor
	 *            Color of shadow
	 */
	public void setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
	}

	/**
	 * Check if component drops shadow.
	 * 
	 * @return <b>TRUE</b> if it drops shadow ; <b>FALSE</b> Otherwise
	 */
	public boolean isShady() {
		return shady;
	}

	/**
	 * Sets whether this component drops shadow
	 * 
	 * @param shady
	 *            if <b>TRUE</b>, it draws shadow
	 */
	public void setShady(boolean shady) {
		this.shady = shady;
	}

	/**
	 * Returns the size of strokes.
	 * 
	 * @return the value of size.
	 */
	public float getStrokeSize() {
		return strokeSize;
	}

	/**
	 * Sets the stroke size value.
	 * 
	 * @param strokeSize
	 *            stroke size value
	 */
	public void setStrokeSize(int strokeSize) {
		this.strokeSize = strokeSize;
	}

	/**
	 * Get the value of arcs
	 * 
	 * @return the value of arcs
	 */
	public Dimension getArcs() {
		return arcs;
	}

	/**
	 * Set the value of arcs
	 * 
	 * @param arcs
	 *            new value of arcs
	 */
	public void setArcs(Dimension arcs) {
		this.arcs = arcs;
	}

	/**
	 * Get the value of shadowOffset
	 * 
	 * @return the value of shadowOffset
	 */
	public int getShadowOffset() {
		return shadowOffset;
	}

	/**
	 * Set the value of shadowOffset
	 * 
	 * @param shadowOffset
	 *            new value of shadowOffset
	 */
	public void setShadowOffset(int shadowOffset) {
		if (shadowOffset >= 1) {
			this.shadowOffset = shadowOffset;
		} else {
			this.shadowOffset = 1;
		}
	}

	/**
	 * Get the value of shadowGap
	 * 
	 * @return the value of shadowGap
	 */
	public int getShadowGap() {
		return shadowGap;
	}

	/**
	 * Set the value of shadowGap
	 * 
	 * @param shadowGap
	 *            new value of shadowGap
	 */
	public void setShadowGap(int shadowGap) {
		if (shadowGap >= 1) {
			this.shadowGap = shadowGap;
		} else {
			this.shadowGap = 1;
		}
	}

	/**
	 * Get the value of shadowAlpha
	 * 
	 * @return the value of shadowAlpha
	 */
	public int getShadowAlpha() {
		return shadowAlpha;
	}

	/**
	 * Set the value of shadowAlpha
	 * 
	 * @param shadowAlpha
	 *            new value of shadowAlpha
	 */
	public void setShadowAlpha(int shadowAlpha) {
		if (shadowAlpha >= 0 && shadowAlpha <= 255) {
			this.shadowAlpha = shadowAlpha;
		} else {
			this.shadowAlpha = 255;
		}
	}
}