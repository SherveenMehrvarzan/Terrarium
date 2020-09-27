
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TileGraphic extends JPanel{

	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	public Color color;
	
	// accepts a color which will be the color of the shape
	public TileGraphic(Color newColor) {
		this.color = newColor;
	}
	
	public JPanel getPanel(){
		return this;
	}
	
	public void changeColor(Color c) {
		this.color = c;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// creates the rectangle
		g.setColor(this.color);
		g.fillRect(0, 0, 60, 60);
	} 
}
