import java.awt.Color;
import java.awt.Graphics;

/**
 * 代表爆炸的类
 * 使用大小不一的圆进行模拟
 *
 */
public class Explode {
	int x, y;

	private int[] diameters = { 4, 7, 12, 18, 26, 32, 49, 30, 14, 6 };

	private boolean live = true;

	private TankClient tc;

	int step = 0;
	
	/**
	 * 根据位置产生新的爆炸
	 * @param x 爆炸点的x坐标
	 * @param y 爆炸点的y坐标
	 */
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	/**
	 * 画出爆炸当前的圆
	 * @param g 画笔
	 * @see java.awt.Graphics
	 */
	public void draw(Graphics g) {
		if (!live) {
			tc.explodes.remove(this);
			return;
		}

		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, diameters[step], diameters[step]);
		g.setColor(c);

		step++;
		if (step == diameters.length) {
			live = false;
		}
	}
}
