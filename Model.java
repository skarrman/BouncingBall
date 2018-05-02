import java.awt.*;

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
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.2, 10);
		balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3, 20);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT
		if (isCollision(balls[0], balls[1])) {
//			System.out.println("Bounce!");
			Ball b = balls[0];
			Ball b2 = balls[1];
			double u1 = getVelocity(b.vx, b.vy);
			double u2 = getVelocity(b2.vx, b2.vy);

			double r = R(u1, u2);
			double i = I(b.mass, u1, b2.mass, u2);

			double v2 = v2(b.mass, b2.mass, r, i);
			double v1 = v1(v2, r);

			double a2 = getAngle(b.x, b.y, b2.x, b2.y);
			System.out.println(Math.toDegrees(a2));
			double a = (a2 - Math.PI) % (2*Math.PI);

			double constant = 0.2;

			b.vx = constant * getXVelocity(a, v1);
			b.vy = constant * getYVelocity(a, v1);

			b2.vx = constant * getXVelocity(a2, v2);
			b2.vy = constant * getYVelocity(a2, v2);
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

	double getYVelocity(double angle, double velocity){
		return Math.sin(angle) * velocity;
	}

	double getXVelocity(double angle, double velocity){
		return Math.cos(angle) * velocity;
	}

	double getAngle(double x1, double y1, double x2, double y2){
		double dx = x1-x2;
		double dy = y1-y2;

		return  Math.tan(dy/dx);
	}

	double getVelocity(double vx, double vy){
		return Math.sqrt((vx * vx) + (vy * vy));
	}

	double v1(double v2, double r){
		return v2-r;
	}

	double v2(double m1, double m2, double r, double i){
		return (r*m1 + i) / (m1-m2);
	}

	double R(double u1, double u2){
		return -(u2 - u1);
	}

	double I(double m1, double u1, double m2, double u2){
		return (m1 * u1) + (m2 * u2);
	}

	boolean isCollision(Ball b1, Ball b2){
		return Math.abs(Point.distance(b1.x,b1.y,b2.x,b2.y)) <= (b1.radius + b2.radius);
	}
	
	/**
	 * Simple inner class describing balls.
	 */
	class Ball {
		
		Ball(double x, double y, double vx, double vy, double r, double mass) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
			this.mass = mass;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius, mass;
	}
}
