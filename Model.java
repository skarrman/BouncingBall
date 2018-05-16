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
	int time = 5;
	
	Ball [] balls;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		// Initialize the model with a few balls
		balls = new Ball[2];

//		balls[0] = new Ball(width / 3, height * 0.7, 1.1, -0.5, 0.2, Color.red);
//		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.7, 0.6, 0.3, Color.green);


		//No vy
		balls[0] = new Ball(width / 3, height * 0.7, 1.1, 0, 0.3, Color.red);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.7, 0, 0.3, Color.green);
	}

	void step(double deltaT) {

		if (time-- <= 0 && isCollision(balls[0], balls[1])) {

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

			double before1 = v1.x;
			double before2 = v2.x;


//			double r = R(v1.x, v2.x);
//			double i = I(ball1.mass, v1.x, ball2.mass, v2.x);

//			double vx1 = v1(ball1.mass, ball2.mass, r, i);
//			double vx2 = v2(vx1, ball1.mass, ball2.mass, i);

			double vx1 = v1(ball1.mass, ball2.mass, v1.x, v2.x);
			double vx2 = v2(ball1.mass, ball2.mass, v1.x, v2.x);

			System.out.println("Before: ball1: "+before1+", " +
					"ball2: "+before2+", " +
					"Momentum: "+(ball1.mass*Math.abs(before1)+ball2.mass*Math.abs(before2)));
			System.out.println("After: ball1: "+vx1+", " +
					"ball2: "+vx2+", " +
					"Momentum: "+(ball1.mass*Math.abs(vx1)+ball2.mass*Math.abs(vx2)));
			System.out.println();

			v1.x = vx1;
			v2.x = vx2;

			v1 = rotate(v1, b1);
			v2 = rotate(v2, b2);

			ball1.vx = v1.x;
			ball1.vy = v1.y;

			ball2.vx = v2.x;
			ball2.vy = v2.y;

			time = 10;

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

	double getVelocity(double vx, double vy){
		return Math.sqrt((vx * vx) + (vy * vy));
	}

	double v1(double m1, double m2, double u1, double u2){
		double rt = ((m1-m2)/(m1+m2))*u1;
		double lt = ((2*m2)/(m1+m2))*u2;

		return rt + lt;
	}

	double v2(double m1, double m2, double u1, double u2){
		double rt = ((2*m1)/(m1+m2))*u1;
		double lt = ((m2-m1)/(m1+m2))*u2;

		return rt + lt;
	}

	boolean isCollision(Ball b1, Ball b2){
		return Math.abs(Point.distance(b1.x,b1.y,b2.x,b2.y)) <= (b1.radius + b2.radius) * 1.001;
	}


//	double getYVelocity(double angle, double velocity){
//		return Math.sin(angle) * velocity;
//	}
//
//	double getXVelocity(double angle, double velocity){
//		return Math.cos(angle) * velocity;
//	}

//	double v1(double m1, double m2, double r, double i){
//		return (i - (m2 * r)) / (m1+m2);
//	}
//
//	double v2(double v1, double m1, double m2, double i){
//		return (i - m1*v1)/m2;
//	}
//
//	double R(double u1, double u2){
//		return -(u2 - u1);
//	}
//
//	double I(double m1, double u1, double m2, double u2){
//		return (m1 * u1) + (m2 * u2);
//	}

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
