package ru.hse.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintApp {

	static class PaintPanel extends JPanel {

		private ArrayList<Point> points = new ArrayList<Point>();

		private Point2D.Double offset = new Point2D.Double(0, 0);
		private double scale = 1;
		private Point lastMousePos = new Point();

		public PaintPanel() {
			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						if (points.size() > 0)
							points.add(points.get(0));
					} else {
						points.add(new Point(
								(int) ((e.getPoint().x - offset.x) / scale),
								(int) ((e.getPoint().y - offset.y) / scale)));
					}
					repaint();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {

				@Override
				public void mouseMoved(MouseEvent e) {
					lastMousePos = e.getPoint();
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					offset.x += e.getPoint().x - lastMousePos.x;
					offset.y += e.getPoint().y - lastMousePos.y;
					lastMousePos = e.getPoint();

					repaint();
				}
			});
			addMouseWheelListener(new MouseWheelListener() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					double deltaScale = Math.exp(e.getWheelRotation() / 100.0);

					offset.x = (int) Math.round((offset.x - e.getPoint().x)
							* deltaScale + e.getPoint().x);
					offset.y = (int) Math.round((offset.y - e.getPoint().y)
							* deltaScale + e.getPoint().y);
					scale *= deltaScale;

					repaint();
				}
			});
		}

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			g.clearRect(0, 0, getSize().width, getSize().height);
			g.translate((int) offset.x, (int) offset.y);
			g2.scale(scale, scale);

			int[] xs = new int[points.size()];
			int[] ys = new int[points.size()];

			for (int i = 0; i < points.size(); ++i) {
				xs[i] = (int) points.get(i).getX();
				ys[i] = (int) points.get(i).getY();
			}

			g.drawPolyline(xs, ys, points.size());

			g.setColor(Color.BLUE);
			g.drawOval(-10,-10,20,20);
		}

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Paint frame");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new PaintPanel());
		frame.setVisible(true);
	}
}
