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
	int time = 5;
	
	Ball [] balls;

	Model(double width, double height) {
		areaWidth = width;
		areaHeight = height;
		
		// Initialize the model with a few balls
		balls = new Ball[2];
		balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.3, 20, Color.red);
				balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.3, 20, Color.green);
	}

	void step(double deltaT) {
		// TODO this method implements one step of simulation with a step deltaT

		if (time-- <= 0 && isCollision(balls[0], balls[1])) {
//			System.out.println("Bounce!");

			Ball firstBall;
			Ball secondBall;

			if(balls[0].y < balls[1].y) {
				firstBall = balls[0];
				secondBall = balls[1];
			}else {
				firstBall = balls[1];
				secondBall = balls[0];
			}


			double vp1 = getVelocity(firstBall.vx, firstBall.vy);
			double vp2 = getVelocity(secondBall.vx, secondBall.vy);

			double a1 = getAngle(firstBall.vx, firstBall.vy, 0, 0);
			double a2 = getAngle(secondBall.vx, secondBall.vy, 0, 0);

			double b1 = getAngle(firstBall.x, firstBall.y, secondBall.x, secondBall.y);
			double b2 = getAngle(secondBall.x, secondBall.y, firstBall.x, firstBall.y) - Math.PI;

			double t1 = b1 - a1;
			double t2 = b2 - a2;

			double ux1 = getXVelocity(t1, vp1);
			double ux2 = getXVelocity(t2, vp2);
			double uy1 = getYVelocity(t1, vp1);
			double uy2 = getYVelocity(t2, vp2);

			System.out.println(firstBall.mass+", "+vp1+", "+ux1+", "+t1);
			System.out.println(secondBall.mass+", "+vp2+", "+ux2+", "+t2);

			double r = R(ux1, ux2);
			double i = I(firstBall.mass, ux1, secondBall.mass, ux2);

			double vx1 = v1(firstBall.mass, secondBall.mass, r, i);
			double vx2 = v2(vx1, r);

			double v1 = getVelocity(vx1, uy1);
			double v2 = getVelocity(vx2, uy2);

			System.out.println(v1);
			System.out.println(v2);

			double q1 = getAngle(vx1, uy1,0,0);
			double q2 = getAngle(vx2, uy2,0,0);

			double n1 = q1 + a1;
			double n2 = q2 + a2 + Math.PI;

			firstBall.vx = getXVelocity(n1, v1);
			firstBall.vy = getYVelocity(n1, v1);

			secondBall.vx = getXVelocity(n2, v2);
			secondBall.vy = getYVelocity(n2, v2);

			time = 20;
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
		double dx = x2-x1;
		double dy = y2-y1;

		return  Math.atan(dy/dx);
	}

	double getVelocity(double vx, double vy){
		return Math.sqrt((vx * vx) + (vy * vy));
	}

	double v1(double m1, double m2, double r, double i){
		return (i - (m2 * r)) / (m1+m2);
	}

	double v2(double v1, double r){
		return r + v1;
	}

	double R(double u1, double u2){
		return -(u2 - u1);
	}

	double I(double m1, double u1, double m2, double u2){
		return (m1 * u1) + (m2 * u2);
	}

	boolean isCollision(Ball b1, Ball b2){
		return Math.abs(Point.distance(b1.x,b1.y,b2.x,b2.y)) <= (b1.radius + b2.radius) * 1.1;
	}
	
	/**
	 * Simple inner class describing balls.
	 */
	class Ball {
		
		Ball(double x, double y, double vx, double vy, double r, double mass, Color color) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
			this.radius = r;
			this.mass = mass;
			this.color = color;
		}

		/**
		 * Position, speed, and radius of the ball. You may wish to add other attributes.
		 */
		double x, y, vx, vy, radius, mass;
		Color color;
	}
}
