import java.awt.*;
import java.util.Vector;

/**
 * The physics model.
 * 
 * This class is where you should implement your bouncing balls model.
 * 
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 * 
 * @author Simon Robillard
 *
 */
class Model {

	double areaWidth, areaHeight;
	final static double GRAVITY = 9.82;
	Ball [] balls;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		// Initialize the model with a few balls
		balls = new Ball[2];

		balls[0] = new Ball(width / 3, height * 0.7, 1.1, 0, 0.3, Color.GREEN);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.7, 0, 0.2, Color.RED);
	}

	void step(double deltaT) {

		if (isCollision(balls[0], balls[1])) {

			Ball ball1,ball2;

			if(balls[1].x < balls[0].x && balls[1].y < balls[0].y){
				ball1 = balls[1];
				ball2 = balls[0];
			}else {
				ball1 = balls[0];
				ball2 = balls[1];
			}

			Vector v1 = new Vector(ball1.vx, ball1.vy);
			Vector v2 = new Vector(ball2.vx, ball2.vy);

			double b1 = getAngle(ball1.x,ball1.y, ball2.x, ball2.y);
			double b2 = getAngle(ball2.x,ball2.y, ball1.x, ball1.y);

			v1 = rotate(v1, -b1);
			v2 = rotate(v2, -b2);

			double vx1 = v1(ball1.mass, ball2.mass, v1.x, v2.x);
			double vx2 = v2(ball1.mass, ball2.mass, v1.x, v2.x);

			v1.x = vx1;
			v2.x = vx2;

			v1 = rotate(v1, b1);
			v2 = rotate(v2, b2);

			ball1.vx = v1.x;
			ball1.vy = v1.y;

			ball2.vx = v2.x;
			ball2.vy = v2.y;
		}

		for (Ball b : balls) {
			// detect collision with the border
			if (b.x < b.radius && b.vx < 0 || b.x > areaWidth - b.radius && b.vx > 0) {
				b.vx *= -1; // change direction of ball
			}
			else if(b.y < b.radius && b.vy < 0 || b.y > areaHeight - b.radius && b.vy > 0) {
				b.vy *= -1;
			}else{
				b.vy -= GRAVITY * deltaT;
			}
			// compute new position according to the speed of the ball
			b.x += deltaT * b.vx;
			b.y += deltaT * b.vy;
		}
	}

	Vector rotate(Vector vector, double angle){
		return new Vector(vector.x*Math.cos(angle) - vector.y*Math.sin(angle), vector.x*Math.sin(angle) + vector.y*Math.cos(angle));
	}


	double getAngle(double x1, double y1, double x2, double y2){
		double dx = x2-x1;
		double dy = y2-y1;

		return  Math.atan(dy/dx);
	}

	double v1(double m1, double m2, double u1, double u2){
		return ((m1-m2)/(m1+m2))*u1 + ((2*m2)/(m1+m2))*u2;
	}

	double v2(double m1, double m2, double u1, double u2){
		return ((2*m1)/(m1+m2))*u1 + ((m2-m1)/(m1+m2))*u2;
	}

	boolean isCollision(Ball b1, Ball b2){
		return Math.abs(Point.distance(b1.x,b1.y,b2.x,b2.y)) <= (b1.radius + b2.radius) * 1.001;
	}

	private class Vector {
		double x,y;

		Vector(double x, double y){
			this.x = x;
			this.y = y;
		}
	}
	
	/**
	 * Simple inner class describing balls.
	 */
	class Ball {
		
		Ball(double x, double y, double vx, double vy, double r, Color color) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
			this.mass = r*r * 15;
			this.color = color;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius, mass;
		Color color;
	}
}
