import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

/**
 * 代表子弹的类
 *
 */
public class Missile {
	/**
	 * 子弹x方向的速度
	 */
	public static final int XSPEED = 10;
	/**
	 * 子弹y方向的速度
	 */
	public static final int YSPEED = 10;
	/**
	 * 子弹的宽度
	 */
	public static final int WIDTH = 10;
	/**
	 * 子弹的高度
	 */
	public static final int HEIGHT = 10;

	private static int ID = 1;

	TankClient tc;

	int tankId;

	int id;

	int x, y;

	Dir dir = Dir.R;

	boolean live = true;

	boolean good;
	
	/**
	 * 根据位置等属性构造子弹
	 * @param tankId 所属坦克的id号(用于网络版)
	 * @param x 子弹产生的x坐标
	 * @param y 子弹产生的y坐标
	 * @param good 子弹的立场是好还是坏
	 * @param dir 子弹的方向
	 * @see Dir
	 */
	
	public Missile(int tankId, int x, int y, boolean good, Dir dir) {
		this.tankId = tankId;
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = dir;
		this.id = ID++;
	}
	
	/**
	 * 根据位置和TankClient构建子弹
	 * @param tankId
	 * @param x
	 * @param y
	 * @param good
	 * @param dir
	 * @param tc 子弹构建的场所
	 * @see TankClient
	 */
	public Missile(int tankId, int x, int y, boolean good, Dir dir,
			TankClient tc) {
		this(tankId, x, y, good, dir);
		this.tc = tc;
	}
	
	/**
	 * 画出子弹
	 * @param g 画笔
	 */
	public void draw(Graphics g) {
		if (!live) {
			tc.missiles.remove(this);
			return;
		}

		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);

		move();
	}

	private void move() {
		switch (dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}

		if (x < 0 || y < 0 || x > TankClient.GAME_WIDTH
				|| y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	
	/**
	 * 取得子弹的外切方形
	 * @return 子弹的外切Rectangle
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * 检测子弹是否撞到坦克
	 * @param t 被检测的坦克
	 * @return 如果撞到返回true,否则返回false
	 */
	public boolean hitTank(Tank t) {
		if (this.live && t.isLive() && this.good != t.good
				&& this.getRect().intersects(t.getRect())) {
			this.live = false;
			t.setLive(false);
			tc.explodes.add(new Explode(x, y, tc));
			return true;
		}
		return false;
	}
	
	/**
	 * 检测是否撞到一系列坦克中的一个
	 * @param tanks 被检测的坦克序列
	 * @return 如果撞到其中一个,返回true,否则返回false
	 */
	public boolean hitTanks(List<Tank> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if (this.hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
}
